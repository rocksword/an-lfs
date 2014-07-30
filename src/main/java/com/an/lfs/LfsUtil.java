package com.an.lfs;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.enu.BetRet;
import com.an.lfs.enu.CmpType;
import com.an.lfs.enu.Country;
import com.an.lfs.enu.ForecastRet;
import com.an.lfs.enu.ScoreType;

public class LfsUtil {
    private static final Log logger = LogFactory.getLog(LfsUtil.class);

    public static final String LFS_HOME = "LFS_HOME";
    public static int CURRENT_YEAR = 2014;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd hh:mm:ss";
    public static final String HADOOP_INSTALL = "HADOOP_INSTALL";
    public static final String HBASE_INSTALL = "HBASE_INSTALL";

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

    public static String PASS = "P";
    public static String FAIL = "F";
    public static String NULL = "N";
    public static String TOP3 = "T3";
    public static String LAST3 = "L3";
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
    public static String STATIS_HEADER_SIMPLE = "TYPE,NUM,PER\n";

    public static final String A_A_A = "- - -";
    public static final String A_A_Z = "- - +";
    public static final String A_A_O = "- - =";
    public static final String A_Z_A = "- + -";
    public static final String A_Z_Z = "- + +";
    public static final String A_Z_O = "- + =";
    public static final String A_O_A = "- = -";
    public static final String A_O_Z = "- = +";
    public static final String A_O_O = "- = =";

    public static final String Z_A_A = "+ - -";
    public static final String Z_A_Z = "+ - +";
    public static final String Z_A_O = "+ - =";
    public static final String Z_Z_A = "+ + -";
    public static final String Z_Z_Z = "+ + +";
    public static final String Z_Z_O = "+ + =";
    public static final String Z_O_A = "+ = -";
    public static final String Z_O_Z = "+ = +";
    public static final String Z_O_O = "+ = =";

    public static final String O_A_A = "= - -";
    public static final String O_A_Z = "= - +";
    public static final String O_A_O = "= - =";
    public static final String O_Z_A = "= + -";
    public static final String O_Z_Z = "= + +";
    public static final String O_Z_O = "= + =";
    public static final String O_O_A = "= = -";
    public static final String O_O_Z = "= = +";
    public static final String O_O_O = "= = =";

    public static final String W_W = "W_W";
    public static final String W_D = "W_D";
    public static final String W_L = "W_L";
    public static final String L_W = "L_W";
    public static final String L_D = "L_D";
    public static final String L_L = "L_L";

    public static final String BRA = "bra";
    public static final String ENG = "eng";
    public static final String ESP = "esp";
    public static final String FRA = "fra";
    public static final String GER = "ger";
    public static final String ITA = "ita";
    public static final String JPN = "jpn";
    public static final String JPN_B = "jpn_b";
    public static final String NOR = "nor";
    public static final String KOR = "kor";
    public static final String SWE = "swe";
    public static final String USA = "usa";
    public static final String CL = "cl";
    public static final String EL = "el";

    public static String getBetRetStr(BetRet betRet) {
        if (betRet.isPass()) {
            return PASS;
        } else if (betRet.isFail()) {
            return FAIL;
        } else {
            return NULL;
        }
    }

