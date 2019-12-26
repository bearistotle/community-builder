package dev.bearistotle.communitybuilder.models;

import dev.bearistotle.communitybuilder.models.data.AvailabilityDao;
import dev.bearistotle.communitybuilder.models.data.EventDao;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private EventDao eventDao;

    @Autowired
    private AvailabilityDao availabilityDao;

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
            Map<String, String> credsMap = objectMapper.readValue(creds, Map.class);
            String API_KEY = credsMap.get("API_KEY");
            String API_USERNAME = credsMap.get("API_USERNAME");
            HashMap<String, HashMap<String, Double>> classifierResult = new HashMap<>();
            HashMap<String, Double> classification = new HashMap<>();

            // TODO: Make these an ENUM or store in json file and access
            //  (basically apply DRY principle)
            ArrayList<String> classifierNames = new ArrayList<>();
            classifierNames.add("topics");
            classifierNames.add("art-topics");
            classifierNames.add("business-topics");
            classifierNames.add("computer-topics");
            classifierNames.add("game-topics");
            classifierNames.add("health-topics");
            classifierNames.add("home-topics");
            classifierNames.add("recreation-topics");
            classifierNames.add("science-topics");
            classifierNames.add("society-topics");
            classifierNames.add("sport-topics");

            String textToClassify = this.getName() + ", " + this.getDescription();
            for (Activity activity : this.activities) {
                textToClassify += ", " + activity.getName() + ", " + activity.getDescription();
            }

            ArrayList<String> textsArray = new ArrayList<>();
            textsArray.add(textToClassify);
            JSONObject textsJson = new JSONObject();
            textsJson.accumulate("texts", textsArray);

            // TODO: Consult uClassify docs to figure out how to batch requests. On the last request I hit a "too many
            //   simultaneous requests" error from the API.
            for (String classifierName : classifierNames) {

                HttpResponse<JsonNode> response = Unirest.post(baseURL + API_USERNAME + "/" + classifierName + "/classify")
                        .header("content-type", "application/json")
                        .header("authorization", "Token " + API_KEY)
                        .body(textsJson)
                        .asJson();

                // TODO: Delete print statements when no longer needed
                System.out.println(response.getBody().toString());

                JSONArray responseArray = response.getBody()
                        .getArray()
                        .getJSONObject(0)
                        .getJSONArray("classification");

                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject result = (JSONObject) responseArray.get(i);
                    classification.put((String) result.get("className"), (Double) result.get("p"));
                }

                classifierResult.put(classifierName, classification);
                System.out.println("Body: " + response.getBody());
                System.out.println("Status Code: " + response.getStatus());
            }

            System.out.println("Classifier Result: " + classifierResult.toString());
            this.setClassification(classifierResult);

        } catch(UnirestException | IOException e){
                e.printStackTrace();
        }

    }

    // TODO: See if there is a library that does for Java what scikit does for Python. May be useful here.
    public void calculateMatchLevels(){
        // Check for classification?

        // Loop through all calendarItems; if id is not in match levels, calculate and add it; else skip.

        // Loop through availabilities
        ArrayList<Availability> allAvailabilities = new ArrayList<>();
        for (Availability a: allAvailabilities){
            if(this.getMatchLevels().keySet().contains(a.getId())){
            }else{
                // compare the classifications according to the following formula: The summation of the mean value for
                // each item's probability of being in the given class times 1 minus the absolute value of the
                // difference between each item's probability of being in the given class for each class in the
                // classification. Still need to figure out how to weight topic matches higher than subtopic matches.
                // TODO: Consider whether I should save the top categories to show as explanation of match to user.
                HashMap<String, HashMap<String, Double>> classification1 = this.getClassification();
                HashMap<String, HashMap<String, Double>> classification2 = a.getClassification();
                HashMap<String, Double> matchFactors = new HashMap<>();

                for (String classifier: classification1.keySet()){
                    Double matchFactor = 0.0;
                    for (String category: classification1.get(classifier).keySet()){
                        Double class1Prob = classification1.get(classifier).get(category);
                        Double class2Prob = classification2.get(classifier).get(category);
                        matchFactor += ((class1Prob + class2Prob) / 2) * (1- Math.abs(class1Prob - class2Prob));
                    }
                    matchFactors.put(classifier, matchFactor);
                }

            }
        }
    }

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
