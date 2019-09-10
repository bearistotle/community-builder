package dev.bearistotle.communitybuilder.models.forms;

import dev.bearistotle.communitybuilder.models.Activity;
import dev.bearistotle.communitybuilder.models.Availability;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class AddAvailabilityForm {

    @NotNull
    @Size(min = 3, max = 200, message = "Name must be 3--200 characters.")
    private String name;

    @NotNull
    @Size(min = 10, max = 500, message = "Description must be 10--500 characters.")
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

    public AddAvailabilityForm(Availability availability, List<Activity> activities){
        this.name = availability.getName();
        this.description = availability.getDescription();
        this.activities = activities;
        this.date = availability.getDate().toString();
        this.startTime = availability.getStartTime().toString();
        this.endTime = availability.getEndTime().toString();
        this.recurrencePattern = availability.getRecurrencePattern();
    }

    public AddAvailabilityForm(List<Activity> activities){
        this.activities = activities;
    }
    public AddAvailabilityForm(){}

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
}
