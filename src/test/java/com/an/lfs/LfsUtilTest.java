package com.an.lfs;

import org.junit.Test;

public class LfsUtilTest {
    @Test
    public void test() {
        String dirName = LfsUtil.getMatchDirName(LfsConst.GER, 2013);
        System.out.println(LfsUtil.getInputFilePath(dirName, LfsConst.MATCH_FILE));
    }
}
