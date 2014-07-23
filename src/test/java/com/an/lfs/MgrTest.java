package com.an.lfs;

import org.junit.Test;

public class MgrTest {

    @Test
    public void testCompMgr() {
        System.out.println(CompanyMgr.getName("test"));
    }

    @Test
    public void testTeamMgr() {
        System.out.println(TeamMgr.getName(LfsConst.GER, "test"));
    }
}
