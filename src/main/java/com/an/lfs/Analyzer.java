package com.an.lfs;

import java.io.IOException;

public interface Analyzer {
    public void exportReport(String country, int year) throws IOException;

    public void generateRateFiles(String country, int year) throws IOException;
}
