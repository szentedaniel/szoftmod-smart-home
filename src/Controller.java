import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Controller {
    Loader loader = new Loader();
    Monitor monitor = new Monitor();

    public Controller() {
        Subscribers subs = loader.loadSubscribers();
        Driver driver = new Driver();

        while (true) {
            LocalTime ldate = LocalTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            System.out.println("\n=======" + ldate.getHour() + ":" + ldate.getMinute() + "=======");

            subs.getSubscribers().forEach(subscriber -> {

                System.out.println("\n-------" + subscriber.getHomeId() + "-------");
                Session session = monitor.getSession(subscriber.getHomeId());

                for (Temperature temperature : subscriber.getTemperatures()) {

                    String[] tmp = temperature.getPeriod().split("-");
                    if ((ldate.getHour() > Integer.parseInt(tmp[0])) && (ldate.getHour() <= Integer.parseInt(tmp[1]))) {
                        if (temperature.getTemperature() - 0.2 > session.temperature) {

                            System.out.println("Túl alacsony hőmérséklet. \n(kívánt: " + temperature.getTemperature() + ", jelenlegi: " + session.temperature + ")");
                            int resultDriver = driver.sendCommand(subscriber, true, false);

                            try {
                                Thread.sleep(2000);
                                if (resultDriver == 100) {
                                    System.out.println(resultDriver + " - A parancs végrehajtásra került");
                                } else if (resultDriver == 101) {
                                    System.out.println(resultDriver + " - Hibás parancs");
                                } else if (resultDriver == 102) {
                                    System.out.println(resultDriver + " - Nincs ilyen eszköz");
                                } else if (resultDriver == 103) {
                                    System.out.println(resultDriver + " - Nincs ilyen előfizető");
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (temperature.getTemperature() * 0.2 <= (temperature.getTemperature() - session.temperature)) {
                                System.out.println("LOGGED!");
                                log(dtf.format(now) + " [" + subscriber.getHomeId() + "] " + "kívánt: " + temperature.getTemperature() + ", jelenlegi: " + session.temperature + ", eltérés: " +
                                        String.format("%,.2f", (temperature.getTemperature() - session.temperature) / temperature.getTemperature() * 100) + "%");
                            }

                        } else if (temperature.getTemperature() + 0.2 < session.temperature) {

                            System.out.println("Túl magas hőmérséklet. \n(kívánt: " + temperature.getTemperature() + ", jelenlegi: " + session.temperature + ")");
                            int resultDriver = driver.sendCommand(subscriber, false, true);
                            try {

                                Thread.sleep(2000);
                                if (resultDriver == 100) {
                                    System.out.println(resultDriver + " - A parancs végrehajtásra került");
                                } else if (resultDriver == 101) {
                                    System.out.println(resultDriver + " - Hibás parancs");
                                } else if (resultDriver == 102) {
                                    System.out.println(resultDriver + " - Nincs ilyen eszköz");
                                } else if (resultDriver == 103) {
                                    System.out.println(resultDriver + " - Nincs ilyen előfizető");
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (temperature.getTemperature() * 0.2 <= (session.temperature - temperature.getTemperature())) {
                                System.out.println("LOGGED!");

                                log(dtf.format(now) + " [" + subscriber.getHomeId() + "] " + "kívánt: " + temperature.getTemperature() + ", jelenlegi: " + session.temperature + ", eltérés: " +
                                        String.format("%,.2f", (session.temperature - temperature.getTemperature()) / temperature.getTemperature() * 100) + "%");
                            }

                        } else {
                            System.out.println("Megfelelő hőmérséklet.");
                            System.out.println("(kívánt: " + temperature.getTemperature() + ", jelenlegi: " + session.temperature + ")");
                        }
                    }
                }
            });

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private void log(String log_text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("errors.log", true));
            bw.write(log_text);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


}
