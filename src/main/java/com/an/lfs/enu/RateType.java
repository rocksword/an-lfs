package com.an.lfs.enu;

public enum RateType {
    RT_1_0("1.0"), //
    RT_1_2("1.2"), //
    RT_1_4("1.4"), //
    RT_1_6("1.6"), //
    RT_1_8("1.8"), //
    RT_2_0("2.0"), //
    RT_2_3("2.3"), //
    RT_2_6("2.6"), //
    RT_3_0("3.0"), //
    RT_4_0("4.0"), //
    RT_5_0("5.0"), //
    RT_6_0("6.0"), //
    RT_7_0("7.0"), //
    RT_8_0("8.0");

    public static RateType[] allRateTypes = new RateType[] { RateType.RT_1_0, RateType.RT_1_2, RateType.RT_1_4,
            RateType.RT_1_6, RateType.RT_1_8, RateType.RT_2_0, RateType.RT_2_3, RateType.RT_2_6, RateType.RT_3_0,
            RateType.RT_4_0, RateType.RT_5_0, RateType.RT_6_0, RateType.RT_7_0, RateType.RT_8_0 };

    private String val;

    public String getVal() {
        return val;
    }

    private RateType(String val) {
        this.val = val;
    }
}