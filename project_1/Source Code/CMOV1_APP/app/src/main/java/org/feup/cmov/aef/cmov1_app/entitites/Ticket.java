package org.feup.cmov.aef.cmov1_app.entitites;

public class Ticket
{
    public String id;
    public int performanceId;
    public String performanceName;
    public String performanceDate;
    public int placeInRoom;
    public boolean Used = false;
    public boolean selected = false;

    public Ticket(String id, int performanceId, String performanceName, String performanceDate, int placeInRoom, boolean used)
    {
        this.id = id;
        this.performanceId = performanceId;
        this.performanceName = performanceName;
        this.performanceDate = performanceDate;
        this.placeInRoom = placeInRoom;
        this.Used = used;
    }

    public Ticket(String id, int performanceId, String performanceName, String performanceDate, int placeInRoom)
    {
        this(id, performanceId, performanceName, performanceDate, placeInRoom, false);
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
}
