package dev.bearistotle.communitybuilder.models;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Entity
@DiscriminatorValue("availability")
@Transactional
public class Availability extends CalendarItem {

    @NotNull
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "availability_activity",
            joinColumns = { @JoinColumn(name = "availability_id") },
            inverseJoinColumns = { @JoinColumn(name = "activity_id") }
    )
    private List<Activity> activities;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    public Availability(String name,
                        String description,
                        User creator,
                        ArrayList<Activity> activities,
                        LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime,
                        String recurrencePattern){
        super(name,
                description,
                date,
                startTime,
                endTime,
                recurrencePattern);
        this.creator = creator;
        this.activities = activities;
    }

    public Availability(){

        this.activities = new ArrayList<>();
    }

    public void classify(){
        String baseURL = "https://api.uclassify.com/v1/";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FileInputStream creds = new FileInputStream("C:\\Users\\brand\\lc101\\community-builder\\src\\main\\resources\\credentials.json");
            Map<String,String> credsMap = objectMapper.readValue(creds, Map.class);
            String API_KEY = credsMap.get("API_KEY");
            String API_USERNAME = credsMap.get("API_USERNAME");

            ArrayList<String> classifierNames = new ArrayList<>();
            classifierNames.add("Art Topics");
            classifierNames.add("Business Topics");
            classifierNames.add("Computer Topics");
            classifierNames.add("Games Topics");
            classifierNames.add("Health Topics");
            classifierNames.add("Home Topics");
            classifierNames.add("Recreation Topics");
            classifierNames.add("Science Topics");
            classifierNames.add("Society Topics");
            classifierNames.add("Sports Topics");

            String textToClassify = this.getName() + ", " + this.getDescription();
            for (Activity activity: this.activities){
                textToClassify += ", " + activity.getName() + ", " + activity.getDescription();
            }

            ArrayList<String> textsArray = new ArrayList<>();
            textsArray.add(textToClassify);
            JSONObject textsJson = new JSONObject();
            textsJson.accumulate("texts", textsArray);

            HttpResponse<JsonNode> response = Unirest.post(baseURL + API_USERNAME + "/topics" + "/classify")
                    .header("content-type", "application/json")
                    .header("authorization", "Token " + API_KEY)
                    .body(textsJson)
                    .asJson();

            JSONArray responseArray = response.getBody()
                    .getArray()
                    .getJSONObject(0)
                    .getJSONArray("classification");


            System.out.println(responseArray);

            HashMap<String, HashMap<String, Double>> classifierResult = new HashMap<>();
            HashMap<String, Double> classification = new HashMap<>();

            // TODO: Move this list elsewhere (json file maybe or ENUM in a class)
            ArrayList<String> classNames = new ArrayList<>();
            classNames.add("Arts");
            classNames.add("Business");
            classNames.add("Computers");
            classNames.add("Games");
            classNames.add("Health");
            classNames.add("Home");
            classNames.add("Recreation");
            classNames.add("Science");
            classNames.add("Society");
            classNames.add("Sports");
            String classifier = "Topics";

            for (String className : classNames) {
                for (int i = 0; i < responseArray.length(); i++) {
                    Object result = responseArray.get(i);
                    System.out.println(result.toString());
                }
            }
            //this.setClassification(classification);

            System.out.println("Body: " + response.getBody());
            System.out.println("Status Code: " + response.getStatus());

        } catch (UnirestException | IOException e){
            e.printStackTrace();
        }



    }

    // TODO: See if there is a library that does for Java what scikit does for Python. May be useful here.
    public void calculateMatchLevels(){}

    public String getItemType(){
        return "availability";
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void removeActivity(Activity activity){ this.activities.remove(activity); }

    public void addActivity(Activity activity){ this.activities.add(activity); }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return "Availability{" +
                "availabilityId= " + this.getId() +
                "activities= " + activities +
                ", creator= " + creator +
                ", date= " + this.getDate() +
                ", startTime= " + this.getStartTime() +
                ", endTime= " + this.getEndTime() +
                ", recurrencePattern= " + this.getRecurrencePattern() + '\'' + '}';
    }
}
