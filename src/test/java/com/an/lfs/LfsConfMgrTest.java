package com.an.lfs;

import org.junit.Test;

public class LfsConfMgrTest {
    @Test
    public void test() {
        AppContextFactory.init();
        System.out.println(AppContextFactory.getConfMgr().getConf());
    }
}