    public static Comparator<String> floatCompare = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Float.compare(Float.parseFloat(o1), Float.parseFloat(o2));
        }
    };

    public static CmpType getTrend(float win, float draw, float lose, float winEnd, float drawEnd, float loseEnd) {
        if (winEnd == 0.0f || drawEnd == 0.0f || loseEnd == 0.0f) {
            return CmpType.NULL;
        }
        StringBuilder ret = new StringBuilder();
        if (Float.compare(winEnd, win) > 0) {
            ret.append("+").append(" ");
        } else if (Float.compare(winEnd, win) == 0) {
            ret.append("=").append(" ");
        } else {
            ret.append("-").append(" ");
        }
        if (Float.compare(drawEnd, draw) > 0) {
            ret.append("+").append(" ");
        } else if (Float.compare(drawEnd, draw) == 0) {
            ret.append("=").append(" ");
        } else {
            ret.append("-").append(" ");
        }
        if (Float.compare(loseEnd, lose) > 0) {
            ret.append("+");
        } else if (Float.compare(loseEnd, lose) == 0) {
            ret.append("=");
        } else {
            ret.append("-");
        }
        String cmp = ret.toString();
        return CmpType.getCmpType(cmp);
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
    public static ForecastRet getRateForecast(float win, float draw, float lose) {
        if ((win == 0.0f) || (draw == 0.0f) || (lose == 0.0f)) {
            return ForecastRet.NULL;
        }

        float min = win;
        ForecastRet result = ForecastRet.WIN;

        if (Float.compare(draw, min) < 0) {
            min = draw;
            result = ForecastRet.DRAW;
        }

        if (Float.compare(lose, min) < 0) {
            min = lose;
            result = ForecastRet.LOSE;
        }
        return result;
    }

    /**
     * @param country
     * @param year
     * @param filename
     * @return
     */
    public static String getInputFilePath(Country country, int year, String filename) {
        String home = getLfsHome();
        String homeDir = new File(home).getAbsolutePath();
        String subDir = String.format("%s_%s", country.getVal(), year);
        return new StringBuilder().append(homeDir).append(File.separator).append(DIR_INPUT).append(File.separator)
                .append(subDir).append(File.separator).append(filename).toString();
    }

    /**
     * @param country
     * @return
     */
    public static String getInputFilePath(Country country) {
        String home = getLfsHome();
        String homeDir = new File(home).getAbsolutePath();
        String filename = String.format("%s.txt", country.getVal());
        return new StringBuilder().append(homeDir).append(File.separator).append(DIR_INPUT).append(File.separator)
                .append(filename).toString();
    }

    /**
     * @param country
     * @param year
     * @return
     */
    public static String getInputFilePath(Country country, int year) {
        String home = getLfsHome();
        String homeDir = new File(home).getAbsolutePath();
        String filename = String.format("%s_%s.txt", country.getVal(), year);
        return new StringBuilder().append(homeDir).append(File.separator).append(DIR_INPUT).append(File.separator)
                .append(filename).toString();
    }

    /**
     * @param country
     * @param year
     * @return
     */
    public static String getStatisFile(String country, int year) {
        return String.format("%s_%s_statis.csv", country, year);
    }

    public static String getSumFile(Country country, int year) {
        return String.format("%s_%s_sum.csv", country.getVal(), year);
    }

    public static String getSumExcelFile(Country country, int year) {
        return String.format("%s_%s_sum.xls", country.getVal(), year);
    }

    public static String getBoardExcelFile(Country country) {
        return String.format("%s_board.xls", country.getVal());
    }

    public static String getMatchExcelFile(Country country) {
        return String.format("%s_match.xls", country.getVal());
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

    public static WritableCellFormat getGreenFont() throws WriteException {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
        WritableCellFormat wcf = new WritableCellFormat(wf);
        return wcf;
    }

    public static WritableCellFormat getRedFont() throws WriteException {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.RED);
        WritableCellFormat wcf = new WritableCellFormat(wf);
        return wcf;
    }

    public static WritableCellFormat getRoseFont() throws WriteException {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.ROSE);
        WritableCellFormat wcf = new WritableCellFormat(wf);
        return wcf;
    }

    public static WritableCellFormat getYellowFont() throws WriteException {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.YELLOW);
        WritableCellFormat wcf = new WritableCellFormat(wf);
        return wcf;
    }

    public static WritableCellFormat getBlueFont() throws WriteException {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLUE2);
        WritableCellFormat wcf = new WritableCellFormat(wf);
        return wcf;
    }

    public static WritableCellFormat getRedFmt() throws WriteException {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setBackground(Colour.RED);
        return wcf;
    }

    public static WritableCellFormat getRoseFmt() throws WriteException {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setBackground(Colour.ROSE);
        return wcf;
    }

    public static WritableCellFormat getYellowFmt() throws WriteException {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setBackground(Colour.YELLOW);
        return wcf;
    }

    public static ScoreType getScoreType(String score) {
        ScoreType ret = ScoreType.INVALID;
        if (score != null && !score.trim().isEmpty()) {
            String[] strs = score.trim().split("-");
            if (strs.length != 2) {
                logger.error("Invalid score: " + score);
                ret = ScoreType.INVALID;
            } else {
                ret = ScoreType.WIN;
                if (strs[0].trim().compareTo(strs[1].trim()) == 0) {
                    ret = ScoreType.DRAW;
                } else if (strs[0].trim().compareTo(strs[1].trim()) < 0) {
                    ret = ScoreType.LOSE;
                }
            }
        }
        return ret;
    }

    public static String getRankPKString(int hostRank, int guestRank) {
        String ret = String.format(" %02d - %02d", hostRank, guestRank);
        return ret;
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
