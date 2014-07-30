package com.an.lfs.enu;

import java.util.HashMap;
import java.util.Map;

import com.an.lfs.LfsUtil;

public enum Country {
    INVALID("INVALID"), //
    BRA(LfsUtil.BRA), //
    ENG(LfsUtil.ENG), //
    ESP(LfsUtil.ESP), //
    FRA(LfsUtil.FRA), //
    GER(LfsUtil.GER), //
    ITA(LfsUtil.ITA), //
    JPN(LfsUtil.JPN), //
    JPN_B(LfsUtil.JPN_B), //
    NOR(LfsUtil.NOR), //
    KOR(LfsUtil.KOR), //
    SWE(LfsUtil.SWE), //
    USA(LfsUtil.USA), //
    CL(LfsUtil.CL), //
    EL(LfsUtil.EL);

    public static Country[] allCountries = new Country[] { BRA, ENG, ESP, FRA, GER, ITA, JPN, JPN_B, NOR, KOR, SWE, USA };

    public static Country[] leagueCountries = new Country[] { CL, EL };

    public static Map<String, Country> countryMap = new HashMap<>();
    static {
        countryMap.put(LfsUtil.BRA, BRA);
        countryMap.put(LfsUtil.ENG, ENG);
        countryMap.put(LfsUtil.ESP, ESP);
        countryMap.put(LfsUtil.FRA, FRA);
        countryMap.put(LfsUtil.GER, GER);
        countryMap.put(LfsUtil.ITA, ITA);
        countryMap.put(LfsUtil.JPN, JPN);
        countryMap.put(LfsUtil.JPN_B, JPN_B);
        countryMap.put(LfsUtil.NOR, NOR);
        countryMap.put(LfsUtil.KOR, KOR);
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
