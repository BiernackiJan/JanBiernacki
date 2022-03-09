package models;

import java.util.Objects;
import static utils.Utilities.truncateString;

public class Item {

    private String itemDescription = "No Description";
    private boolean isItemCompleted = false;

    public Item(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Item(String itemDescription, boolean isItemCompleted) {
        this.itemDescription = truncateString(itemDescription, 50);
        this.isItemCompleted = isItemCompleted;
    }

    public String getItemDescription() {
        return truncateString(this.itemDescription,50);
    }

    public void setItemDescription(String itemDescription) {
        if (itemDescription.length() <= 50)
            this.itemDescription = itemDescription;
    }

    public boolean isItemCompleted() {
        return isItemCompleted;
    }

    public void setItemCompleted(boolean itemCompleted) {
        isItemCompleted = itemCompleted;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return isItemCompleted == item.isItemCompleted && Objects.equals(itemDescription, item.itemDescription);
    }

    public String toString() {
        String complete =  itemDescription + ". " ;
                if(!isItemCompleted){
                    complete += "[TODO]" + '\n';
                }
                else{
                    complete += "[Completed]" + '\n';
                }
                return complete;
    }
}
