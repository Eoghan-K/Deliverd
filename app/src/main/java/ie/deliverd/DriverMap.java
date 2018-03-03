package ie.deliverd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;

public class DriverMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Order order;
    private double[] pLatLong;
    private double[] cLatLong;
    private int state;
    private Button stateBtn;
    private Button cancelBtn;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        order = (Order) getIntent().getSerializableExtra("order");
        ref = FirebaseDatabase.getInstance().getReference("users/vendors/" + order.getVendorID() + "/orders");

        pLatLong = new double[]{order.getPickUpLatLong().get(0), order.getPickUpLatLong().get(1)};
        cLatLong = new double[]{order.getCustomerLatLong().get(0), order.getCustomerLatLong().get
                (1)};
        state = 0;
        stateBtn = findViewById(R.id.stateBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void
            onClick(View v) {
                if (state == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DriverMap.this);
                    builder.setMessage("Are you sure you want to start the journey")
                            .setTitle("Start Journey")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getDirections(pLatLong);
                                    stateBtn.setText("Pickup Complete");
                                    state = 1;
                                }
                            })
                            .setNegativeButton("No", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (state == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DriverMap.this);
                    builder.setMessage("Confirm that you have picked up the order")
                            .setTitle("Confirm Order Pickup")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getDirections(cLatLong);
                                    stateBtn.setText("Delivery Complete");
                                    state = 2;
                                    order.getOrderStatus().setItemCollected(true);
                                    ref.child(order.getOrderID()).setValue(order);
                                }
                            })
                            .setNegativeButton("No", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (state == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DriverMap.this);
                    builder.setMessage("Confirm that you have delivered the order")
                            .setTitle("Confirm Order Delivery")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    order.getOrderStatus().setDelivered(true);
                                    ref.child(order.getOrderID()).setValue(order);
                                    startActivity(new Intent(DriverMap.this, DriverDashboard
                                            .class));
                                    finish();
                                }
                            })
                            .setNegativeButton("No", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DriverMap.this);
                builder.setMessage("Are ou sure you want to cancel?")
                        .setTitle("Cancel Delivery")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                order.getOrderStatus().setSelected(false);
                                order.getOrderStatus().setItemCollected(false);
                                order.getOrderStatus().setDelivered(false);
                                ref.child(order.getOrderID()).setValue(order);
                                startActivity(new Intent(DriverMap.this, DriverDashboard
                                        .class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void getDirections(final double[] latLong) {
        mMap.clear();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission
                    .ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                        .ACCESS_FINE_LOCATION}, 1);
            }
        }

        final LatLng destination = new LatLng(latLong[0], latLong[1]);

        try {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager
                    .GPS_PROVIDER);
            LatLng lastLoc = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation
                    .getLongitude());
            LatLngBounds bounds = new LatLngBounds.Builder().include(lastLoc).include
                    (destination).build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 75));
        } catch (NullPointerException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));

                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude
                        ());
                mMap.addMarker(new MarkerOptions().position(currentPosition).title("You").icon
                        (BitmapDescriptorFactory.fromResource(R.drawable.icon)));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentPosition)
                        .zoom(mMap.getCameraPosition().zoom)
                        .bearing(mMap.getCameraPosition().bearing)
                        .tilt(mMap.getCameraPosition().tilt)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000,
                        null);

                String dest = Double.toString(latLong[0]) + "," + Double.toString(latLong[1]);
                String post = location.getLatitude() + "," + location.getLongitude();

                drawMap(dest, post);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
    }

    private void drawMap(String dest, String post) {
        List<LatLng> path = new ArrayList<>();

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCsMnZS9fYj9otRB8tKC-GVqtnKcorUleU")
                .build();

        DirectionsApiRequest req = DirectionsApi.getDirections(context, post, dest);
        try {
            DirectionsResult res = req.await();

            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            List<com.google.maps.model.LatLng> coords1 =
                                                    points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 :
                                                    coords1) {
                                                path.add(new LatLng(coord1.lat,
                                                        coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        List<com.google.maps.model.LatLng> coords1 = points
                                                .decodePath();
                                        for (com.google.maps.model.LatLng coord1 :
                                                coords1) {
                                            path.add(new LatLng(coord1.lat, coord1.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        if (path.size() > 0) {
            PolylineOptions polylineOptions = new PolylineOptions().addAll(path).color(Color.BLUE)
                    .width(5);
            mMap.addPolyline(polylineOptions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission
                        .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1,
                                locationListener);
                    } catch (NullPointerException ex) {
                        System.out.println(ex.getLocalizedMessage());
                    }
                }
            } else {
                order.getOrderStatus().setSelected(false);
                ref.child(order.getOrderID()).setValue(order);
                startActivity(new Intent(this, DriverDashboard.class));
                finish();
            }
        }
    }
}
