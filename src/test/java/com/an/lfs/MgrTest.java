package com.an.lfs;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class MgrTest {

    @Test
    public void testCompMgr() {
        Map<String, String> companyMap = CompanyMgr.getCompanyMap();
    }

    @Test
    public void testTeamMgr() {
        Map<String, Map<String, String>> ctyTeamMap = TeamMgr.getCtyTeamMap();
        for (String cty : ctyTeamMap.keySet()) {
        }
    }

    @Test
    public void testLfsConfMgr() {
        Assert.assertTrue(LfsConfMgr.contains(LfsConst.ENG, "LiBo"));
        Assert.assertFalse(LfsConfMgr.contains(LfsConst.ENG, "Oddset"));
        Assert.assertTrue(LfsConfMgr.contains(LfsConst.GER, "Oddset"));
    }
}
