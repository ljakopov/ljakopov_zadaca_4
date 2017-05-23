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
import org.foi.nwtis.ljakopov.ejb.eb.Promjene;
import org.foi.nwtis.ljakopov.ejb.sb.PromjeneFacade;

/**
 *
 * @author ljakopov
 */
@Named(value = "pregledPromjena")
@SessionScoped
public class PregledPromjena implements Serializable {

    @EJB
    private PromjeneFacade promjeneFacade;

    private String id;
    private String naziv;
    private List<Promjene> promjena = new ArrayList<>();
    private boolean prikazPromjena = false;

    /**
     * Creates a new instance of PregledPromjena
     */
    public PregledPromjena() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public List<Promjene> getPromjena() {
        return promjena;
    }

    public void setPromjena(List<Promjene> promjena) {
        this.promjena = promjena;
    }

    public boolean isPrikazPromjena() {
        return prikazPromjena;
    }

    public void setPrikazPromjena(boolean prikazPromjena) {
        this.prikazPromjena = prikazPromjena;
    }
    

    public void preuzmiPodatkePromjena() {
        prikazPromjena = true;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("zadaca_4_1PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Promjene.class);
        Root<Dnevnik> podaciDevnik = criteriaQuery.from(Promjene.class);
        Query query;

        Expression<String> expressionId = podaciDevnik.get("id");
        Expression<String> expressionNaziv = podaciDevnik.get("naziv");

        if (!id.isEmpty() && !naziv.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(expressionId, id),
                    criteriaBuilder.equal(expressionNaziv, naziv)));
        } else if (!id.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.equal(expressionId, id));
        } else if (!naziv.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.equal(expressionNaziv, naziv));
        } else {
        }

        query = entityManager.createQuery(criteriaQuery);
        promjena = query.getResultList();
    }

}
