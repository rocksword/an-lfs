/**
 * 
 */
package com.an.lfs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.an.lfs.db.SussDao;
import com.an.lfs.service.BoardService;

/**
 * @author Anthony
 * 
 */
public class AppContextFactory {
    private static volatile boolean initialized = false;
    private static ApplicationContext appContext;

    public AppContextFactory() {
    }

    public synchronized static void init() {
        if (!initialized) {
            synchronized (AppContextFactory.class) {
                if (!initialized) {
                    appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
                    initialized = true;
                }
            }
        }
    }

    public static SussDao getDao() {
        return (SussDao) appContext.getBean("sussDaoImpl");
    }

    public static BoardService getBoardService() {
        return (BoardService) appContext.getBean("boardServiceImpl");
    }
}
