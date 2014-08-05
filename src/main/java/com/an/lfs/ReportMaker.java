package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.enu.Result;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.Cell;
import com.an.lfs.vo.MatchInfo;
import com.an.lfs.vo.MatchRate;
import com.an.lfs.vo.Rate;

public class ReportMaker {
    private static final Log logger = LogFactory.getLog(ReportMaker.class);
    private static String[] matchReportHeadNames = new String[] { "Year", "T", "Date", "Host", "R", "Score", "PK",
            "Guest", "R", "TN-LN", "ScoreRet", "RankFc", };

    public static void makeMatchReport(Country country, Map<Integer, List<MatchInfo>> yearMatchMap,
            RateLoader rateLoader) throws Exception {
        if (yearMatchMap.isEmpty()) {
            return;
        }
        List<Integer> years = new ArrayList<>();
        years.addAll(yearMatchMap.keySet());
        Collections.sort(years);

        BoardLoader board = new BoardLoader(country);

        List<List<Cell>> rows = new ArrayList<>();
        List<Cell> head = getMatchReportHead();
        LfsUtil.addComRateHead(rateLoader, head);
        rows.add(head);

        int totalYear = years.size();
        for (int i = 0; i < totalYear; i++) {
            if (i > 0) {
                rows.add(getBlankRow(boardReportHeadNames.length));
            }

            int year = years.get(totalYear - 1 - i);
            logger.info("country: " + country.getVal() + ", year: " + year);

            Set<String> topN = board.getTopNTeam(year);
            Set<String> lastN = board.getLastNTeam(year);
            Map<String, Integer> teamRank = board.getTeamRank(year);
            List<MatchInfo> matchList = yearMatchMap.get(year);
            for (MatchInfo mi : matchList) {
                boolean isHostTopN = topN.contains(mi.getHost());
                boolean isHostLastN = lastN.contains(mi.getHost());
                boolean isGuestTopN = topN.contains(mi.getGuest());
                boolean isGuestLastN = lastN.contains(mi.getGuest());

                String host = mi.getHost();
                String guest = mi.getGuest();

                List<Cell> row = new ArrayList<>();
                row.add(new Cell(year));
                row.add(new Cell(mi.getTurn()));
                row.add(new Cell(mi.getDate().substring(0, 5)));

                row.add(new Cell(host, isHostTopN ? LfsUtil.getRoseFmt()
                        : (isHostLastN ? LfsUtil.getYellowFmt() : null)));

                Integer hostRank = getTeamRank(teamRank, host);
                Integer guestRank = getTeamRank(teamRank, guest);

                row.add(new Cell(hostRank));
                row.add(new Cell(mi.getScore()));

                row.add(new Cell(LfsUtil.getRankPKString(hostRank, guestRank)));
                row.add(new Cell(guest, isGuestTopN ? LfsUtil.getRoseFmt() : (isGuestLastN ? LfsUtil.getYellowFmt()
                        : null)));

                row.add(new Cell(guestRank));
                row.add(new Cell(getTopLastStr(isHostTopN, isGuestTopN, isHostLastN, isGuestLastN, host, guest,
                        teamRank)));

                // Score result
                row.add(LfsUtil.getResultCell(LfsUtil.getScoreRet(mi.getScore())));
                // Rank forecast
                row.add(LfsUtil.getResultCell(LfsUtil.getRankFc(hostRank, guestRank)));

                addComRate(rateLoader, host, guest, row);
                rows.add(row);
            }
        }

        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getMatchExcelFile(country));
        logger.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
        WritableSheet sheet = wb.createSheet(country.getVal(), 0);
        addRows(sheet, rows);
        wb.write();
        wb.close();
    }

    private static WritableCellFormat getFont(float win, float winEnd) throws WriteException {
        WritableCellFormat font = null;
        if (winEnd > win) {
            font = LfsUtil.getRoseFont();
        } else if (winEnd < win) {
            font = LfsUtil.getBlueFont();
        }
        return font;
    }

    private static String getTopLastStr(boolean isHostTopN, boolean isGuestTopN, boolean isHostLastN,
            boolean isGuestLastN, String host, String guest, Map<String, Integer> teamRank) {
        String ret = "";
        if (isHostTopN) {
            ret = "TN";
        } else if (isHostLastN) {
            ret = "LN";
        } else {
            ret = "  ";
        }
        if (isGuestTopN) {
            ret = ret + " - " + "TN";
        } else if (isGuestLastN) {
            ret = ret + " - " + "LN";
        } else {
            ret = ret + " - " + "  ";
        }
        return ret;
    }

    private static Integer getTeamRank(Map<String, Integer> teamRank, String team) {
        if (!teamRank.containsKey(team)) {
            logger.warn("Not found team rank, team: " + team);
            return 0;
        }
        return teamRank.get(team);
    }

    private static List<Cell> getMatchReportHead() {
        List<Cell> head = new ArrayList<>();
        for (String name : matchReportHeadNames) {
            head.add(new Cell(name));
        }
        return head;
    }

    private static String[] boardReportHeadNames = new String[] { "Year", "R", "Team", "T", "W", "D", "L", "For",
            "Against", "Net", "For", "Against", "W%", "D%", "L%", "Score" };

    public static void makeBoardReport(Country cty, Map<Integer, List<BoardTeam>> teamMap) throws Exception {
        if (teamMap.isEmpty()) {
            logger.warn("Empty team map, country " + cty.getVal());
            return;
        }
        List<Integer> years = new ArrayList<>();
        years.addAll(teamMap.keySet());
        Collections.sort(years);

        List<List<Cell>> rows = new ArrayList<>();
        rows.add(getBoardReportHead());
        int yearCnt = years.size();
        for (int i = 0; i < yearCnt; i++) {
            if (i > 0) {
                rows.add(getBlankRow(boardReportHeadNames.length));
            }
            int year = years.get((yearCnt - 1 - i));
            List<BoardTeam> teams = teamMap.get(year);
            for (BoardTeam bt : teams) {
                rows.add(bt.getRow(cty, year, teams.size()));
            }
        }

        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getBoardExcelFile(cty));
        logger.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
        WritableSheet sheet = wb.createSheet(cty.getVal(), 0);
        addRows(sheet, rows);
        wb.write();
        wb.close();
    }

    private static String[] leagueMatchReportHeadNames = new String[] { "Date", "Host", "Score", "Guest", "ScoreRet" };

    public static void makeLeagueMatchReport(Country country, Map<Integer, List<MatchInfo>> yearMatchMap,
            RateLoader rateLoader) throws Exception {
        if (yearMatchMap.isEmpty()) {
            return;
        }
        List<Integer> years = new ArrayList<>();
        years.addAll(yearMatchMap.keySet());
        Collections.sort(years);

        List<List<Cell>> rows = new ArrayList<>();
        List<Cell> head = getLeagueMatchReportHead();
        LfsUtil.addComRateHead(rateLoader, head);
        rows.add(head);

        List<List<Cell>> tmpRows = new ArrayList<>();
        int totalYear = years.size();
        for (int i = 0; i < totalYear; i++) {
            int year = years.get(totalYear - 1 - i);
            logger.info("country: " + country.getVal() + ", year: " + year);

            List<MatchInfo> matchList = yearMatchMap.get(year);
            for (MatchInfo mi : matchList) {
                String host = mi.getHost();
                String guest = mi.getGuest();
                List<Cell> row = new ArrayList<>();
                row.add(new Cell(mi.getDate()));// 0
                row.add(new Cell(host));// 1
                row.add(new Cell(mi.getScore()));
                row.add(new Cell(guest));// 3
                row.add(LfsUtil.getResultCell(LfsUtil.getScoreRet(mi.getScore())));
                addComRate(rateLoader, host, guest, row);
                tmpRows.add(row);
            }
        }
        Collections.sort(tmpRows, rowComparator);
        rows.addAll(tmpRows);

        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getMatchExcelFile(country));
        logger.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
        WritableSheet sheet = wb.createSheet(country.getVal(), 0);
        addRows(sheet, rows);
        wb.write();
        wb.close();
    }

    private static Comparator<List<Cell>> rowComparator = new Comparator<List<Cell>>() {
        @Override
        public int compare(List<Cell> o1, List<Cell> o2) {
            String key1 = getRowKey(o1);
            String key2 = getRowKey(o2);
            return key1.compareTo(key2);
        }

        private String getRowKey(List<Cell> o1) {
            List<String> teams = new ArrayList<>();
            teams.add(o1.get(1).getVal());
            teams.add(o1.get(3).getVal());
            Collections.sort(teams);
            String key = teams.get(0) + teams.get(1);
            return key;
        }
    };

    private static void addComRate(RateLoader rateLoader, String host, String guest, List<Cell> row)
            throws WriteException {
        MatchRate mr = rateLoader.getMatchRate(host, guest);
        if (mr == null) {
            return;
        }
        for (String com : mr.getCompany()) {
            Rate rate = mr.getRate(com);
            float win = rate.getWin();
            float draw = rate.getDraw();
            float lose = rate.getLose();
            float winEnd = rate.getWinEnd();
            float drawEnd = rate.getDrawEnd();
            float loseEnd = rate.getLoseEnd();
            float winRatio = rate.getWinRatio();
            float drawRatio = rate.getDrawRatio();
            float loseRatio = rate.getLoseRatio();

            // Rate forecast
            row.add(LfsUtil.getResultCell(LfsUtil.getRateFc(winEnd, drawEnd, loseEnd)));
            // Trend forecast
            row.add(LfsUtil.getResultCell(LfsUtil.getTrendFc(win, draw, lose, winEnd, drawEnd, loseEnd)));
            // Ratio forecast
            row.add(LfsUtil.getResultCell(LfsUtil.getRatioFc(winRatio, drawRatio, loseRatio)));

            row.add(new Cell(win));
            row.add(new Cell(draw));
            row.add(new Cell(lose));
            row.add(new Cell(winEnd, getFont(win, winEnd)));
            row.add(new Cell(drawEnd, getFont(draw, drawEnd)));
            row.add(new Cell(loseEnd, getFont(lose, loseEnd)));

            Result ret = LfsUtil.getRatioFc(winRatio, drawRatio, loseRatio);
            row.add(new Cell(winRatio, ret.isWin() ? LfsUtil.getRedFont() : null));
            row.add(new Cell(drawRatio, ret.isDraw() ? LfsUtil.getRedFont() : null));
            row.add(new Cell(loseRatio, ret.isLose() ? LfsUtil.getRedFont() : null));
        }
    }

    private static List<Cell> getLeagueMatchReportHead() {
        List<Cell> head = new ArrayList<>();
        for (String name : leagueMatchReportHeadNames) {
            head.add(new Cell(name));
        }
        return head;
    }

    private static void addRows(WritableSheet sheet, List<List<Cell>> rows) throws WriteException,
            RowsExceededException {
        for (int row = 0; row < rows.size(); row++) {
            List<Cell> rowList = rows.get(row);
            for (int col = 0; col < rowList.size(); col++) {
                Cell cell = rowList.get(col);
                if (cell.getFmt() != null) {
                    sheet.addCell(new Label(col, row, cell.getVal(), cell.getFmt()));
                } else {
                    sheet.addCell(new Label(col, row, cell.getVal()));
                }
            }
        }
    }

    private static List<Cell> getBoardReportHead() {
        List<Cell> head = new ArrayList<>();
        for (String name : boardReportHeadNames) {
            head.add(new Cell(name));
        }
        return head;
    }

    private static List<Cell> getBlankRow(int size) {
        List<Cell> head = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            head.add(new Cell(""));
        }
        return head;
    }
}
