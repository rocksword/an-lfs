package com.an.lfs;

import org.junit.Test;

public class LfsUtilTest {
    @Test
    public void test() {
        String inputFile = "2013.txt";
        System.out.println(LfsUtil.getInputFilePath(inputFile));
    }
}
