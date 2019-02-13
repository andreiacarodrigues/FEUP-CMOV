package org.feup.cmov.aef.cmov1_tic.entities;

public class Ticket
{
    public String id;
    public String performanceName;
    public String performanceDate;
    public int placeInRoom;
    public boolean Used = false;

    public Ticket(String id, String performanceName, String performanceDate, int placeInRoom, boolean used)
    {
        this.id = id;
        this.performanceName = performanceName;
        this.performanceDate = performanceDate;
        this.placeInRoom = placeInRoom;
        this.Used = used;
    }

    public Ticket(String id, String performanceName, String performanceDate, int placeInRoom)
    {
        this(id, performanceName, performanceDate, placeInRoom, false);
    }

    public String getId() {
        return id;
    }

    public String getPerformanceName() {
        return performanceName;
    }

    public String getPerformanceDate() {
        return performanceDate;
    }

    public String getPlaceInRoom() {
        return String.valueOf(placeInRoom);
    }

    public Boolean getUsed() {
        return Used;
    }
}
