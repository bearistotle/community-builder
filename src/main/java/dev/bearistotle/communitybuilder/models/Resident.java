package dev.bearistotle.communitybuilder.models;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class Resident extends User {

    @OneToMany
    private ArrayList<Availability> availabilities;

    @OneToMany
    private ArrayList<Event> eventMatches;

    @OneToMany
    private ArrayList<Availability> availabilityMatches;

    @NotNull
    private boolean isAdmin;

    public Resident(String username, String email, String pwHash){
        super(username, email, pwHash);
        this.isAdmin = false;
        this.availabilities = new ArrayList<>();
        this.eventMatches = new ArrayList<>();
        this.availabilityMatches = new ArrayList<>();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public ArrayList<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(ArrayList<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public void addAvailability(Availability availability){
        this.availabilities.add(availability);
    }

    public void removeAvailability(Availability availability){
        this.availabilities.remove(availability);
    }

    public ArrayList<Event> getEventMatches() {
        return eventMatches;
    }

    public void setEventMatches(ArrayList<Event> eventMatches) {
        this.eventMatches = eventMatches;
    }

    public void addEventMatch(Event eventMatch){
        this.eventMatches.add(eventMatch);
    }

    public void removeEventMatch(Event eventMatch){
        this.eventMatches.remove(eventMatch);
    }

    public ArrayList<Availability> getAvailabilityMatches() {
        return availabilityMatches;
    }

    public void setAvailabilityMatches(ArrayList<Availability> availabilityMatches) {
        this.availabilityMatches = availabilityMatches;
    }

    public void addAvailabilityMatch(Availability availabilityMatch){
        this.availabilityMatches.add(availabilityMatch);
    }

    public void removeAvailabilityMatch(Availability availabilityMatch){
        this.availabilityMatches.remove(availabilityMatch);
    }
}
