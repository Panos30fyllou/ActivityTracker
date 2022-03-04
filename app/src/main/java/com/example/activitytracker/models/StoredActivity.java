package com.example.activitytracker.models;

public class StoredActivity {
    private String Type;
    private long Seconds;

    public StoredActivity(){

    }
    public StoredActivity(String Type, long Seconds){
        this.Type = Type;
        this.Seconds = Seconds;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public long getSeconds() {
        return Seconds;
    }

    public void setSeconds(long seconds) {
        this.Seconds = seconds;
    }
}
