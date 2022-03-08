package com.example.activitytracker.models;

import java.util.ArrayList;

public class StatisticsRecord {

    private String Username;
    private long InVehicle;
    private long OnFoot;
    private long OnBicycle;
    private long Running;
    private long Walking;
    private long Still;
    private long Unknown;
    private long Unidentifiable;
    private long Tilting;
    private ArrayList<Long> Activities;

    public StatisticsRecord(){
    }


    public StatisticsRecord(String username, long InVehicle, long OnFoot, long OnBicycle, long Running, long Still, long Unknown, long Walking, long Unidentifiable, long Tilting){
        this.Username = username;
        this.OnFoot = OnFoot;
        this.OnBicycle = OnBicycle;
        this.Running = Running;
        this.Still = Still;
        this.Unknown = Unknown;
        this.Walking = Walking;
        this.Unidentifiable = Unidentifiable;
        this.InVehicle = InVehicle;
        this.Tilting = Tilting;



        //this.Activities = new ArrayList<DetectedActivity>();
        //Activities.add();
        Activities.add(this.Unidentifiable);
        Activities.add(this.InVehicle);
        Activities.add(this.Running);
        Activities.add(this.Still);
        Activities.add(this.Unknown);
        Activities.add(this.Walking);
    }

    public ArrayList<Long> getActivities() {
        return Activities;
    }

    public void setActivities() {
        this.Activities = new ArrayList<>();
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public long getInVehicle() {
        return InVehicle;
    }

    public void setInVehicle(long inVehicle) {
        this.InVehicle = inVehicle;
    }

    public long getOnFoot() {
        return OnFoot;
    }

    public void setOnFoot(long onFoot) {
        this.OnFoot = onFoot;
    }

    public long getOnBicycle() {
        return OnBicycle;
    }

    public void setOnBicycle(long onBicycle) {
        OnBicycle = onBicycle;
    }

    public long getRunning() {
        return Running;
    }

    public void setRunning(long running) {
        Running = running;
    }

    public long getWalking() {
        return Walking;
    }

    public void setWalking(long walking) {
        Walking = walking;
    }

    public long getStill() {
        return Still;
    }

    public void setStill(long still) {
        Still = still;
    }

    public long getUnknown() {
        return Unknown;
    }

    public void setUnknown(long unknown) {
        Unknown = unknown;
    }

    public long getUnidentifiable() {
        return Unidentifiable;
    }

    public void setUnidentifiable(long unidentifiable) {
        Unidentifiable = unidentifiable;
    }

    public long getTilting() {
        return Tilting;
    }

    public void setTilting(long tilting) {
        Tilting = tilting;
    }
}
