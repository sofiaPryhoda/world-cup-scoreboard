package com.sofiiapryhoda.scoreboard.model;

import com.sofiiapryhoda.scoreboard.exception.InvalidMatchException;

public class Match {
    private final String homeTeam;
    private final String awayTeam;
    private final long startTime;
    private int homeScore;
    private int awayScore;

    public Match(String homeTeam, String awayTeam, long startTime) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = startTime;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void updateScore(int newHomeScore, int newAwayScore) {
        if (newHomeScore < 0 || newAwayScore < 0) {
            throw new InvalidMatchException("Scores must be non-negative");
        }

        if (newHomeScore < this.homeScore || newAwayScore < this.awayScore) {
            throw new InvalidMatchException("Scores cannot decrease");
        }
        this.homeScore = newHomeScore;
        this.awayScore = newAwayScore;
    }
}
