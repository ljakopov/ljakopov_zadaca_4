/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.ljakopov.konfiguracije.Konfiguracija;
import org.foi.nwtis.ljakopov.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ljakopov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.ljakopov.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.ljakopov.web.zrna.OdabirIoTPrognoza;

/**
 * Web application lifecycle listener.
 *
 * @author Lovro
 */
public class SlusacAplikacije implements ServletContextListener {

    private ServletContext context = null;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        String datoteka = context.getRealPath("/WEB-INF")
                + File.separator
                + context.getInitParameter("konfiguracija");
        
        OdabirIoTPrognoza.datoteka = datoteka;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
