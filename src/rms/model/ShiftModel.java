package rms.model;

public class ShiftModel {

    public final int shiftID;

    public final int breakTime;

    public final String startTime;

    public final String endTime;

    public final int duration;

    public ShiftModel(int shiftID, int breakTime, String startTime, String endTime, int duration){
        this.shiftID = shiftID;
        this.breakTime = breakTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public int getShiftID() {
        return shiftID;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return duration;
    }
}
