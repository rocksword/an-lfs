package com.an.lfs.vo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.Country;

public class LfsConfMgr {
    private static final Logger logger = LoggerFactory.getLogger(LfsConfMgr.class);

    private static Map<String, List<String>> ctyCompanyMap = new HashMap<>();

    /**
     * @param country
     * @return
     */
    public static List<String> getCompany(Country country) {
        if (ctyCompanyMap.isEmpty()) {
            initMap();
        }
        if (!ctyCompanyMap.containsKey(country.getVal())) {
            logger.warn("Not found {}'s company map.", country);
            return null;
        }
        return ctyCompanyMap.get(country.getVal());
    }

    public static boolean contains(Country country, String company) {
        if (ctyCompanyMap.isEmpty()) {
            initMap();
        }

        if (ctyCompanyMap.containsKey(country.getVal())) {
            return ctyCompanyMap.get(country.getVal()).contains(company);
        }
        return false;
    }

    private static void initMap() {
        Map<String, List<String>> countryCompanies = conf.getCountryCompanies();
        ctyCompanyMap.putAll(countryCompanies);
    }

    private static LfsConf conf;
    static {
        init();
    }

    private static void init() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.debug("Initialize conf.");
            String filepath = LfsUtil.getConfFilePath("lfs.txt");
            File f = new File(filepath);
            logger.debug("file: " + f.getPath());
            conf = mapper.readValue(f, LfsConf.class);
            logger.debug("conf: " + conf.toString());
        } catch (Exception e) {
            logger.error("Error: " + e);
            throw new RuntimeException("Failed to initialize configuration, error: " + e);
        }
    }

    public LfsConfMgr() {
    }
}