package ie.deliverd;

import java.util.List;

public class Order {
    private String orderID;
    private String orderTitle;
    private String vendorID;
    private String pickUpAddr;
    private String customerName;
    private String customerAddr;
    private String customerPh;
    private boolean isSelected;

    public  Order(){}

    public Order(String orderID, String orderTitle, String vendorID, String pickUpAddr, String customerName, String
            customerAddr, String customerPh) {
        this.orderID = orderID;
        this.orderTitle = orderTitle;
        this.vendorID = vendorID;
        this.pickUpAddr = pickUpAddr;
        this.customerName = customerName;
        this.customerAddr = customerAddr;
        this.customerPh = customerPh;
        this.isSelected = false;
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
