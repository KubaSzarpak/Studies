/**
 * @author Szarpak Jakub S25511
 */

package zad1;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Service {
    private String countyCode;
    private String currency;

    public Service(String country) {

        for (Locale e : Locale.getAvailableLocales()) {
            if (e.getDisplayCountry(Locale.forLanguageTag("EN_US")).equals(country)) {
                countyCode = e.getCountry();
                this.currency = Currency.getInstance(e).getCurrencyCode();
            }
        }
    }

    public String getWeather(String city) {
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + countyCode + "&APPID=0ebe2b0bd2ccb170afe2e8e536148894");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Scanner reader = new Scanner(connection.getInputStream());

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader.nextLine(), JsonObject.class);

            JsonArray weather = jsonObject.get("weather").getAsJsonArray();

            String weatherDescription = weather.get(0).getAsJsonObject().get("description").getAsString();

            return weatherDescription;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getRateFor(String currency) {
        try {
            URL url = new URL("https://api.exchangerate.host/latest?base=" + currency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Scanner reader = new Scanner(connection.getInputStream());
            String json = reader.nextLine();

            Gson gson = new Gson();
            Map<String, Double> mapa = gson.fromJson(json, JsonRateFor.class).getRates();

            return mapa.get(this.currency);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getNBPRate() {
        try {
            URL urlA = new URL("http://api.nbp.pl/api/exchangerates/rates/A/" + currency + "?format=json");
            URL urlB = new URL("http://api.nbp.pl/api/exchangerates/tables/B/" + currency + "?format=json");
            HttpURLConnection connection = (HttpURLConnection) urlA.openConnection();

            if (connection.getResponseCode() > 299) {
                connection = (HttpURLConnection) urlB.openConnection();

                if (connection.getResponseCode() > 299){
                    return 1.0;
                }
            }

            Scanner reader = new Scanner(connection.getInputStream());
            String line = reader.nextLine();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(line, JsonObject.class);

            JsonArray rates = jsonObject.getAsJsonArray("rates");
            Double mid = rates.get(0).getAsJsonObject().get("mid").getAsDouble();

            return mid;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


class JsonRateFor {
    private Map<String, Double> rates;

    public Map<String, Double> getRates() {
        return rates;
    }
}