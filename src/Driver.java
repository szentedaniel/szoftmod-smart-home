import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Driver implements IDriver {

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";

    private HttpClient getHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }


    @Override
    public int sendCommand(Subscriber subs, boolean boiler, boolean ac) {
        int result = 0;
        String boilerCommand = "";
        String acCommand = "";

        switch (subs.getBoilerType()) {
            case "Boiler 1200W":
                if (boiler) {
                    boilerCommand = "bX3434";
                } else {
                    boilerCommand = "bX1232";
                }
                break;
            case "Boiler p5600":
                if (boiler) {
                    boilerCommand = "cX7898";
                } else {
                    boilerCommand = "cX3452";
                }
                break;
            case "Boiler tw560":
                if (boiler) {
                    boilerCommand = "dX3422";
                } else {
                    boilerCommand = "dX111";
                }
                break;
            case "Boiler 1400L":
                if (boiler) {
                    boilerCommand = "kx8417";
                } else {
                    boilerCommand = "kx4823";
                }
                break;
            case "hibas":
                if (boiler) {
                    boilerCommand = "kecske";
                } else {
                    boilerCommand = "beka";
                }
                break;
            default:
                boilerCommand = "none";
                break;
        }

        switch (subs.getAirConditionerType()) {
            case "Air p5600":
                if (boiler) {
                    acCommand = "bX5676";
                } else {
                    acCommand = "bX3421";
                }
                break;
            case "Air c3200":
                if (boiler) {
                    acCommand = "cX3452";
                } else {
                    acCommand = "cX5423";
                }
                break;
            case "Air rk110":
                if (boiler) {
                    acCommand = "eX1111";
                } else {
                    acCommand = "eX2222";
                }
                break;
            default:
                acCommand = "none";
                break;
        }


        try {

            String url = "http://193.6.19.58:8182/smarthome/" + subs.getHomeId();
            String urlParameters = "{\"homeId\":\"" + subs.getHomeId() + "\", \"boilerCommand\":\"" + boilerCommand + "\", \"airConditionerCommand\":\"" + acCommand + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(urlParameters))
                    .uri(URI.create(url))
                    .setHeader("User-Agent", USER_AGENT)
                    .header("Content-Type", "text/plain")
                    .build();

            HttpResponse<String> response = getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//                ======= KIIRATAS TESZTHEZ =========

            boolean test = false;
            if (test) {
                System.out.println(subs.getHomeId() + "," + boilerCommand + "," + acCommand);
                System.out.println(urlParameters);
            }
            //                ======= KIIRATAS TESZTHEZ =========

            result = Integer.parseInt(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        return result;
    }

}
