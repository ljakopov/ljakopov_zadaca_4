/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.rest.klijenti;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.New;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.ljakopov.web.podaci.MeteoPodaci;
import org.foi.nwtis.ljakopov.web.podaci.MeteoPrognoza;

/**
 *
 * @author ljakopov klasa za dohvaćanje vremena adresa na temelju lat i log,
 * preko API key kojeg smo dobili generirali na openweathermap.org
 */
public class OWMKlijent {

    String apiKey;
    OWMRESTHelper helper;
    Client client;

    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
    }

    public MeteoPodaci getRealTimeWeather(String latitude, String longitude) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Current_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JsonReader reader = Json.createReader(new StringReader(odgovor));

            JsonObject jo = reader.readObject();

            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJsonObject("sys").getJsonNumber("sunrise").bigDecimalValue().longValue() * 1000));
            mp.setSunSet(new Date(jo.getJsonObject("sys").getJsonNumber("sunset").bigDecimalValue().longValue() * 1000));

            mp.setTemperatureValue(new Double(jo.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
            mp.setTemperatureMin(new Double(jo.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
            mp.setTemperatureMax(new Double(jo.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
            mp.setTemperatureUnit("celsius");

            mp.setHumidityValue(new Double(jo.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
            mp.setHumidityUnit("%");

            mp.setPressureValue(new Double(jo.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
            mp.setPressureUnit("hPa");

            mp.setWindSpeedValue(new Double(jo.getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
            mp.setWindSpeedName("");

            mp.setWindDirectionValue(new Double(jo.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");

            mp.setCloudsValue(jo.getJsonObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setPrecipitationMode("");

            mp.setWeatherNumber(jo.getJsonArray("weather").getJsonObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJsonArray("weather").getJsonObject(0).getString("icon"));

            mp.setLastUpdate(new Date(jo.getJsonNumber("dt").bigDecimalValue().longValue() * 1000));
            return mp;

        } catch (Exception ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * metoda služi za dohvaćanje prognoza. Prima latitude i longitude, oni služe kao "adresa".
     * Podatke preuzima sa JSON-a i sprema ih u listu klase MeteoPrognoza
     *
     * @param id
     * @param latitude
     * @param longitude
     * @return MeteoPrognoza[]
     */
    public MeteoPrognoza[] getWeatherForecast(int id, String latitude, String longitude) {

        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Forecast_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

        JsonReader reader = Json.createReader(new StringReader(odgovor));
        JsonObject jsonObject = reader.readObject();
        System.out.println("ISPIS: " + jsonObject.toString());
        JsonArray jsonArray = jsonObject.getJsonArray("list");
        int brojPrognoza = jsonArray.size();

        MeteoPrognoza[] mp = new MeteoPrognoza[brojPrognoza];
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                JsonObject iJsonObject = jsonArray.getJsonObject(i);
                DateFormat formatter = null;
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                MeteoPodaci meteoPodaci = new MeteoPodaci(new Date(), new Date(), (float) iJsonObject.getJsonObject("main").getJsonNumber("temp").doubleValue(),
                        (float) iJsonObject.getJsonObject("main").getJsonNumber("temp_min").doubleValue(),
                        (float) iJsonObject.getJsonObject("main").getJsonNumber("temp_max").doubleValue(), "C",
                        (float) iJsonObject.getJsonObject("main").getJsonNumber("humidity").doubleValue(), "%",
                        (float) iJsonObject.getJsonObject("main").getJsonNumber("pressure").doubleValue(), "hPa",
                        (float) iJsonObject.getJsonObject("wind").getJsonNumber("speed").doubleValue(), "", 0.0f, "", "",
                        iJsonObject.getJsonObject("clouds").getInt("all"), "", "OK", 0.0f, "", "",
                        iJsonObject.getJsonArray("weather").getJsonObject(0).getInt("id"), iJsonObject.getJsonArray("weather").getJsonObject(0).getString("description"),
                        iJsonObject.getJsonArray("weather").getJsonObject(0).getString("icon"), formatter.parse(iJsonObject.getString("dt_txt")));
                mp[i] = new MeteoPrognoza(id, 1, meteoPodaci);
            } catch (ParseException ex) {
                Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return mp;
    }
}
