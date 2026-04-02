package com.sofiiapryhoda.scoreboard;

import com.sofiiapryhoda.scoreboard.model.Match;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {
    private final List<Match> matches = new ArrayList<>();

    public void startMatch(String homeTeam, String awayTeam) {

    }

    public List<Match> getMatches() {
        return matches;
    }
}
