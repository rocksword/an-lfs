package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import com.an.lfs.enu.Country;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.MatchInfo;
import com.an.lfs.vo.MatchRuleMgr;

public class LfsMain {
    public final static Country[] ALL_COUNTRY = new Country[] { Country.BRA };
    private static int BEGIN_YEAR = 2013;
    private static int END_YEAR = 2014;
    // 0: diff report 1: all country, 2: league country
    private static int TYPE = 0;

    public static void main(String[] args) throws Exception {
        init();
        if (TYPE == 0) {
            for (Country cty : ALL_COUNTRY) {
                // Generate board report
                for (int year = BEGIN_YEAR; year <= END_YEAR; year++) {
                    RateLoader rateLoader = new RateLoader(cty, year);
                    // Generate match report
                    Map<Integer, List<MatchInfo>> yearMatchMap = new MatchLoader(cty, year).getYearMatchMap();
                    ReportMaker.makeMatchDiffReport(year, cty, yearMatchMap, rateLoader);
                }
            }
        } else if (TYPE == 1) {
            for (Country cty : ALL_COUNTRY) {
                // Generate board report
                Map<Integer, List<BoardTeam>> teamMap = new BoardLoader(cty).getBoardTeamMap();
                ReportMaker.makeBoardReport(cty, teamMap);
                for (int year = BEGIN_YEAR; year <= END_YEAR; year++) {
                    RateLoader rateLoader = new RateLoader(cty, year);
                    // Generate match report
                    Map<Integer, List<MatchInfo>> yearMatchMap = new MatchLoader(cty, year).getYearMatchMap();

                    MatchRuleMgr matchRuleMgr = new MatchRuleMgr();

                    ReportMaker.makeMatchReport(cty, yearMatchMap, rateLoader, matchRuleMgr);
                }
            }
        } else if (TYPE == 2) {
            for (Country cty : Country.LEAGUE_COUNTRY) {
                for (int year = BEGIN_YEAR; year <= END_YEAR; year++) {
                    RateLoader rateLoader = new RateLoader(cty, year);

                    LeagueMatchLoader league = new LeagueMatchLoader(cty, 2014, 2014);
                    Map<Integer, List<MatchInfo>> yearMatchMap = league.getYearMatchMap();

                    MatchRuleMgr matchRuleMgr = new MatchRuleMgr();

                    ReportMaker.makeLeagueMatchReport(cty, yearMatchMap, rateLoader, matchRuleMgr);
                }
            }
        } else if (TYPE == 3) {
            for (int year = BEGIN_YEAR; year <= END_YEAR; year++) {
                MatchReportMaker maker = new MatchReportMaker(Country.JPN_B, year);
                maker.analyzeMatch();
                maker.analyzeRate();
                maker.exportExcel();
                // maker.generateRateFiles();
            }
        }
    }

    private static void init() throws FileNotFoundException {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        System.out.println("filepath: " + filepath);
        File f = new File(filepath);
        PropertyConfigurator.configure(new FileInputStream(f));
    }
}
