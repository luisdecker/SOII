package br.com.soserver.comm;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by fernando on 14/10/16.
 */
public class HookManager implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Booting system..");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Closing system..");
        PortManager.destroyInstance();
    }
}
