package com.an.lfs;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.an.lfs.enu.Country;
import com.an.lfs.vo.CompanyMgr;
import com.an.lfs.vo.LfsConfMgr;
import com.an.lfs.vo.TeamMgr;

public class MgrTest {

    @Test
    public void testCompMgr() {
        Map<String, String> companyMap = CompanyMgr.getCompanyMap();
        for (String company : companyMap.keySet()) {
            // System.out.println(company + " -> " + companyMap.get(company));
        }
    }

    @Test
    public void testTeamMgr() {
        Map<Country, Map<String, String>> ctyTeamMap = TeamMgr.getCtyTeamMap();
        for (Country cty : ctyTeamMap.keySet()) {
            Map<String, String> teamMap = ctyTeamMap.get(cty);
            for (String team : teamMap.keySet()) {
                // System.out.println(cty.getVal() + " -> " + team + " -> " + teamMap.get(team));
            }
        }
    }

    @Test
    public void testLfsConfMgr() {
        Assert.assertTrue(LfsConfMgr.contains(Country.ENG, "LiBo"));
        Assert.assertFalse(LfsConfMgr.contains(Country.ENG, "Oddset"));
        Assert.assertTrue(LfsConfMgr.contains(Country.GER, "Oddset"));
    }
}
