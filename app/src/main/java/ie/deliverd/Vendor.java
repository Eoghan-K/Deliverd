package ie.deliverd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Vendor {
    private String vendorId;
    private String email;
    private String username;
    private String address;
    private HashMap<String, Order> orders;

    public Vendor(){

    }

    public Vendor(String vendorId, String email, String username, String address) {
        this.vendorId = vendorId;
        this.email = email;
        this.username = username;
        this.address = address;
        this.orders = new HashMap<>();
    }

    public HashMap<String, Order> getOrders() {
        return orders;
    }

    public void setOrders(HashMap<String, Order> orders) {
        this.orders = orders;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }
}
