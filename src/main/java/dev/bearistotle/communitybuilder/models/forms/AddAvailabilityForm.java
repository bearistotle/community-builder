package dev.bearistotle.communitybuilder.models.forms;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.Availability;
import dev.bearistotle.communitybuilder.models.Location;

import javax.validation.constraints.NotNull;
import java.util.List;

public class AddAvailabilityForm {

    @NotNull
    private String date;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @NotNull
    private String recurrencePattern;

    @NotNull
    private List<Activity> activities;

    private List<Location> locations;

    public AddAvailabilityForm(Availability availability, List<Activity> activities, List<Location> locations){
        this.date = availability.getDate().toString();
        this.startTime = availability.getStartTime().toString();
        this.endTime = availability.getEndTime().toString();
        this.recurrencePattern = availability.getRecurrencePattern();
        this.activities = activities;
        this.locations = locations;
    }

    public AddAvailabilityForm(List<Activity> activities, List<Location> locations){
        this.activities = activities;
        this.locations = locations;
    }
    public AddAvailabilityForm(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
