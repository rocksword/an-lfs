package com.an.lfs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;

public class CompanyMgr {
    private static final Log logger = LogFactory.getLog(CompanyMgr.class);
    // Raw company name -> companyKey
    private static Map<String, String> companyMap = new HashMap<>();

    public static String getName(String company) {
        if (!companyMap.containsKey(company)) {
            logger.warn("Not found company: " + company);
            return company;
        }

        return companyMap.get(company);
    }

    public static Map<String, String> getCompanyMap() {
        return companyMap;
    }

    public CompanyMgr() {
    }

    static {
        init();
    }

    private static void init() {
        String filepath = LfsUtil.getConfFilePath(LfsConst.COMPANY_FILE);
        logger.info("filepath: " + filepath);

        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            String line = null;
            while ((line = iter.nextLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] strs = line.split(",");
                if (strs.length != 2) {
                    logger.info("Invalid line: " + line);
                    continue;
                }
                companyMap.put(strs[0].trim(), strs[1].trim());
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
        }
    }
}
