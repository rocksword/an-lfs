package com.an.lfs;

import java.io.IOException;

public interface Analyze {
    public boolean analyzeMatch();

    public void analyzeRate();

    public void generateRateFiles();

    public void exportSummary() throws IOException;

    public void exportStatis() throws IOException;
}
