package com.an.lfs.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.LfsUtil;

public class CompanyParser {
    private static final Log logger = LogFactory.getLog(CompanyParser.class);

    public CompanyParser() {
    }

    public Map<String, String> parse(String country, int year) {
        Map<String, String> companys = new HashMap<String, String>();
        String filepath = LfsUtil.getInputFilePath(country, year, "2013_01_Bai_Men.txt");
        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            Pattern pat = Pattern.compile("\t");
            String line = null;
            while ((line = iter.nextLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] splits = pat.split(line.toString());
                if (splits.length < 2) {
                    logger.info("Invalid line " + line);
                    continue;
                }
                String rawName = splits[1].trim();
                int index = rawName.indexOf("(");
                String name = null;
                if (index != -1) {
                    name = rawName.substring(0, index);
                    System.out.println(rawName + "," + name);
                }
                companys.put(rawName, name);
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
        }
        return companys;
    }
}
