import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Loader implements ILoader {

    private final Subscribers subscribers = new Subscribers();

    private Subscribers init() {
        try {

            BufferedReader br = new BufferedReader(new FileReader("subscribers.json"));

            String line;
            List<Subscriber> subs = new ArrayList<>();
            StringBuilder builder = new StringBuilder();

            while ((line = br.readLine()) != null) {
                builder.append(line);
            }

            String response = builder.toString();

            JSONObject json_body = new JSONObject(response);

            JSONArray json_subs = json_body.getJSONArray("subscribers");

            for (int i = 0; i < json_subs.length(); i++) {
                Subscriber subscriber = new Subscriber();
                JSONObject json_sub = json_subs.getJSONObject(i);
                subscriber.setSubscriber(json_sub.getString("subscriber"));
                subscriber.setHomeId(json_sub.getString("homeId"));
                subscriber.setBoilerType(json_sub.getString("boilerType"));
                subscriber.setAirConditionerType(json_sub.getString("airConditionerType"));

                List<Temperature> list = new ArrayList<>();
                JSONArray json_temps = json_sub.getJSONArray("temperatures");

                for (int j = 0; j < json_temps.length(); j++) {
                    JSONObject json_temp = json_temps.getJSONObject(j);
                    Temperature temperature = new Temperature();

                    temperature.setPeriod(json_temp.getString("period"));
                    temperature.setTemperature(json_temp.getDouble("temperature"));

                    list.add(temperature);
                }

                subscriber.setTemperatures(list);
//                ======= KIIRATAS TESZTHEZ =========
                boolean test = false;
                if (test) {
                    System.out.println("homeId:" + subscriber.getHomeId());
                    System.out.println("subscriber:" + subscriber.getSubscriber());
                    System.out.println("boilerType:" + subscriber.getBoilerType());
                    System.out.println("airConditionerType:" + subscriber.getAirConditionerType());
                    for (Temperature temp : subscriber.getTemperatures()) {
                        System.out.println("period:" + temp.getPeriod());
                        System.out.println("temperature: " + temp.getTemperature());
                    }
                    System.out.println("\n===============\n");
                }
//                ======= KIIRATAS TESZTHEZ =========

                subs.add(subscriber);
                subscribers.setSubscribers(subs);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return subscribers;
    }

    @Override
    public Subscribers loadSubscribers() {
        return init();
    }
}
