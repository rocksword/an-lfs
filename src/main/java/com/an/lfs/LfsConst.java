package com.an.lfs;

import com.an.lfs.vo.Country;

public class LfsConst {
    public static final String LFS_HOME = "LFS_HOME";

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd hh:mm:ss";
    public static final String HADOOP_INSTALL = "HADOOP_INSTALL";
    public static final String HBASE_INSTALL = "HBASE_INSTALL";

    public static Country[] COUNTRIES = new Country[] { Country.ENG, Country.GER, Country.FRA, Country.ITA, Country.SPA };

    public static String MATCH_FILE = "match.txt";

    public static String PASS = "T";
    public static String FAIL = "F";
    public static String SEPARATOR = "~";
    public static String COMMA = ",";
    public static String NEXT_LINE = "\n";
}
