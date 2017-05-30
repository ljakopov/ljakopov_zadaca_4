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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.foi.nwtis.ljakopov.konfiguracije.Konfiguracija;
import org.foi.nwtis.ljakopov.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ljakopov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.ljakopov.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.ljakopov.web.kontrole.Izbornik;
import org.foi.nwtis.ljakopov.web.podaci.Lokacija;
import org.foi.nwtis.ljakopov.web.podaci.MeteoPrognoza;

/**
 *
 * @author ljakopov. Klasa služi za prikaz, rukovanje i spremanje podataka u
 * bazu podataka
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

    /**
     * varijable za unos novog uređaja
     */
    private String noviId;
    private String noviNaziv;
    private String noviAdresa;
    /**
     * liste za baratanje uređajima
     */
    private List<Izbornik> raspoloziviIoT = new ArrayList<>();
    private List<Izbornik> odabraniIoT = new ArrayList<>();
    private List<String> popisRaspoloziviIoT = new ArrayList<>();
    private List<String> popisOdabraniIoT = new ArrayList<>();
    /**
     * varijable za ažuriranje uređaja
     */
    private String azurirajId;
    private String azurirajNaziv;
    private String azurirajAdresa;
    /**
     * lista za ispis prognoza
     */
    private List<MeteoPrognoza> meteoPrognoze = new ArrayList<>();
    /**
     * boolean varijable za prikaz kroz ajax
     */
    private boolean azuriranje;
    private boolean prognoze = false;
    private boolean prvi = true;
    /**
     * varijabla za pamćenje uređaja kod ažuriranja
     */
    Uredaji uredjajZaAzuriranje;
    /**
     * varijabla za gumb kod pregleda prognoza
     */
    private String gumbPregledPrognoza = "Pregled prognoza";
    private MeteoPrognoza[] lis;
    boolean vrati = false;
    /**
     * varijabla za ispis greške
     */
    private String greska;
    /**
     * varijabla za dohvaćanje konfiguracijske datoteke
     */
    public static String datoteka;

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

    public String getGreska() {
        return greska;
    }

    public void setGreska(String greska) {
        this.greska = greska;
    }

    /**
     * metoda spremiIUPromjene se poziva kod spremanja podataka u tablicu
     * promjena. Koristi id,naziv, lat, log i status. Na početku kreira se
     * objekt promjene te se kasnije taj objekt sprema sa svim podacima
     */
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

    /**
     * metoda spremiIUDnevnik se poziva kod spremanja podataka u tablicu
     * dnevnik. Koristi integer trajanje. Na početku kreira se objekt dnevnik te
     * se kasnije taj objekt sprema sa svim podacima u tablicu dnevnik. Metoda
     * se poziva nakon obavljanja neke ispravne radnje.
     */
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

    /**
     * metoda isInteger provjerava da li je upisani ID integer. Ako se radi o
     * integeru vraća true, a ako nije vraća false.
     *
     * @param s
     * @return boolean
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * metoda dodajToTUredjaj sprema novouneseni uređaj u bazu podataka. Prvo se
     * provjerava da li su sve varijable unesene. Nakon toga da li je ID
     * integer. Ako je on upisan, da li se ID već nalazi u bazi podataka. U
     * slučaju da se ne nalazi, sprema se novi uređaj u bazu podataka. Radnja se
     * bilježi u tablicu dnevnik.
     */
    public String dodajIoTUredaj() {
        if (!noviId.isEmpty() && !noviAdresa.isEmpty() && !noviNaziv.isEmpty()) {
            if (isInteger(noviId) == false) {
                System.out.println("NIJE INTEGER");
                greska = "ID treba biti broj";
            } else if (uredajiFacade.find(Integer.parseInt(noviId)) != null) {
                System.out.println("ID POSTOJI");
                greska = "ID već postoji";
            } else {
                greska = "";
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
            }
        } else {
            greska = "Niste upisali sve podatke";
        }
        return "";
    }

    /**
     * metoda preuzmi preuzmiRaspoloziveIoTUredaje() uzima sve uređaje iz baze
     * podataka i provjerava gdje se nalaze. Tj. da li pripadaju listi
     * raspoloživih uređaja ili listi odabranih uređaja
     */
    public void preuzmiRaspoloziveIoTUredaje() {
        long pocetak = System.currentTimeMillis();
        boolean prvi = false;
        boolean odabrani = false;
        this.raspoloziviIoT.clear();
        List<Uredaji> raspolozivi = uredajiFacade.findAll();
        for (Uredaji uredaji : raspolozivi) {
            if (popisRaspoloziviIoT.isEmpty() && odabraniIoT.isEmpty()) {
                this.raspoloziviIoT.add(new Izbornik(uredaji.getNaziv(),
                        uredaji.getId().toString()));
            }
            for (int i = 0; i < popisRaspoloziviIoT.size(); i++) {
                if (popisRaspoloziviIoT.get(i).equals(uredaji.getId().toString())) {
                    prvi = true;
                }
            }
            for (int j = 0; j < odabraniIoT.size(); j++) {
                if (odabraniIoT.get(j).getVrijednost().equals(uredaji.getId().toString())) {
                    odabrani = true;
                }
            }
            if (!popisRaspoloziviIoT.isEmpty() || !odabraniIoT.isEmpty()) {
                if (prvi == false && odabrani == true) {
                    odabrani = false;
                } else if (prvi && odabrani == false) {
                    odabraniIoT.add(new Izbornik(uredaji.getNaziv(),
                            uredaji.getId().toString()));
                    prvi = false;
                } else {
                    this.raspoloziviIoT.add(new Izbornik(uredaji.getNaziv(),
                            uredaji.getId().toString()));
                }
            }
        }
        long kraj = System.currentTimeMillis();
        spremiUDnevnik((int) (kraj - pocetak));
        this.popisRaspoloziviIoT.clear();
    }

    /**
     * metoda vratiDodaneUredjaje() vraća uređaje iz liste odabranih uređaja u
     * listu raspoloživih uređaja
     */
    public void vratiDodaneUredjaje() {
        popisRaspoloziviIoT.clear();
        for (Iterator<Izbornik> iterator = odabraniIoT.iterator(); iterator.hasNext();) {
            Izbornik izbornik = iterator.next();
            for (String id : popisOdabraniIoT) {
                if (izbornik.getVrijednost().equals(id)) {
                    iterator.remove();
                }
            }
        }
        preuzmiRaspoloziveIoTUredaje();
    }

    /**
     * metoda azuriraj() uzima JEDAN uređaj iz liste raspoloživih uređaja,
     * pronalazi ga u bazi podataka, traži njegove lat i log i ispisuje njegove
     * podatke, zajedno sa adresom na korisničko sučelje. Ako je uzeto više
     * uređaja ispisuje se greška
     */
    public void azuriraj() {
        if (this.popisRaspoloziviIoT.size() == 1) {
            azuriranje = true;
            greska = "";
            azurirajId = popisRaspoloziviIoT.get(0);
            uredjajZaAzuriranje = uredajiFacade.find(Integer.parseInt(azurirajId));
            azurirajNaziv = uredjajZaAzuriranje.getNaziv();
            azurirajAdresa = meteoIoTKlijent.dajMjesto(String.valueOf(uredjajZaAzuriranje.getLatitude()), String.valueOf(uredjajZaAzuriranje.getLongitude()));

        } else {
            greska = "Uzeli ste previše argumenata";
            System.out.println("PREVIŠE ARGUMENATA");
        }

    }

    /**
     * metoda spremmiAzuriranjeUBazu() prvo provjerava da li su upisani svi
     * argumenti. Zatim provjerva da li je upisani ID jednak prijašnjem. Ako je
     * jednak on ažurira uređaj i sve sprema u tablicu projema i dnevnik. Ako
     * nije jednak, briše prijašnjeg uređaja, sprema novog i opet sve upisuje u
     * tablicu promjea i dnevnik. Ako se dogodila neka greška vraća određeni
     * ispis.
     */
    public void spremmiAzuriranjeUBazu() {
        if (!azurirajId.isEmpty() && !azurirajAdresa.isEmpty() && !azurirajNaziv.isEmpty()) {
            if (isInteger(azurirajId) == true) {
                greska = "";
                long pocetak = System.currentTimeMillis();
                if (this.azurirajId.equals(uredjajZaAzuriranje.getId().toString())) {
                    uredjajZaAzuriranje.setNaziv(azurirajNaziv);
                    Lokacija l = meteoIoTKlijent.dajLokaciju(azurirajAdresa);

                    uredjajZaAzuriranje.setLatitude(Float.parseFloat(l.getLatitude()));
                    uredjajZaAzuriranje.setLongitude(Float.parseFloat(l.getLongitude()));
                    uredajiFacade.edit(uredjajZaAzuriranje);
                    spremiUPromjene(uredjajZaAzuriranje.getId(), azurirajNaziv, Float.parseFloat(l.getLatitude()), Float.parseFloat(l.getLongitude()), 0);
                    azuriranje = false;
                    this.popisRaspoloziviIoT.clear();
                    preuzmiRaspoloziveIoTUredaje();
                    long kraj = System.currentTimeMillis();
                    spremiUDnevnik((int) (kraj - pocetak));
                } else {
                    if (uredajiFacade.find(Integer.parseInt(azurirajId)) != null) {
                        System.out.println("ID POSTOJI");
                        greska = "ID već postoji";
                    } else {
                        uredajiFacade.remove(uredjajZaAzuriranje);
                        Lokacija l = meteoIoTKlijent.dajLokaciju(azurirajAdresa);
                        Uredaji uredajiZaUnos = new Uredaji(Integer.parseInt(azurirajId), azurirajNaziv, Float.parseFloat(l.getLatitude()), Float.parseFloat(l.getLongitude()), 0, new Date(), new Date());
                        uredajiFacade.create(uredajiZaUnos);
                        spremiUPromjene(Integer.parseInt(azurirajId), azurirajNaziv, Float.parseFloat(l.getLatitude()), Float.parseFloat(l.getLongitude()), 0);
                        azuriranje = false;
                        this.popisRaspoloziviIoT.clear();
                        preuzmiRaspoloziveIoTUredaje();
                        long kraj = System.currentTimeMillis();
                        spremiUDnevnik((int) (kraj - pocetak));
                    }
                }
            } else {
                greska = "ID treba biti broj";
                System.out.println("NISTE UPISAli INTEGERA");
            }
        } else {
            greska = "Potrebno je upisati sve podatke";
        }
    }

    /**
     * metoda dohvatiPrognoze() uzima konfiguracijsku datoteku i apikey, šalje
     * ga meteoIoTKlijentu. Uzima sve odabrane uređaje i preko ostalih metoda
     * dobiva prognoze kroz sljedeći 5 dana za uređaje.
     */
    public void dohvatiPrognoze() {
        Konfiguracija konf = null;
        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(OdabirIoTPrognoza.class.getName()).log(Level.SEVERE, null, ex);
        }
        String apikey = konf.dajPostavku("apikey");
        System.out.println("API KEY  " + apikey);
        meteoIoTKlijent.postaviKorisnickePodatke(apikey);
        if (prognoze == false) {
            meteoPrognoze.clear();
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
