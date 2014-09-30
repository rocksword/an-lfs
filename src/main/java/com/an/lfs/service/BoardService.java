package com.an.lfs.service;

import java.util.List;

import com.an.lfs.vo.BoardSummary;
import com.an.lfs.vo.LeagueSummary;

public interface BoardService {
    public List<BoardSummary> getBoardSummary(String league);

    public LeagueSummary getLeagueSummary(String league,int lastYearNum);

    public List<BoardSummary> getTeamSummary();
}
