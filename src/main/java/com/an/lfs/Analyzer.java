package com.an.lfs;

import java.io.IOException;

public interface Analyzer {
    public void analyze(String country, int year) throws IOException;
}
