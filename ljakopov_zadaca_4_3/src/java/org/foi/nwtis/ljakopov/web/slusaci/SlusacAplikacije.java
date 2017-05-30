/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.slusaci;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.ljakopov.web.zrna.OdabirIoTPrognoza;

/**
 * Web application lifecycle listener.
 *
 * @author ljakopov. Klasa služi kao slušač apliakcije, predaje putanju datoteke
 * konfiguracije
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
