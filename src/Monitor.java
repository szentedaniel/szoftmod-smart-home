import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Monitor implements IMonitor {
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";

    private HttpClient getHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    @Override
    public Session getSession(String homeId) {

        Session resultSession = new Session();

        try {
            String url = "http://193.6.19.58:8182/smarthome/" + homeId;
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .setHeader("User-Agent", USER_AGENT) // request header
                    .build();

            HttpResponse<String> response = getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject object = new JSONObject(response.body());

            resultSession.sessionId = object.getString("sessionId");
            resultSession.temperature = object.getDouble("temperature");
            resultSession.boilerState = object.getBoolean("boilerState");
            resultSession.airConditionerState = object.getBoolean("airConditionerState");

            //                ======= KIIRATAS TESZTHEZ =========
            boolean test = false;
            if (test) {
                System.out.println(resultSession.sessionId);
                System.out.println(resultSession.temperature);
                System.out.println(resultSession.boilerState);
                System.out.println(resultSession.airConditionerState);
            }
            //                ======= KIIRATAS TESZTHEZ =========


        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        return resultSession;
    }
}
