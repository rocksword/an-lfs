package com.an.lfs;

import org.junit.Test;

import com.an.lfs.vo.ClaimRateSummary;

public class ParserTest {
    @Test
    public void testClaimRateParser() {
        String key = "2013_01_Ao_Duo";
        ClaimRateSummary sum = ClaimRateParser.parse(LfsMain.ARGUMENT, key);
        System.out.println(sum);
    }
}
