package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LfsUtil {
    private static final Log logger = LogFactory.getLog(LfsConfMgr.class);
    private static final String DIR_CONF = "conf";
    private static final String DIR_INPUT = "input";
    private static final String DIR_OUTPUT = "output";

    public synchronized static String getLfsHome() {
        String dir = System.getenv(LfsConst.LFS_HOME);
        if (StringUtils.isEmpty(dir)) {
            throw new RuntimeException("Not found LFS_HOME, please set it.");
        }
        return dir;
    }

    public synchronized static String getConfFilePath(String filename) {
        String home = getLfsHome();
        File file = new File(home);
        return new StringBuilder().append(file.getAbsolutePath()).append(File.separator).append(DIR_CONF)
                .append(File.separator).append(filename).toString();
    }

    public synchronized static String getInputFilePath(String filename) {
        String home = getLfsHome();
        File file = new File(home);
        return new StringBuilder().append(file.getAbsolutePath()).append(File.separator).append(DIR_INPUT)
                .append(File.separator).append(filename).toString();
    }

    public synchronized static String getOutputFilePath(String filename) {
        String home = getLfsHome();
        File file = new File(home);
        return new StringBuilder().append(file.getAbsolutePath()).append(File.separator).append(DIR_OUTPUT)
                .append(File.separator).append(filename).toString();
    }

    // invalid chars in domain names
    private static String[] invalidDomainNameChars = { "/", "!", "#", "@", "$", "%", "<", ">", "^", "&", "*", "(", ")",
            "[", "]", "{", "}", ",", "?", "\"", "'", "+", "|", "_", "'", ":" };

    public static boolean isValidUrl(String urlString) {
        String regex = "^(https|http|ftp|rtsp|mms)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(urlString);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isIpAddr(String ipAddr) {
        String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddr);
        return matcher.matches();
    }

    public static boolean isValidDomain(String domain) {
        if (domain == null) {
            return false;
        }

        domain = domain.trim();
        if (domain.isEmpty()) {
            return false;
        }
        if (domain.startsWith(".") || domain.startsWith("-")) {
            return false;
        }
        if (!domain.contains(".")) {
            return false;
        }

        for (String invalid : invalidDomainNameChars) {
            if (domain.contains(invalid)) {
                return false;
            }
        }
        return true;
    }

    public static void logCollection(Collection<?> collection) {
        logger.info("Total size: " + collection.size());
        for (Object obj : collection) {
            logger.info(obj);
        }
    }
}
