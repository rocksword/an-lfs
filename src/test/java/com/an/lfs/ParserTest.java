package com.an.lfs;

import org.junit.Test;

import com.an.lfs.vo.ClaimRateSummary;

public class ParserTest {
    @Test
    public void testClaimRateParser() {
        String key = "2013_01_Ao_Duo";
        String dirName = LfsUtil.getMatchDirName(LfsConst.GER, 2013);
        ClaimRateSummary sum = ClaimRateParser.parse(dirName, key);
        System.out.println(sum);
    }
}
