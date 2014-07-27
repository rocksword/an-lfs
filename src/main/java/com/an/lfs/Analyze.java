package com.an.lfs;

public interface Analyze {
    public boolean analyzeMatch();

    public void analyzeRate();

    public void generateRateFiles();

    public void exportExcel() throws Exception;
}
