/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.ljakopov.ejb.eb.Dnevnik;
import org.foi.nwtis.ljakopov.ejb.eb.Promjene;
import org.foi.nwtis.ljakopov.ejb.eb.Uredaji;
import org.foi.nwtis.ljakopov.ejb.sb.DnevnikFacade;
import org.foi.nwtis.ljakopov.ejb.sb.MeteoIoTKlijent;
import org.foi.nwtis.ljakopov.ejb.sb.PromjeneFacade;
import org.foi.nwtis.ljakopov.ejb.sb.UredajiFacade;
import org.foi.nwtis.ljakopov.web.kontrole.Izbornik;
import org.foi.nwtis.ljakopov.web.podaci.Lokacija;
import org.foi.nwtis.ljakopov.web.podaci.MeteoPrognoza;

/**
 *
 * @author Lovro
 */
@Named(value = "odabirIoTPrognoza")
@SessionScoped
public class OdabirIoTPrognoza implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    @EJB
    private PromjeneFacade promjeneFacade;

    @EJB
    private MeteoIoTKlijent meteoIoTKlijent;

    @EJB
    private UredajiFacade uredajiFacade;

    /**
     * Creates a new instance of OdabirIoTPrognoza
     */
    public OdabirIoTPrognoza() {
    }

    private String noviId;
    private String noviNaziv;
    private String noviAdresa;
    private List<Izbornik> raspoloziviIoT = new ArrayList<>();
    private List<Izbornik> odabraniIoT = new ArrayList<>();
    private List<String> popisRaspoloziviIoT = new ArrayList<>();
    private List<String> popisOdabraniIoT = new ArrayList<>();
    private String azurirajId;
    private String azurirajNaziv;
    private String azurirajAdresa;
    private List<MeteoPrognoza> meteoPrognoze = new ArrayList<>();
    private boolean azuriranje;
    private boolean prognoze = false;
    private boolean prvi = true;
    Uredaji uredjajZaAzuriranje;
    private String gumbPregledPrognoza = "Pregled prognoza";
    private MeteoPrognoza[] lis;

    public String getNoviId() {
        return noviId;
    }

    public void setNoviId(String noviId) {
        this.noviId = noviId;
    }

    public String getNoviNaziv() {
        return noviNaziv;
    }

    public void setNoviNaziv(String noviNaziv) {
        this.noviNaziv = noviNaziv;
    }

    public String getNoviAdresa() {
        return noviAdresa;
    }

    public void setNoviAdresa(String noviAdresa) {
        this.noviAdresa = noviAdresa;
    }

    public List<Izbornik> getRaspoloziviIoT() {
        if (this.prvi == true) {
            preuzmiRaspoloziveIoTUredaje();
            prvi = false;
        }
        return raspoloziviIoT;
    }

    public void setRaspoloziviIoT(List<Izbornik> raspoloziviIoT) {
        this.raspoloziviIoT = raspoloziviIoT;
    }

    public List<Izbornik> getOdabraniIoT() {
        return odabraniIoT;
    }

    public void setOdabraniIoT(List<Izbornik> odabraniIoT) {
        this.odabraniIoT = odabraniIoT;
    }

    public List<String> getPopisRaspoloziviIoT() {
        return popisRaspoloziviIoT;
    }

    public void setPopisRaspoloziviIoT(List<String> popisRaspoloziviIoT) {
        this.popisRaspoloziviIoT = popisRaspoloziviIoT;
    }

    public List<String> getPopisOdabraniIoT() {
        return popisOdabraniIoT;
    }

    public void setPopisOdabraniIoT(List<String> popisOdabraniIoT) {
        this.popisOdabraniIoT = popisOdabraniIoT;
    }

    public String getAzurirajId() {
        return azurirajId;
    }

    public void setAzurirajId(String azurirajId) {
        this.azurirajId = azurirajId;
    }

    public String getAzurirajNaziv() {
        return azurirajNaziv;
    }

    public void setAzurirajNaziv(String azurirajNaziv) {
        this.azurirajNaziv = azurirajNaziv;
    }

    public String getAzurirajAdresa() {
        return azurirajAdresa;
    }

    public void setAzurirajAdresa(String azurirajAdresa) {
        this.azurirajAdresa = azurirajAdresa;
    }

    public List<MeteoPrognoza> getMeteoPrognoze() {
        return meteoPrognoze;
    }

    public void setMeteoPrognoze(List<MeteoPrognoza> meteoPrognoze) {
        this.meteoPrognoze = meteoPrognoze;
    }

    public boolean isAzuriranje() {
        return azuriranje;
    }

    public void setAzuriranje(boolean azuriranje) {
        this.azuriranje = azuriranje;
    }

    public boolean isPrognoze() {
        return prognoze;
    }

    public void setPrognoze(boolean prognoze) {
        this.prognoze = prognoze;
    }

    public String getGumbPregledPrognoza() {
        return gumbPregledPrognoza;
    }

    public void setGumbPregledPrognoza(String gumbPregledPrognoza) {
        this.gumbPregledPrognoza = gumbPregledPrognoza;
    }

    private void spremiUPromjene(int id, String naziv, float lat, float log, int status) {
        Promjene promjene = new Promjene();
        promjene.setId(id);
        promjene.setNaziv(naziv);
        promjene.setLatitude(lat);
        promjene.setLongitude(log);
        promjene.setStatus(status);
        promjene.setVrijemeKreiranja(new Date());
        promjene.setVrijemePromjene(new Date());
        promjeneFacade.create(promjene);
    }

    private void spremiUDnevnik(int trajanje) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAdresa = httpServletRequest.getHeader("X-FORWARDED-FOR");
        String urlAdresa = httpServletRequest.getRequestURI();
        if (ipAdresa == null) {
            ipAdresa = httpServletRequest.getRemoteAddr();
        }
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setKorisnik("ljakopov");
        dnevnik.setStatus(0);
        dnevnik.setIpadresa(ipAdresa);
        dnevnik.setUrl(urlAdresa);
        dnevnik.setTrajanje(trajanje);
        dnevnik.setVrijeme(new Date());
        dnevnikFacade.create(dnevnik);
    }

    public String dodajIoTUredaj() {
        if (!noviId.isEmpty() && !noviAdresa.isEmpty() && !noviNaziv.isEmpty()) {
            long pocetak = System.currentTimeMillis();
            Lokacija l = meteoIoTKlijent.dajLokaciju(noviAdresa);
            Uredaji uredaji = new Uredaji(Integer.parseInt(noviId), noviNaziv,
                    Float.parseFloat(l.getLatitude()),
                    Float.parseFloat(l.getLongitude()), 0, new Date(), new Date());
            uredajiFacade.create(uredaji);
            spremiUPromjene(Integer.parseInt(noviId), noviNaziv, Float.parseFloat(l.getLatitude()), Float.parseFloat(l.getLongitude()), 0);
            long kraj = System.currentTimeMillis();
            preuzmiRaspoloziveIoTUredaje();
            spremiUDnevnik((int) (kraj - pocetak));
        } else {

        }
        return "";
    }

    public void preuzmiRaspoloziveIoTUredaje() {
        long pocetak = System.currentTimeMillis();
        this.raspoloziviIoT.clear();
        List<Uredaji> raspolozivi = uredajiFacade.findAll();
        System.out.println("DOHVACANJE");
        for (Uredaji uredaji : raspolozivi) {
            System.out.println("PRIMEJR: " + uredaji.getNaziv());
            if (!this.popisRaspoloziviIoT.isEmpty()) {
                for (int i = 0; i < this.popisRaspoloziviIoT.size(); i++) {
                    if (uredaji.getId().toString().equals(this.popisRaspoloziviIoT.get(i))) {
                        System.out.println("ISPIS: " + popisRaspoloziviIoT);
                        this.odabraniIoT.add(new Izbornik(uredaji.getNaziv(),
                                uredaji.getId().toString()));
                    } else {
                        System.out.println("ISPIS: " + popisRaspoloziviIoT);
                        for (int j = 0; j < this.odabraniIoT.size(); j++) {
                            if (uredaji.getId().toString().equals(this.odabraniIoT.get(j).getVrijednost())) {
                            } else {
                                this.raspoloziviIoT.add(new Izbornik(uredaji.getNaziv(),
                                        uredaji.getId().toString()));
                            }
                        }
                    }
                }
            } else {
                System.out.println("ovo je proba");
                this.raspoloziviIoT.add(new Izbornik(uredaji.getNaziv(),
                        uredaji.getId().toString()));
            }
        }
        long kraj = System.currentTimeMillis();
        spremiUDnevnik((int) (kraj - pocetak));
        this.popisRaspoloziviIoT.clear();
    }

    public void azuriraj() {
        if (this.popisRaspoloziviIoT.size() == 1) {
            azuriranje = true;
            azurirajId = popisRaspoloziviIoT.get(0);
            uredjajZaAzuriranje = uredajiFacade.find(Integer.parseInt(azurirajId));
            azurirajNaziv = uredjajZaAzuriranje.getNaziv();
            azurirajAdresa = meteoIoTKlijent.dajMjesto(String.valueOf(uredjajZaAzuriranje.getLatitude()), String.valueOf(uredjajZaAzuriranje.getLongitude()));

        } else {
            System.out.println("PREVIŠE ARGUMENATA");
        }

    }

    public void spremmiAzuriranjeUBazu() {
        if (!azurirajId.isEmpty() && !azurirajAdresa.isEmpty() && !azurirajNaziv.isEmpty()) {
            long pocetak = System.currentTimeMillis();
            if (this.azurirajId.equals(uredjajZaAzuriranje.getId().toString())) {
                uredjajZaAzuriranje.setNaziv(azurirajNaziv);
                Lokacija l = meteoIoTKlijent.dajLokaciju(azurirajAdresa);

                uredjajZaAzuriranje.setLatitude(Float.parseFloat(l.getLatitude()));
                uredjajZaAzuriranje.setLongitude(Float.parseFloat(l.getLongitude()));
                uredajiFacade.edit(uredjajZaAzuriranje);
                spremiUPromjene(uredjajZaAzuriranje.getId(), azurirajNaziv, Float.parseFloat(l.getLatitude()), Float.parseFloat(l.getLongitude()), 0);
            } else {
                uredajiFacade.remove(uredjajZaAzuriranje);
                Lokacija l = meteoIoTKlijent.dajLokaciju(azurirajAdresa);
                Uredaji uredajiZaUnos = new Uredaji(Integer.parseInt(azurirajId), azurirajNaziv, Float.parseFloat(l.getLatitude()), Float.parseFloat(l.getLongitude()), 0, new Date(), new Date());
                uredajiFacade.create(uredajiZaUnos);
                preuzmiRaspoloziveIoTUredaje();

            }
            azuriranje = false;
            this.popisRaspoloziviIoT.clear();
            preuzmiRaspoloziveIoTUredaje();
            long kraj = System.currentTimeMillis();
            spremiUDnevnik((int) (kraj - pocetak));
        }
    }

    public void dohvatiPrognoze() {
        if (prognoze == false) {
            long pocetak = System.currentTimeMillis();
            prognoze = true;
            gumbPregledPrognoza = "Zatvori prognoze";
            String adresa;
            Uredaji uredjajaZaPrognozu;
            for (int i = 0; i < odabraniIoT.size(); i++) {
                uredjajaZaPrognozu = uredajiFacade.find(Integer.parseInt(odabraniIoT.get(i).getVrijednost()));
                adresa = meteoIoTKlijent.dajMjesto(String.valueOf(uredjajaZaPrognozu.getLatitude()), String.valueOf(uredjajaZaPrognozu.getLongitude()));
                lis = meteoIoTKlijent.dajMeteoPrognoze(Integer.parseInt(odabraniIoT.get(i).getVrijednost()), adresa);
                meteoPrognoze.addAll(Arrays.asList(lis));
            }
            long kraj = System.currentTimeMillis();
            spremiUDnevnik((int) (kraj - pocetak));
        } else {
            prognoze = false;
            gumbPregledPrognoza = "Prognoze";
        }
    }
}
