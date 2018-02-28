package ie.deliverd;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private String orderID;
    private String orderTitle;
    private String vendorID;
    private String pickUpAddr;
    private String customerName;
    private String customerAddr;
    private String customerPh;
    private boolean isSelected;
    private List<Double> pickUpLatLong;
    private List<Double> customerLatLong;

    public  Order(){}

    public Order(String orderID, String orderTitle, String vendorID, String pickUpAddr, String customerName, String
            customerAddr, String customerPh, final double pickUpLat, final double pickUpLong, final double deliveryLat, final double deliveryLong) {
        this.orderID = orderID;
        this.orderTitle = orderTitle;
        this.vendorID = vendorID;
        this.pickUpAddr = pickUpAddr;
        this.customerName = customerName;
        this.customerAddr = customerAddr;
        this.customerPh = customerPh;
        this.isSelected = false;
        pickUpLatLong = new ArrayList<Double>(){{
            add(pickUpLat);
            add(pickUpLong);
        }};
        customerLatLong = new ArrayList<Double>(){{
            add(deliveryLat);
            add(deliveryLong);
        }};
    }

    public List<Double> getPickUpLatLong() {
        return pickUpLatLong;
    }

    public List<Double> getDeliveryLatLong() {
        return customerLatLong;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public String getPickUpAddr() {
        return pickUpAddr;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddr() {
        return customerAddr;
    }

    public String getCustomerPh() {
        return customerPh;
    }

    public boolean isSelected() {
        return isSelected;
    }
}