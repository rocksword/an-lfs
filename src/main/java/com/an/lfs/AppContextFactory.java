package com.an.lfs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContextFactory {
    private static volatile boolean initialized = false;
    private static ApplicationContext appContext;

    public AppContextFactory() {
    }

    public synchronized static void init() {
        LfsUtil.getLfsHome();
        if (!initialized) {
            synchronized (AppContextFactory.class) {
                if (!initialized) {
                    appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
                    initialized = true;
                }
            }
        }
    }

    public static LfsDao getDao() {
        LfsDao result = (LfsDao) appContext.getBean("lfsDao");
        return result;
    }
}
