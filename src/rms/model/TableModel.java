package rms.model;

public class TableModel {
    public final int tableNum;

    public final int seats;

    public final int indoorOutdoor;

    public final int waiterID;

    public TableModel(int tableNum, int seats, int indoorOutdoor, int waiterID) {
        this.tableNum = tableNum;
        this.seats = seats;
        this.indoorOutdoor = indoorOutdoor;
        this.waiterID = waiterID;
    }

    public int getTableNum() {
        return tableNum;
    }

    public int getSeats() {
        return seats;
    }

    public int isIndoorOutdoor() {
        return indoorOutdoor;
    }

    public int getWaiterID() {
        return waiterID;
    }
}
