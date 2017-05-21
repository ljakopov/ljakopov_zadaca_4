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

    private String api_key = "73f8bbb7d2439f4fdf7d2bc55acd4ebf";

    public void postaviKorisnickePodatke(String api_key) {
        this.api_key = api_key;
    }

    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent();
        return gmk.getGeoLocation(adresa);
    }

    public MeteoPrognoza[] dajMeteoPrognoze(int id, String adresa) {
        OWMKlijent owmk = new OWMKlijent(api_key);
        Lokacija l = dajLokaciju(adresa);
        return owmk.getWeatherForecast(id, l.getLatitude(), l.getLongitude());
    }
    
    public String dajMjesto(String lat, String lot){
        GMKlijent gmk = new GMKlijent();
        return gmk.getGeoLocationFromLatLot(lat,lot);
    }
}
