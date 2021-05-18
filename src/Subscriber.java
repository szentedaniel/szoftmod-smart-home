import java.util.List;

public class Subscriber {
    private String subscriber;
    private String homeId;
    private String boilerType;
    private String airConditionerType;
    private List<Temperature> temperatures;

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getBoilerType() {
        return boilerType;
    }

    public void setBoilerType(String boilerType) {
        this.boilerType = boilerType;
    }

    public String getAirConditionerType() {
        return airConditionerType;
    }

    public void setAirConditionerType(String airConditionerType) {
        this.airConditionerType = airConditionerType;
    }

    public List<Temperature> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<Temperature> temperatures) {
        this.temperatures = temperatures;
    }
}
