package com.nikith_shetty.vgroup;

/**
 * Created by Nikith_Shetty on 12/04/2016.
 */
public class eventData {
    private String eventName;
    private String college;
    private int fee;
    private String venue;
    private String details;
    private String coordinatorInfo;
    //private eventData subEvents;


    public eventData(String eventName, String college, int fee, String venue, String details, String coordinatorInfo){
        this.eventName = eventName;
        this.college = college;
        this.fee = fee;
        this.venue = venue;
        this.details = details;
        this.coordinatorInfo = coordinatorInfo;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getFee() {
        return String.valueOf(fee);
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCoordinatorInfo() {
        return coordinatorInfo;
    }

    public void setCoordinatorInfo(String coordinatorInfo) {
        this.coordinatorInfo = coordinatorInfo;
    }
}
