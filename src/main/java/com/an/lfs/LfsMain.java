package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

        MatchParser mParser = new MatchParser();
        mParser.parse("2013", LfsUtil.matchItems);
        logger.info(LfsUtil.matchItems);

        CompanyParser cParser = new CompanyParser();
        cParser.parse(LfsUtil.companys);

        ClaimRateParser crParser = new ClaimRateParser();
        crParser.parse(LfsUtil.claimRates);

        LfsUtil.createClaimRateFiles();
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
