package ie.deliverd;


public class Driver {
    private String driverId;
    private String email;
    private String username;
    private String phNumber;

    public Driver(){

    }

    public Driver(String driverId, String email, String username, String phNumber) {
        this.driverId = driverId;
        this.email = email;
        this.username = username;
        this.phNumber = phNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public String getDriverId() {
        return driverId;
    }
}