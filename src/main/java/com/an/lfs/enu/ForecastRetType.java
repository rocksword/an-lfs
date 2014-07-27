package com.an.lfs.enu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.an.lfs.LfsUtil;

public enum ForecastRetType {
    NULL(LfsUtil.NULL), //
    W_W(LfsUtil.W_W), //
    W_D(LfsUtil.W_D), //
    W_L(LfsUtil.W_L), //
    L_W(LfsUtil.L_W), //
    L_D(LfsUtil.L_D), //
    L_L(LfsUtil.L_L); //

    public static ForecastRetType[] allFcRetTypes = new ForecastRetType[] { W_W, W_D, W_L, L_W, L_D, L_L };

    public static ForecastRetType[] allWinFcRetTypes = new ForecastRetType[] { W_W, W_D, W_L };
    public static Set<ForecastRetType> allWinFcRetSet = new HashSet<>();

    public static ForecastRetType[] allLoseFcRetTypes = new ForecastRetType[] { L_W, L_D, L_L };
    public static Set<ForecastRetType> allLoseFcRetSet = new HashSet<>();

    private static Map<String, ForecastRetType> values = new HashMap<>();
    static {
        values.put(LfsUtil.W_W, W_W);
        values.put(LfsUtil.W_D, W_D);
        values.put(LfsUtil.W_L, W_L);
        values.put(LfsUtil.L_W, L_W);
        values.put(LfsUtil.L_D, L_D);
        values.put(LfsUtil.L_L, L_L);

        for (ForecastRetType ft : allWinFcRetTypes) {
            allWinFcRetSet.add(ft);
        }

        for (ForecastRetType ft : allLoseFcRetTypes) {
            allLoseFcRetSet.add(ft);
        }
    }

    public static ForecastRetType getForecastResultType(String val) {
        if (values.containsKey(val)) {
            return values.get(val);
        }
        return ForecastRetType.NULL;
    }

    private String val;

    public String getVal() {
        return val;
    }

    private ForecastRetType(String val) {
        this.val = val;
    }
}
