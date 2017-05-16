/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.kontrole;

/**
 *
 * @author Lovro 
 * Klasa služi za spremanje mapa koje korisnik posjeduje na svojem
 * email računu na localhostu i kasniji ispis 
 */
public class Izbornik {

    private String labela;
    private String vrijednost;

    public Izbornik(String labela, String vrijednost) {
        this.labela = labela;
        this.vrijednost = vrijednost;
    }

    public String getLabela() {
        return labela;
    }

    public void setLabela(String labela) {
        this.labela = labela;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }

}
