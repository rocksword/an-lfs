package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

public class LfsMain {
    private static final Log logger = LogFactory.getLog(LfsMain.class);
    // Arguments
    public static final int year = 2013;

    private static Analyzer analyzer = new SimpleAnalyzer();

    public static void main(String[] args) {
        init();
        try {
            analyzer.analyze(LfsConst.FRA, year);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void init() {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        File f = new File(filepath);
        try {
            PropertyConfigurator.configure(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            logger.error("Error: " + e);
        }
    }
}
