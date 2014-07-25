package com.an.lfs;

import org.junit.Test;

public class LfsUtilTest {
    @Test
    public void test() {
        System.out.println(LfsUtil.getInputFilePath(LfsUtil.GER, 2013, LfsUtil.MATCH_FILE));
    }
}
