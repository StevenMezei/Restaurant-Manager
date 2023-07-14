package rms.model;

public class MenuItemModel {
    public final String itemName;
    public final double price;
    public final int preparationTime;

    public MenuItemModel(String itemName, double price, int preparationTime) {
        this.itemName = itemName;
        this.price = price;
        this.preparationTime = preparationTime;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

//    @Override
//    public String toString() {
//        return "name: " + getItemName().trim() + "\tprice: " + getPrice() + "\tprep time: " + getPreparationTime();
//    }
}
