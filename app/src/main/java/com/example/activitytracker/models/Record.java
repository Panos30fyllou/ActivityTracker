package com.example.activitytracker.models;

public class Record {

    private String Username;
    private long inVehicle;
    private long onFoot;
    private long onBicycle;
    private long running;
    private long walking;
    private long still;
    private long unknown;
    private long unidentifiable;
    private long tilting;

    public Record(){
        this.Username = State.user.getUsername();
        this.onBicycle = 0;
        this.onFoot = 0;
        this.walking = 0;
        this.tilting = 0;
        this.unidentifiable = 0;
        this.unknown = 0;
        this.running = 0;
        this.still = 0;

    }
    public Record(String username, long InVehicle, long OnFoot, long OnBicycle, long Running, long Still, long Unknown, long Walking, long Unidentifiable, long Tilting) {
        this.Username = username;
        this.onFoot = OnFoot;
        this.onBicycle = OnBicycle;
        this.running = Running;
        this.still = Still;
        this.unknown = Unknown;
        this.walking = Walking;
        this.unidentifiable = Unidentifiable;
        this.inVehicle = InVehicle;
        this.tilting = Tilting;


    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public long getInVehicle() {
        return inVehicle;
    }

    public void setInVehicle(long inVehicle) {
        this.inVehicle = inVehicle;
    }

    public long getOnFoot() {
        return onFoot;
    }

    public void setOnFoot(long onFoot) {
        this.onFoot = onFoot;
    }

    public long getOnBicycle() {
        return onBicycle;
    }

    public void setOnBicycle(long onBicycle) {
        this.onBicycle = onBicycle;
    }

    public long getRunning() {
        return running;
    }

    public void setRunning(long running) {
        this.running = running;
    }

    public long getWalking() {
        return walking;
    }

    public void setWalking(long walking) {
        this.walking = walking;
    }

    public long getStill() {
        return still;
    }

    public void setStill(long still) {
        this.still = still;
    }

    public long getUnknown() {
        return unknown;
    }

    public void setUnknown(long unknown) {
        this.unknown = unknown;
    }

    public long getUnidentifiable() {
        return unidentifiable;
    }

    public void setUnidentifiable(long unidentifiable) {
        this.unidentifiable = unidentifiable;
    }

    public long getTilting() {
        return tilting;
    }

    public void setTilting(long tilting) {
        this.tilting = tilting;
    }
}
