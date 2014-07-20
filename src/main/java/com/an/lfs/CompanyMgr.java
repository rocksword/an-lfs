package com.an.lfs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CompanyMgr {
    private static final Log logger = LogFactory.getLog(CompanyMgr.class);
    // Raw company name -> companyKey
    private static Map<String, String> companys = new HashMap<>();

    public CompanyMgr() {
        init();
    }

    static {
        init();
    }

    /**
     * @param comp
     * @return
     */
    public static String getName(String comp) {
        return companys.get(comp);
    }

    private static void init() {
        String filepath = LfsUtil.getConfFilePath("company.txt");
        FileLineIterator iter = new FileLineIterator(filepath);
        String line = null;
        while ((line = iter.nextLine()) != null) {
            String[] strs = line.split(",");
            if (strs.length != 2) {
                logger.info("Invalid line: " + line);
                continue;
            }
            companys.put(strs[0].trim(), strs[1].trim());
        }
    }
}