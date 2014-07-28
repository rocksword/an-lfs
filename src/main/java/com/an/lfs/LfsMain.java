package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.apache.log4j.PropertyConfigurator;

import com.an.lfs.enu.Country;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.Cell;

public class LfsMain {
    private static final Log logger = LogFactory.getLog(LfsMain.class);

    // Arguments
    private static void init() throws FileNotFoundException {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        System.out.println("filepath: " + filepath);
        File f = new File(filepath);
        PropertyConfigurator.configure(new FileInputStream(f));
    }

    private static int BEGIN_YEAR = 2014;
    private static int matchReport = 2;

    public static void main(String[] args) throws Exception {
        init();

        if (matchReport == 0) {
            for (int year = BEGIN_YEAR; year < 2015; year++) {
                MatchReportMaker maker = new MatchReportMaker(Country.JPN, year);
                maker.analyzeMatch();
                maker.analyzeRate();
                maker.exportExcel();
                // maker.generateRateFiles();
            }
        } else {
            for (Country cty : Country.allCountries) {
                BoardLoader loader = new BoardLoader(cty);
                Map<Integer, List<BoardTeam>> teamMap = loader.getBoardTeamMap();
                if (!teamMap.isEmpty()) {
                    exportBoardReport(cty, teamMap);
                }
            }
        }
    }

    private static void exportBoardReport(Country cty, Map<Integer, List<BoardTeam>> teamMap) throws IOException,
            WriteException, RowsExceededException {
        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getBoardExcelFile(cty));
        logger.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));

        List<Integer> years = new ArrayList<>();
        years.addAll(teamMap.keySet());
        Collections.sort(years);

        List<List<Cell>> rows = new ArrayList<>();
        rows.add(getHead());
        int yearCnt = years.size();
        for (int i = 0; i < yearCnt; i++) {
            if (i > 0) {
                rows.add(getBlankRow());
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

    private static String[] headNames = new String[] { "Year", "Rank", "Team", "Total", "Win", "Draw", "Lose",
            "Goal for", "Against", "Net", "Avg for", "Against", "Win%", "Draw%", "Lose%", "Score" };

    private static List<Cell> getHead() {
        List<Cell> head = new ArrayList<>();
        for (String name : headNames) {
            head.add(new Cell(name));
        }
        return head;
    }

    private static List<Cell> getBlankRow() {
        List<Cell> head = new ArrayList<>();
        for (int i = 0; i < headNames.length; i++) {
            head.add(new Cell(""));
        }
        return head;
    }
}
