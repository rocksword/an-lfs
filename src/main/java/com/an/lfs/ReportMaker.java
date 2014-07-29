package com.an.lfs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.Cell;
import com.an.lfs.vo.MatchInfo;

public class ReportMaker {
    private static final Log logger = LogFactory.getLog(ReportMaker.class);

    public static void exportMatchReport(Country cty, Map<Integer, List<MatchInfo>> yearMatchMap) throws IOException,
            WriteException, RowsExceededException {
        if (yearMatchMap.isEmpty()) {
            return;
        }
        List<Integer> years = new ArrayList<>();
        years.addAll(yearMatchMap.keySet());
        Collections.sort(years);

        List<List<Cell>> rows = new ArrayList<>();
        rows.add(getMatchReportHead());

        int total = years.size();
        for (int i = 0; i < total; i++) {
            if (i > 0) {
                rows.add(getBlankRow(boardReportHeadNames.length));
            }
            Integer year = years.get(total - 1 - i);
            List<MatchInfo> matchList = yearMatchMap.get(year);
            for (MatchInfo mi : matchList) {
                rows.add(mi.getRow(year));
            }
        }
        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getMatchExcelFile(cty));
        logger.info("Generate file " + filepath);

        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
        WritableSheet sheet = wb.createSheet(cty.getVal(), 0);
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
        wb.write();
        wb.close();
    }

    private static String[] matchReportHeadNames = new String[] { "Year", "Turn", "Date", "Host", "Score", "Guest",
            "Win", "Draw", "Lose" };

    private static List<Cell> getMatchReportHead() {
        List<Cell> head = new ArrayList<>();
        for (String name : matchReportHeadNames) {
            head.add(new Cell(name));
        }
        return head;
    }

    public static void exportBoardReport(Country cty, Map<Integer, List<BoardTeam>> teamMap) throws IOException,
            WriteException, RowsExceededException {
        if (teamMap.isEmpty()) {
            return;
        }

        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getBoardExcelFile(cty));
        logger.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));

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
                rows.add(bt.getRow(year, teams.size()));
            }
        }

        WritableSheet sheet = wb.createSheet(cty.getVal(), 0);
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
        wb.write();
        wb.close();
    }

    private static String[] boardReportHeadNames = new String[] { "Year", "Rank", "Team", "Total", "Win", "Draw",
            "Lose", "Goal for", "Against", "Net", "Avg for", "Against", "Win%", "Draw%", "Lose%", "Score" };

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
