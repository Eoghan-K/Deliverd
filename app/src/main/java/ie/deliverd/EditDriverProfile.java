package ie.deliverd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditDriverProfile extends AppCompatActivity {


    public static final int IMAGE_GALLERY_REQUEST = 20;
    private TextView UserName;
    private Button SaveBtn;
    private DatabaseReference orderDB;
    private Button EditPictures;
    private Button EditInfo;
    private ImageView Pfp;
    /*public final static String EXTRA_MESSAGE = "com.example.name.MESSAGE";*/

    private FirebaseAuth mRef;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_driver_profile);

        //REFERENCING the image view , so its available to any method within this class is in the on create
        //the best place to do it is in the oncreate which gets called as an initialisation call
        //when we are creating this activity

        //find view by id is asking to give a reference to this unique key
        //get a reference to teh image view that holds the image that the user will see
        Pfp = (ImageView) findViewById(R.id.Pfp);


        EditPictures = findViewById(R.id.EditPictures);
        EditInfo = findViewById(R.id.EditInfo);
        SaveBtn = findViewById(R.id.SaveBtn);
        UserName = findViewById(R.id.UserName);
        mRef = FirebaseAuth.getInstance();
        orderDB = FirebaseDatabase.getInstance().getReference("users/drivers/" + mRef.getUid() + "/username");

        UserName.setText(mRef.getCurrentUser().getDisplayName());





        /*SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(EditDriverProfile.this, DriverProfile.class);
                TextView UserName = ((TextView) findViewById(R.id.UserName));
                String name = UserName.getText().toString();
                intent.putExtra("name_here",name);
                startActivity(intent);
            }*/
            /*@Override
            public void onClick(View v) {
                startActivity(new Intent(EditDriverProfile.this, DriverProfile.class));
            }*/
        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditDriverProfile.this, DriverProfile.class));
            }
        });


        sp=getSharedPreferences("profilePicture",MODE_PRIVATE);

        String s = getIntent().getStringExtra("Username");

        if(!sp.getString("dp","").equals("")){
            byte[] decodedString = Base64.decode(sp.getString("dp", ""), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Pfp.setImageBitmap(decodedByte);
        }

    }




    /**
     *
     * This Method will be invoked when the user clicks a button
     * @param v
     */
    public void onImageGalleryClicked(View v) {
        //invoke the image gallary using an implicict intent
        Intent EditPictures = new Intent(Intent.ACTION_PICK);


        //where do we want to find the data
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //get the public directory E certain media types are stored
        String pictureDirectoryPath = pictureDirectory.getPath();

        //get a URI representation

        Uri data  = Uri.parse(pictureDirectoryPath);

        //set data(where we want to look for the  media) + type(what media are we looking for)
        EditPictures.setDataAndType(data, "image/*");
        //request code is a number i can make just as long as its not been used before
        //else we could call multiple intents from this screen and we need a unique id to know which intent is which
        //refactor the number, extract it and make it a constant
        startActivityForResult(EditPictures, IMAGE_GALLERY_REQUEST);
        //we will invoke this activity and receive smt back from it


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //request code: constant in the activity results to avoid returning multiple items, its  way to dtermine which activity is returning
        //result code: tells us if called activity executes correctly or if i cancelled out ou the activity
        //intent data: any data the results return
        if(resultCode == RESULT_OK){
            //if everything went well
            if(requestCode == IMAGE_GALLERY_REQUEST){
                //IF HERE; we are hearing  back from image gallery
                //URI = universal resource indicator = The address of the image
                Uri imageUri = data.getData();
                //declare a stream to read image data from gallery
                InputStream inputStream;
                //we are getting an input stream based on the URI of teh image
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    //get a bitmap from teh stream
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    //get a reference to pfp
                    //show the image to me
                    Pfp.setImageBitmap(image);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    sp.edit().putString("dp", encodedImage).commit();

                } catch (FileNotFoundException e) {//this catch area only runs if a problem occurred
                    e.printStackTrace();
                    //show a message to the user infomring them that the imaeg is inaccessible
                    Toast.makeText(this, "Unable to open Image", Toast.LENGTH_LONG).show();
                }

            }


        }


    }
}
