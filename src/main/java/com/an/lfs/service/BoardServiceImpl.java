package com.an.lfs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.an.lfs.LfsUtil;
import com.an.lfs.db.SussDao;
import com.an.lfs.vo.BoardPo;
import com.an.lfs.vo.BoardSummary;
import com.an.lfs.vo.LeagueSummary;

@Component
@Scope("singleton")
public class BoardServiceImpl implements BoardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoardServiceImpl.class);
    @Autowired
    private SussDao dao;

    @Override
    public List<BoardSummary> getBoardSummary(String league) {
        List<BoardPo> boards = new ArrayList<>();
        dao.getBoardByLeague(league, boards);
        LOGGER.info("board list size: {}", boards.size());

        // League -> {year, BoardPo}
        Map<Integer, List<BoardPo>> map = new HashMap<>();

        for (BoardPo bp : boards) {
            int year = bp.getPlayYear();
            if (!map.containsKey(year)) {
                map.put(year, new ArrayList<BoardPo>());
            }
            List<BoardPo> list = map.get(year);
            list.add(bp);
        }

        List<BoardSummary> result = new ArrayList<>();
        for (int year : map.keySet()) {
            List<BoardPo> list = map.get(year);
            int totalCnt = 0;
            int winCnt = 0;
            int drawCnt = 0;
            int loseCnt = 0;
            for (BoardPo bp : list) {
                totalCnt += bp.getMatchCnt();
                winCnt += bp.getWinCnt();
                drawCnt += bp.getDrawCnt();
                loseCnt += bp.getLoseCnt();
            }
            BoardSummary sum = new BoardSummary();
            sum.setLeague(league);
            sum.setYear(year);
            sum.setWinPer(Float.parseFloat(LfsUtil.DECIMAL_FORMAT_1.format((float) winCnt * 100f / (float) totalCnt)));
            sum.setDrawPer(Float.parseFloat(LfsUtil.DECIMAL_FORMAT_1.format((float) drawCnt * 100f / (float) totalCnt)));
            sum.setLosePer(Float.parseFloat(LfsUtil.DECIMAL_FORMAT_1.format((float) loseCnt * 100f / (float) totalCnt)));
            result.add(sum);
        }
        return result;
    }

    @Override
    public LeagueSummary getLeagueSummary(String league, int lastYearNum) {
        LOGGER.debug("league: {}, lastYearNum: {}", league, lastYearNum);
        List<BoardPo> boards = new ArrayList<>();
        dao.getBoardByLeague(league, boards);
        LOGGER.debug("board list size: {}", boards.size());

        int totalCnt = 0;
        int winCnt = 0;
        int drawCnt = 0;
        int loseCnt = 0;
        for (BoardPo bp : boards) {
            if (bp.getPlayYear() == 2014) {
                continue;
            }
            // 2011 + 3
            if ((bp.getPlayYear() + lastYearNum) >= 2014) {
                totalCnt += bp.getMatchCnt();
                winCnt += bp.getWinCnt();
                drawCnt += bp.getDrawCnt();
                loseCnt += bp.getLoseCnt();
            }
        }
        LeagueSummary result = new LeagueSummary();
        result.setLeague(league);
        result.setWinPer(Float.parseFloat(LfsUtil.DECIMAL_FORMAT_1.format((float) winCnt * 100f / (float) totalCnt)));
        result.setDrawPer(Float.parseFloat(LfsUtil.DECIMAL_FORMAT_1.format((float) drawCnt * 100f / (float) totalCnt)));
        result.setLosePer(Float.parseFloat(LfsUtil.DECIMAL_FORMAT_1.format((float) loseCnt * 100f / (float) totalCnt)));
        return result;
    }

    @Override
    public List<BoardSummary> getTeamSummary() {
        return null;
    }
}
