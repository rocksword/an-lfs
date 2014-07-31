package com.an.lfs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.an.lfs.enu.ScoreType;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.Cell;
import com.an.lfs.vo.MatchInfo;
import com.an.lfs.vo.TeamMgr;

public class ReportMaker {
    private static final Log logger = LogFactory.getLog(ReportMaker.class);

    public static void makeMatchReport(Country cty, Map<Integer, List<MatchInfo>> yearMatchMap) throws IOException,
            WriteException, RowsExceededException {
        if (yearMatchMap.isEmpty()) {
            return;
        }
        List<Integer> years = new ArrayList<>();
        years.addAll(yearMatchMap.keySet());
        Collections.sort(years);

        BoardLoader board = new BoardLoader(cty);

        List<List<Cell>> rows = new ArrayList<>();
        rows.add(getMatchReportHead());

        int totalYear = years.size();
        for (int i = 0; i < totalYear; i++) {
            if (i > 0) {
                rows.add(getBlankRow(boardReportHeadNames.length));
            }

            int year = years.get(totalYear - 1 - i);
            logger.info("country: " + cty.getVal() + ", year: " + year);

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
                String hostName = TeamMgr.getName(cty, host);
                String guestName = TeamMgr.getName(cty, guest);

                List<Cell> row = new ArrayList<>();
                row.add(new Cell(year));
                row.add(new Cell(mi.getTurn()));
                row.add(new Cell(mi.getDate().substring(0, 5)));

                WritableCellFormat hostFmt = null;
                if (isHostTopN) {
                    hostFmt = LfsUtil.getRoseFmt();
                } else if (isHostLastN) {
                    hostFmt = LfsUtil.getYellowFmt();
                }
                row.add(new Cell(hostName, hostFmt));

                Integer hostRank = getTeamRank(teamRank, host);
                Integer guestRank = getTeamRank(teamRank, guest);

                row.add(new Cell(hostRank));
                row.add(new Cell(mi.getScore()));

                row.add(new Cell(LfsUtil.getRankPKString(hostRank, guestRank)));
                row.add(getRankBet(hostRank, guestRank, mi.getScoreType()));// Rank bet

                WritableCellFormat guestFmt = null;
                if (isGuestTopN) {
                    guestFmt = LfsUtil.getRoseFmt();
                } else if (isGuestLastN) {
                    guestFmt = LfsUtil.getYellowFmt();
                }
                row.add(new Cell(guestName, guestFmt));

                row.add(new Cell(guestRank));
                row.add(new Cell(mi.getWin()));
                row.add(new Cell(mi.getDraw()));
                row.add(new Cell(mi.getLose()));

                row.add(new Cell(getTopLastStr(isHostTopN, isGuestTopN, isHostLastN, isGuestLastN, host, guest,
                        teamRank)));
                rows.add(row);
            }
        }

        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getMatchExcelFile(cty));
        logger.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
        WritableSheet sheet = wb.createSheet(cty.getVal(), 0);
        addRows(sheet, rows);
        wb.write();
        wb.close();
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

    private static Cell getRankBet(int hostRank, int guestRank, ScoreType scoreType) throws WriteException {
        if (scoreType.isInvalid()) {
            return new Cell(LfsUtil.NULL);
        } else if (scoreType.isWin() && hostRank < guestRank) {
            return new Cell(LfsUtil.PASS);
        } else if (scoreType.isLose() && hostRank > guestRank) {
            return new Cell(LfsUtil.PASS);
        } else {
            return new Cell(LfsUtil.FAIL, LfsUtil.getBlueFont());
        }
    }

    private static Integer getTeamRank(Map<String, Integer> teamRank, String team) {
        if (!teamRank.containsKey(team)) {
            logger.warn("Not found team rank, team: " + team);
            return 0;
        }
        return teamRank.get(team);
    }

    private static String[] matchReportHeadNames = new String[] { "Year", "Turn", "Date", "Host", "Rank", "Score",
            "PK", "Bet", "Guest", "Rank", "W", "D", "L", "T3-L3" };

    private static List<Cell> getMatchReportHead() {
        List<Cell> head = new ArrayList<>();
        for (String name : matchReportHeadNames) {
            head.add(new Cell(name));
        }
        return head;
    }

    public static void makeBoardReport(Country cty, Map<Integer, List<BoardTeam>> teamMap) throws IOException,
            WriteException, RowsExceededException {
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

    private static String[] boardReportHeadNames = new String[] { "Year", "R", "Team", "T", "W", "D", "L", "For",
            "Against", "Net", "For", "Against", "W%", "D%", "L%", "Score" };

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
