package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class LfsMain {
    private static final Log logger = LogFactory.getLog(LfsMain.class);

    public static void main(String[] args) {
        LfsMain app = new LfsMain();

        try {
            init();
            app.startApp();
        } catch (Exception e) {
            logger.error("Error: " + e);
            System.exit(1);
        }

    }

    private void startApp() {
        logger.info("Starting application.");
        InputParser parser = new InputParser();
        List<MatchItem> items = new ArrayList<>();
        parser.parse("2013", "2014", items);
        LfsUtil.logCollection(items);
    }

    private static void init() {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        File f = new File(filepath);
        try {
            PropertyConfigurator.configure(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            logger.error("Error: " + e);
        }

        AppContextFactory.init();
    }
}