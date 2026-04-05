package com.sofiiapryhoda.scoreboard.exception;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(String homeTeam, String awayTeam) {
        super("Match between " + homeTeam + " and " + awayTeam + " not found");
    }
}
