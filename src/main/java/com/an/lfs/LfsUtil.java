package com.an.lfs;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.vo.BetResult;
import com.an.lfs.vo.MatchResult;
import com.an.lfs.vo.RateResult;

public class LfsUtil {
    private static final Log logger = LogFactory.getLog(LfsUtil.class);

    public static final String LFS_HOME = "LFS_HOME";

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd hh:mm:ss";
    public static final String HADOOP_INSTALL = "HADOOP_INSTALL";
    public static final String HBASE_INSTALL = "HBASE_INSTALL";

    public static final String ENG = "eng";
    public static final String FRA = "fra";
    public static final String GER = "ger";
    public static final String ITA = "ita";
    public static final String JPN = "jpn";
    public static final String KOR = "kor";
    public static final String ESP = "esp";
    public static final String USA = "usa";
    public static String[] COUNTRIES = new String[] { ENG, FRA, GER, ITA, JPN, KOR, ESP, USA };

    public static final String William = "William";
    public static final String LiBo = "LiBo";
    public static final String Bet365 = "Bet365";
    public static final String Oddset = "Oddset";
    public static final String SNAI = "SNAI";
    public static final String AoMen = "AoMen";
    public static final String WeiDe = "WeiDe";
    public static final String Bwin = "Bwin";

    public static String MATCH_FILE = "match.txt";
    public static final String COMPANY_FILE = "company.txt";

    public static String WILLIAM_HILL = "WilliamHill";
    public static String LIBO = "LiBo";
    public static String ODDSET = "Oddset";

    public static String PASS = "P +";
    public static String FAIL = "F -";
    public static String NULL = "N";
    public static String SEPARATOR = "~";
    public static String COMMA = ",";
    public static String NEXT_LINE = "\n";

    public static String WIN = "W +";
    public static String DRAW = "D =";
    public static String LOSE = "L -";

    private static final String DIR_CONF = "conf";
    private static final String DIR_INPUT = "input";
    private static final String DIR_OUTPUT = "output";
    public static String STATIS_HEADER = "TYPE,NUM,PER,PASS_NUM,FAIL_NUM,PASS_PER,FAIL_PER\n";
    public static String MATCH_HEADER = "TYPE,NUM,PER\n";

    public static String getBetStr(BetResult betRet) {
        if (betRet.isPass()) {
            return PASS;
        } else if (betRet.isFail()) {
            return FAIL;
        } else {
            return NULL;
        }
    }

    public static int getFailPer(int passNum, int failNum) {
        return (int) (100 * (float) failNum / (float) (passNum + failNum));
    }

    public static int getPassPer(int passNum, int failNum) {
        return (int) (100 * (float) passNum / (float) (passNum + failNum));
    }

    public static int getPercent(int total, int passNum, int failNum) {
        return (int) (100 * (float) (passNum + failNum) / (float) total);
    }

    public static Comparator<String> floatCompare = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Float.compare(Float.parseFloat(o1), Float.parseFloat(o2));
        }
    };

    public static String getCat(float val) {
        String result = null;
        if (Float.compare(1.2f, val) >= 0) {
            result = "1.2";
        } else if (Float.compare(1.4f, val) >= 0) {
            result = "1.4";
        } else if (Float.compare(1.6f, val) >= 0) {
            result = "1.6";
        } else if (Float.compare(1.8f, val) >= 0) {
            result = "1.8";
        } else if (Float.compare(2.0f, val) >= 0) {
            result = "2.0";
        } else if (Float.compare(2.3f, val) >= 0) {
            result = "2.3";
        } else if (Float.compare(2.6f, val) >= 0) {
            result = "2.6";
        } else if (Float.compare(3.0f, val) >= 0) {
            result = "3.0";
        } else if (Float.compare(4.0f, val) >= 0) {
            result = "4.0";
        } else if (Float.compare(5.0f, val) >= 0) {
            result = "5.0";
        } else if (Float.compare(6.0f, val) >= 0) {
            result = "6.0";
        } else if (Float.compare(7.0f, val) >= 0) {
            result = "7.0";
        } else if (Float.compare(8.0f, val) >= 0) {
            result = "8.0";
        } else {
            result = "9.0";
        }
        return result;
    }

    public static MatchResult getMatchResult(String score) {
        if (score == null || score.trim().isEmpty()) {
            return MatchResult.INVALID;
        }

        String[] strs = score.trim().split("-");
        if (strs.length != 2) {
            logger.error("Invalid score: " + score);
            return MatchResult.INVALID;
        }

        MatchResult matResult = MatchResult.WIN;
        if (strs[0].trim().compareTo(strs[1].trim()) == 0) {
            matResult = MatchResult.DRAW;
        } else if (strs[0].trim().compareTo(strs[1].trim()) < 0) {
            matResult = MatchResult.LOSE;
        }
        return matResult;
    }

    /**
     * @param win
     * @param draw
     * @param lose
     * @return
     */
    public static boolean isValidRate(float win, float draw, float lose) {
        if ((win != 0.0f) || (draw != 0.0f) || (lose != 0.0f)) {
            return true;
        }
        return false;
    }

    /**
     * @param win
     * @param draw
     * @param lose
     * @return
     */
    public static RateResult getRateResult(float win, float draw, float lose) {
        if ((win == 0.0f) || (draw == 0.0f) || (lose == 0.0f)) {
            return RateResult.INVALID;
        }

        float min = win;
        RateResult rateResult = RateResult.WIN;
        if (Float.compare(draw, min) < 0) {
            min = draw;
            rateResult = RateResult.DRAW;
        }
        if (Float.compare(lose, min) < 0) {
            min = lose;
            rateResult = RateResult.LOSE;
        }
        return rateResult;
    }

    /**
     * @param country
     * @param year
     * @param filename
     * @return
     */
    public static String getInputFilePath(String country, int year, String filename) {
        String home = getLfsHome();
        String homeDir = new File(home).getAbsolutePath();
        String subDir = String.format("%s_%s", country, year);
        return new StringBuilder().append(homeDir).append(File.separator).append(DIR_INPUT).append(File.separator)
                .append(subDir).append(File.separator).append(filename).toString();
    }

    /**
     * @param country
     * @param year
     * @return
     */
    public static String getStatisFile(String country, int year) {
        return String.format("%s_%s_statis.csv", country, year);
    }

    /**
     * @param country
     * @param year
     * @return
     */
    public static String getSumFile(String country, int year) {
        return String.format("%s_%s_sum.csv", country, year);
    }

    public static String getLfsHome() {
        String dir = System.getenv(LFS_HOME);
        if (StringUtils.isEmpty(dir)) {
            throw new RuntimeException("Not found LFS_HOME, please set it.");
        }
        return dir;
    }

    public static String getConfFilePath(String filename) {
        String home = getLfsHome();
        File file = new File(home);
        return new StringBuilder().append(file.getAbsolutePath()).append(File.separator).append(DIR_CONF)
                .append(File.separator).append(filename).toString();
    }

    public static String getOutputFilePath(String filename) {
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
