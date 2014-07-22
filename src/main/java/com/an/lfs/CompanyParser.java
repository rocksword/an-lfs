package com.an.lfs;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CompanyParser {
    private static final Log logger = LogFactory.getLog(CompanyParser.class);

    public CompanyParser() {
    }

    public Map<String, String> parse() {
        Map<String, String> companys = new HashMap<String, String>();
        String filepath = LfsUtil.getInputFilePath(LfsMain.ARGUMENT, "2013_01_Bai_Men.txt");
        FileLineIterator iter = new FileLineIterator(filepath);
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
        iter.close();
        return companys;
    }
}
