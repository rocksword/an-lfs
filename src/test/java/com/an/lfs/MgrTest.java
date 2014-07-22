package com.an.lfs;

import org.junit.Test;

public class MgrTest {
    @Test
    public void test() {
        AppContextFactory.init();
    }

    @Test
    public void testCompMgr() {
        CompanyMgr.getName("test");
    }

    @Test
    public void testTeamMgr() {
        TeamMgr.getName("test");
    }
}
