package com.sofiiapryhoda.scoreboard.model;

import com.sofiiapryhoda.scoreboard.exception.InvalidScoreException;

public class Match {
    private final String homeTeam;
    private final String awayTeam;
    private final long startTime;
    private int homeScore;
    private int awayScore;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = System.currentTimeMillis();
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
            throw new InvalidScoreException("Scores must be non-negative");
        }
        if (newHomeScore < this.homeScore || newAwayScore < this.awayScore) {
            throw new InvalidScoreException("Scores cannot decrease");
        }
        this.homeScore = newHomeScore;
        this.awayScore = newAwayScore;
    }

    @Override
    public String toString() {
        return "Match{" +
            "homeTeam='" + homeTeam + '\'' +
            ", awayTeam='" + awayTeam + '\'' +
            ", startTime=" + startTime +
            ", homeScore=" + homeScore +
            ", awayScore=" + awayScore +
            '}';
    }
}
