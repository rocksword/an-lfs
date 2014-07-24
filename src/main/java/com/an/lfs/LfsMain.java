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

    public static void main(String[] args) {
        try {
            init();
            // new CompoundAnalyzer().generateRateFiles(LfsConst.USA, 2014);
            new SimpleAnalyzer().exportReport(LfsConst.USA, 2014);
            new CompoundAnalyzer().exportReport(LfsConst.USA, 2014);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
