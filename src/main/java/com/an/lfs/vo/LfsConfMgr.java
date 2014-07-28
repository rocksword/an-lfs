package com.an.lfs.vo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.Country;

public class LfsConfMgr {
    private static final Log logger = LogFactory.getLog(LfsConfMgr.class);

    private static Map<Country, Set<String>> ctyCompanys = new HashMap<>();

    /**
     * @param country
     * @return
     */
    public static List<String> getCompany(Country country) {
        if (ctyCompanys.isEmpty()) {
            initMap();
        }
        List<String> list = new ArrayList<>();

        Set<String> set = ctyCompanys.get(country);
        list.addAll(set);

        Collections.sort(list);
        return list;
    }

    public static boolean contains(Country country, String company) {
        if (ctyCompanys.isEmpty()) {
            initMap();
        }

        if (ctyCompanys.containsKey(country)) {
            return ctyCompanys.get(country).contains(company);
        }
        return false;
    }

    private static void initMap() {
        List<String> countries = conf.getCountries();
        List<List<String>> companys = conf.getCompanys();
        if (countries.size() != companys.size()) {
            logger.error("Invalid country and company.");
            logger.error(countries);
            logger.error(companys);
        }
        for (int i = 0; i < countries.size(); i++) {
            Country cty = Country.getCountry(countries.get(i));
            ctyCompanys.put(cty, new HashSet<>(companys.get(i)));
        }
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

    @Override
    public String toString() {
        return "LfsConfMgr [" + (conf != null ? "conf=" + conf : "") + "]";
    }
}