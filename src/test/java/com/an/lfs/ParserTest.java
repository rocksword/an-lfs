package com.an.lfs;

import com.an.lfs.vo.ClaimRateSummary;

public class ParserTest {
    public void testClaimRateParser() {
        String key = "2013_01_Ao_Duo";
        ClaimRateParser parser = new ClaimRateParser();
        ClaimRateSummary sum = parser.parse(key);
        System.out.println(sum);
    }
}
