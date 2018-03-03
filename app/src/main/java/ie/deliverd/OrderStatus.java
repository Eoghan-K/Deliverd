package ie.deliverd;

public class OrderStatus {
    private boolean isSelected;
    private boolean isItemCollected;
    private boolean isDelivered;

    public OrderStatus(){
        isSelected = false;
        isItemCollected = false;
        isDelivered = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isItemCollected() {
        return isItemCollected;
    }

    public boolean isDelivered() {
        return isDelivered;
    }
}
