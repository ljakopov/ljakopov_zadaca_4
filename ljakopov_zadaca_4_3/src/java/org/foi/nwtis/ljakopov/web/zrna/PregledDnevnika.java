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
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.foi.nwtis.ljakopov.ejb.eb.Dnevnik;
import org.foi.nwtis.ljakopov.ejb.sb.DnevnikFacade;

/**
 *
 * @author ljakopov
 */
@Named(value = "pregledDnevnika")
@SessionScoped
public class PregledDnevnika implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    private String korisnik;
    private String url;
    private String ipAdresa;
    private Date vrijeme_od;
    private Date vrijeme_do;
    private String trajanje;
    private String status;
    private List<Dnevnik> dnevnik = new ArrayList<>();
    int atributi = 0;
    private boolean prikazDnevnika = false;

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    public Date getVrijeme_od() {
        return vrijeme_od;
    }

    public void setVrijeme_od(Date vrijeme_od) {
        this.vrijeme_od = vrijeme_od;
    }

    public Date getVrijeme_do() {
        return vrijeme_do;
    }

    public void setVrijeme_do(Date vrijeme_do) {
        this.vrijeme_do = vrijeme_do;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Dnevnik> getDnevnik() {
        return dnevnik;
    }

    public void setDnevnik(List<Dnevnik> dnevnik) {
        this.dnevnik = dnevnik;
    }

    public boolean isPrikazDnevnika() {
        return prikazDnevnika;
    }

    public void setPrikazDnevnika(boolean prikazDnevnika) {
        this.prikazDnevnika = prikazDnevnika;
    }

    public void preuzimanjePodataka() {
        prikazDnevnika = true;
        atributi = 0;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("zadaca_4_1PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Dnevnik.class);
        Root<Dnevnik> podaciDevnik = criteriaQuery.from(Dnevnik.class);
        Query query;

        Expression<String> expressionTrajanje = podaciDevnik.get("trajanje");
        Expression<String> expressionIPadresa = podaciDevnik.get("ipadresa");
        Expression<Date> expressionVrijeme = podaciDevnik.get("vrijeme");
        Expression<String> expressionStatus = podaciDevnik.get("status");

        if (vrijeme_do != null && vrijeme_od != null) {
            atributi++;
        }
        if (!ipAdresa.isEmpty()) {
            atributi++;
        }
        if (!status.isEmpty()) {
            atributi++;
        }
        if (!trajanje.isEmpty()) {
            atributi++;
        }
        System.out.println("ATRIBUTI: " + atributi);
        if (atributi == 1) {
            if (vrijeme_do != null && vrijeme_od != null) {
                criteriaQuery.where(criteriaBuilder.between(expressionVrijeme, vrijeme_od, vrijeme_do));
            }
            if (!ipAdresa.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.equal(expressionIPadresa, ipAdresa));
            }
            if (!status.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.equal(expressionStatus, status));
            }
            if (!trajanje.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(expressionTrajanje, trajanje));
            }
        }

        if (atributi == 2) {
            if ((vrijeme_do != null && vrijeme_od != null) && !ipAdresa.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.between(expressionVrijeme, vrijeme_od, vrijeme_do),
                        criteriaBuilder.equal(expressionIPadresa, ipAdresa)));
            }
            if ((vrijeme_do != null && vrijeme_od != null) && !trajanje.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.between(expressionVrijeme, vrijeme_od, vrijeme_do),
                        criteriaBuilder.greaterThanOrEqualTo(expressionTrajanje, trajanje)));
            }
            if ((vrijeme_do != null && vrijeme_od != null) && !status.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.between(expressionVrijeme, vrijeme_od, vrijeme_do),
                        criteriaBuilder.equal(expressionStatus, status)));
            }
            if (!ipAdresa.isEmpty() && !status.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(expressionIPadresa, ipAdresa),
                        criteriaBuilder.equal(expressionStatus, status)));
            }
            if (!ipAdresa.isEmpty() && !trajanje.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(expressionIPadresa, ipAdresa),
                        criteriaBuilder.greaterThanOrEqualTo(expressionTrajanje, trajanje)));
            }
            if (!trajanje.isEmpty() && !status.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(expressionTrajanje, trajanje),
                        criteriaBuilder.equal(expressionStatus, status)));
            }
        }

        if (atributi == 3) {
            if (status.isEmpty()) {
                criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.between(expressionVrijeme, vrijeme_od, vrijeme_do),
                        criteriaBuilder.greaterThanOrEqualTo(expressionTrajanje, trajanje), criteriaBuilder.equal(expressionIPadresa, ipAdresa)));
            } else {
                criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(expressionStatus, status),
                        criteriaBuilder.greaterThanOrEqualTo(expressionTrajanje, trajanje), criteriaBuilder.equal(expressionIPadresa, ipAdresa)));
            }
        }
        if (atributi == 4) {
            criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.between(expressionVrijeme, vrijeme_od, vrijeme_do),
                    criteriaBuilder.greaterThanOrEqualTo(expressionTrajanje, trajanje), criteriaBuilder.equal(expressionIPadresa, ipAdresa),
                    criteriaBuilder.equal(expressionStatus, status)));
        }

        query = entityManager.createQuery(criteriaQuery);
        dnevnik = query.getResultList();
    }
}
