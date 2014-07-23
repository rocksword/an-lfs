package com.an.lfs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;

public class CompanyMgr {
    public static String WILLIAM_HILL = "WilliamHill";
    public static String LIBO = "LiBo";
    public static String ODDSET = "Oddset";

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
        if (companys.containsKey(comp)) {
            return companys.get(comp);
        }
        return comp;
    }

    private static void init() {
        String filepath = LfsUtil.getConfFilePath("company.txt");
        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            String line = null;
            while ((line = iter.nextLine()) != null) {
                String[] strs = line.split(",");
                if (strs.length != 2) {
                    logger.info("Invalid line: " + line);
                    continue;
                }
                companys.put(strs[0].trim(), strs[1].trim());
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
        }
    }
}
