package ie.deliverd;

public class Vendor {
    String vendorId;
    String email;
    String username;
    String address;

    public Vendor(){

    }

    public Vendor(String vendorId, String email, String username, String address) {
        this.vendorId = vendorId;
        this.email = email;
        this.username = username;
        this.address = address;
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
