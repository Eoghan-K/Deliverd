package ie.deliverd;

import java.io.Serializable;

public class OrderStatus implements Serializable {
    private boolean isSelected;
    private boolean isItemCollected;
    private boolean isDelivered;

    public OrderStatus(){
        isSelected = false;
        isItemCollected = false;
        isDelivered = false;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setItemCollected(boolean itemCollected) {
        isItemCollected = itemCollected;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
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
