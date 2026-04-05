package com.sofiiapryhoda.scoreboard.exception;

public class MatchAlreadyExistException extends RuntimeException {
    public MatchAlreadyExistException(String homeTeam, String awayTeam) {
        super("Cannot start match. " + homeTeam + " or " + awayTeam + " already playing");
    }
}
