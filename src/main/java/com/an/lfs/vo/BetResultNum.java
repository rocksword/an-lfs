package com.an.lfs.vo;

import com.an.lfs.enu.RateBet;

public class BetResultNum {
    private int passNum;
    private int failNum;

    public void addBetResult(RateBet betRet) {
        if (betRet.isPass()) {
            passNum++;
        } else if (betRet.isFail()) {
            failNum++;
        }
    }

    public BetResultNum() {
    }

    public int getPassNum() {
        return passNum;
    }

    public String getPassPer() {
        String ret = (int) (100 * (float) passNum / (float) (passNum + failNum)) + "%";
        return ret;
    }

    public String getFailPer() {
        String ret = (int) (100 * (float) failNum / (float) (passNum + failNum)) + "%";
        return ret;
    }

    public int getFailNum() {
        return failNum;
    }
}
