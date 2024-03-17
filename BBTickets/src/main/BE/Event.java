package BE;


import java.time.LocalDateTime;

public class Event {

    private int eventId;
    private String eventType;
    private String eventLocation;
    private LocalDateTime eventStartTime;
    private LocalDateTime eventEndingTime;
    private String eventNotes;
    private String locationGuidance;


    public Event(int eventId, String eventType, String eventLocation, LocalDateTime eventStartTime) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventLocation = eventLocation;
        this.eventStartTime = eventStartTime;
    }
    public Event(int eventId, String eventType, String eventLocation, LocalDateTime eventStartTime, LocalDateTime eventEndingTime, String eventNotes, String locationGuidance) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventLocation = eventLocation;
        this.eventStartTime = eventStartTime;
        this.eventEndingTime = eventEndingTime;
        this.eventNotes = eventNotes;
        this.locationGuidance = locationGuidance;
    }

    public Event(String eventType, String eventLocation, LocalDateTime eventStartTime) {
        this.eventType = eventType;
        this.eventLocation = eventLocation;
        this.eventStartTime = eventStartTime;
    }

    public Event(){

    }


    public Event(String eventType, String eventLocation, LocalDateTime eventStartTime, LocalDateTime eventEndingTime, String eventNotes, String locationGuidance) {
        this.eventType = eventType;
        this.eventLocation = eventLocation;
        this.eventStartTime = eventStartTime;
        this.eventEndingTime = eventEndingTime;
        this.eventNotes = eventNotes;
        this.locationGuidance = locationGuidance;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public LocalDateTime getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(LocalDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public LocalDateTime getEventEndingTime() {
        return eventEndingTime;
    }

    public void setEventEndingTime(LocalDateTime eventEndingTime) {
        this.eventEndingTime = eventEndingTime;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public String getLocationGuidance() {
        return locationGuidance;
    }

    public void setLocationGuidance(String locationGuidance) {
        this.locationGuidance = locationGuidance;
    }

}
