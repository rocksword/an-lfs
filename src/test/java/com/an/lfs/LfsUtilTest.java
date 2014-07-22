package com.an.lfs;

import org.junit.Test;

import com.an.lfs.vo.Country;

public class LfsUtilTest {
    @Test
    public void test() {
        String inputFile = "2013.txt";
        String dirName = LfsUtil.getMatchDirName(Country.ENG, 2013);
        System.out.println(LfsUtil.getInputFilePath(dirName, inputFile));
    }
}
