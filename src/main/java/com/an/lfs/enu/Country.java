package com.an.lfs.enu;

import java.util.HashMap;
import java.util.Map;

import com.an.lfs.LfsUtil;

public enum Country {
    INVALID("INVALID"), //
    ARG(LfsUtil.ARG), //
    BRA(LfsUtil.BRA), //
    ENG(LfsUtil.ENG), //
    ENG_B(LfsUtil.ENG_B), //
    ENG_C(LfsUtil.ENG_C), //
    ESP(LfsUtil.ESP), //
    FRA(LfsUtil.FRA), //
    FRA_B(LfsUtil.FRA_B), //
    GER(LfsUtil.GER), //
    GER_B(LfsUtil.GER_B), //
    ITA(LfsUtil.ITA), //
    JPN(LfsUtil.JPN), //
    JPN_B(LfsUtil.JPN_B), //
    NOR(LfsUtil.NOR), //
    NED(LfsUtil.NED), //
    NED_B(LfsUtil.NED_B), //
    SWE(LfsUtil.SWE), //
    USA(LfsUtil.USA), //
    CL(LfsUtil.CL), //
    EL(LfsUtil.EL);

    public static Map<String, Country> countryMap = new HashMap<>();
    static {
        countryMap.put(LfsUtil.ARG, ARG);
        countryMap.put(LfsUtil.BRA, BRA);
        countryMap.put(LfsUtil.ENG, ENG);
        countryMap.put(LfsUtil.ENG_B, ENG_B);
        countryMap.put(LfsUtil.ENG_C, ENG_C);
        countryMap.put(LfsUtil.ESP, ESP);
        countryMap.put(LfsUtil.FRA, FRA);
        countryMap.put(LfsUtil.FRA_B, FRA_B);
        countryMap.put(LfsUtil.GER, GER);
        countryMap.put(LfsUtil.GER_B, GER_B);
        countryMap.put(LfsUtil.ITA, ITA);
        countryMap.put(LfsUtil.JPN, JPN);
        countryMap.put(LfsUtil.JPN_B, JPN_B);
        countryMap.put(LfsUtil.NED, NED);
        countryMap.put(LfsUtil.NED_B, NED_B);
        countryMap.put(LfsUtil.NOR, NOR);
        countryMap.put(LfsUtil.SWE, SWE);
        countryMap.put(LfsUtil.USA, USA);
        countryMap.put(LfsUtil.CL, CL);
        countryMap.put(LfsUtil.EL, EL);
    }

    public static Country getCountry(String val) {
        if (countryMap.containsKey(val)) {
            return countryMap.get(val);
        }
        return INVALID;
    }

    private Country(String val) {
        this.val = val;
    }

    private String val;

    public String getVal() {
        return val;
    }
}
