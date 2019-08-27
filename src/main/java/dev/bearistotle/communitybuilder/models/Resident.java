package dev.bearistotle.communitybuilder.models;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Resident extends User {

    // TODO: Determine what data structure is appropriate for these. Do I need a Match object w/ events and users and
    //   a OneToMany relationship btw Resident and that object? Or something else? Ditto for availabilityMatches
    private List<Event> eventMatches;

    private List<Availability> availabilityMatches;

    @NotNull
    private boolean isAdmin;

    public Resident(String username, String email, String pwHash){
        super(username, email, pwHash);
        this.isAdmin = false;
        this.eventMatches = new ArrayList<>();
        this.availabilityMatches = new ArrayList<>();
    }

    public Resident(){
        super();
        this.eventMatches = new ArrayList<>();
        this.availabilityMatches = new ArrayList<>();
    }
    public boolean isAdmin() {
        return isAdmin;
    }

    public List<Event> getEventMatches() {
        return eventMatches;
    }

    public void setEventMatches(List<Event> eventMatches) {
        this.eventMatches = eventMatches;
    }

    public void addEventMatch(Event eventMatch){
        this.eventMatches.add(eventMatch);
    }

    public void removeEventMatch(Event eventMatch){
        this.eventMatches.remove(eventMatch);
    }

    public List<Availability> getAvailabilityMatches() {
        return availabilityMatches;
    }

    public void setAvailabilityMatches(List<Availability> availabilityMatches) {
        this.availabilityMatches = availabilityMatches;
    }

    public void addAvailabilityMatch(Availability availabilityMatch){
        this.availabilityMatches.add(availabilityMatch);
    }

    public void removeAvailabilityMatch(Availability availabilityMatch){
        this.availabilityMatches.remove(availabilityMatch);
    }

    @Override
    public String toString() {
        return "User{" +
                "isAdmin='" + isAdmin + '\'' +
                "username='" + this.getUsername() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                '}';
    }
}
