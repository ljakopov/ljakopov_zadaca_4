/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.ljakopov.rest.klijenti.GMKlijent;
import org.foi.nwtis.ljakopov.rest.klijenti.OWMKlijent;
import org.foi.nwtis.ljakopov.web.podaci.Lokacija;
import org.foi.nwtis.ljakopov.web.podaci.MeteoPrognoza;

/**
 *
 * @author ljakopov
 */
@Stateless
@LocalBean
public class MeteoIoTKlijent {

    /**
     * varijabla api_key služi za dohvaćanje prognoza sa
     * https://openweathermap.org/ servisa
     */
    private String api_key;

    /**
     * metoda služa za dohvaćanje api_key-a sa aplikacije ljakopov_zadaca_4_3
     *
     * @param api_key
     */
    public void postaviKorisnickePodatke(String api_key) {
        this.api_key = api_key;
    }

    /**
     * metoda služi za dohvaćanje latitude i longitude odabrane adrese sa
     * servisa
     *
     * @param adresa
     * @return Lokacija
     */
    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent();
        return gmk.getGeoLocation(adresa);
    }

    /**
     * metoda služi za dohvaćanje prognoza sa https://openweathermap.org/
     * servisa. Metoda vraća listu prognoza kroz 5 dana za neko određeno mjesto
     * ili adresu.
     *
     * @param id
     * @param adresa
     * @return MeteoPrognoza[]
     */
    public MeteoPrognoza[] dajMeteoPrognoze(int id, String adresa) {
        OWMKlijent owmk = new OWMKlijent(api_key);
        Lokacija l = dajLokaciju(adresa);
        return owmk.getWeatherForecast(id, l.getLatitude(), l.getLongitude());
    }

    /**
     * metoda služi za dohvaćanje adrese mjesta preko latitude i longitude.
     *
     * @param lat
     * @param lot
     * @return String
     */
    public String dajMjesto(String lat, String lot) {
        GMKlijent gmk = new GMKlijent();
        return gmk.getGeoLocationFromLatLot(lat, lot);
    }
}
