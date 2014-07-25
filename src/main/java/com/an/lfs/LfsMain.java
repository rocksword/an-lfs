package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.PropertyConfigurator;

public class LfsMain {
    // Arguments
    private static void init() throws FileNotFoundException {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        System.out.println("filepath: " + filepath);
        File f = new File(filepath);
        PropertyConfigurator.configure(new FileInputStream(f));
    }

    private static int BEGIN_YEAR = 2009;
    private static String COUNTRY = LfsUtil.JPN;

    public static void main(String[] args) {
        try {
            init();
            for (int year = BEGIN_YEAR; year < 2015; year++) {
                ReportMaker maker = new ReportMaker(COUNTRY, year);
                maker.analyzeMatch();
                maker.analyzeRate();
                maker.exportStatis();
                maker.exportSummary();
                // maker.generateRateFiles();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
