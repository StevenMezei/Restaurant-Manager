package rms.model;

public class IngredientModel {
    public final String name;

    public final int shelfLife;

    public final String storageLocation;

    public final String supplierName;

    public final double price;

    public final int orderQuantity;

    public final String foodType;

    public IngredientModel(String name, int shelfLife, String storageLocation, String supplierName, double price, int orderQuantity, String foodType) {
        this.name = name;
        this.shelfLife = shelfLife;
        this.storageLocation = storageLocation;
        this.supplierName = supplierName;
        this.price = price;
        this.orderQuantity = orderQuantity;
        this.foodType = foodType;
    }

    public String getName() {
        return name;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public double getPrice() {
        return price;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public String getFoodType() {
        return foodType;
    }
}
