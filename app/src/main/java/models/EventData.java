package models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikith_Shetty on 22/04/2016.
 */
public class EventData {
    @SerializedName("_id")
    String _id;
    @SerializedName("eventName")
    String eventName;
    @SerializedName("college")
    String college;
    @SerializedName("fee")
    int fee;
    @SerializedName("details")
    String details;
    @SerializedName("venue")
    Venue venue;

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
