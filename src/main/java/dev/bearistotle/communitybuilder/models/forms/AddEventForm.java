package dev.bearistotle.communitybuilder.models.forms;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.Event;
import dev.bearistotle.communitybuilder.models.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public class AddEventForm {

    @NotNull
    @Size(min = 3, max = 25, message = "Event names must be between 3 and 25 characters.")
    private String name;

    @NotNull
    @Size(min = 10, max = 500, message = "Descriptions must be between 10 and 500 characters.")
    private String description;

    @NotNull
    private List<Activity> activities;

    @NotNull
    private String date;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @NotNull
    private String recurrencePattern;

    @NotNull
    private List<Location> locations;

    private int minParticipants;

    private int maxParticipants;

    public AddEventForm(List<Location> locations, List<Activity> activities){
        this.locations = locations;
        this.activities = activities;
    }

    public AddEventForm(Event event, List<Location> locations, List<Activity> activities){
        this.name = event.getName();
        this.description = event.getDescription();
        this.activities = activities;
        this.date = event.getDate().toString();
        this.startTime = event.getStartTime().toString();
        this.endTime = event.getEndTime().toString();
        this.recurrencePattern = event.getRecurrencePattern();
        this.locations = locations;
        this.minParticipants = event.getMinParticipants();
        this.maxParticipants = event.getMaxParticipants();
    }

    public AddEventForm(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

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

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
