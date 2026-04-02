package com.sofiiapryhoda.scoreboard;

import com.sofiiapryhoda.scoreboard.exception.MatchAlreadyExistException;
import com.sofiiapryhoda.scoreboard.exception.MatchNotFoundException;
import com.sofiiapryhoda.scoreboard.model.Match;
import com.sofiiapryhoda.scoreboard.validation.MatchInputValidator;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {
    private final List<Match> matches = new ArrayList<>();
    private final MatchInputValidator matchInputValidator = new MatchInputValidator();

    public void startMatch(String homeTeam, String awayTeam) {
        this.matchInputValidator.validateMatch(homeTeam, awayTeam);
        if (this.isTeamInActiveMatch(homeTeam, awayTeam)) {
            throw new MatchAlreadyExistException("Cannot start match. " + homeTeam + " or " + awayTeam + " already playing");
        }
        this.matches.add(new Match(homeTeam, awayTeam, System.currentTimeMillis()));
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        this.findMatch(homeTeam, awayTeam).updateScore(homeScore, awayScore);
    }

    public void finishMatch(String homeTeam, String awayTeam) {

    }

    private Match findMatch(String homeTeam, String awayTeam) {
        return this.matches.stream()
            .filter(m -> m.getHomeTeam().equalsIgnoreCase(homeTeam)
                && m.getAwayTeam().equalsIgnoreCase(awayTeam))
            .findFirst()
            .orElseThrow(() -> new MatchNotFoundException("Match between " + homeTeam + " and" + awayTeam + " not found"));
    }

    private boolean isTeamInActiveMatch(String homeTeam, String awayTeam) {
        return this.matches.stream()
            .anyMatch(m ->
                m.getHomeTeam().equalsIgnoreCase(homeTeam)
                    || m.getHomeTeam().equalsIgnoreCase(awayTeam)
                    || m.getAwayTeam().equalsIgnoreCase(awayTeam)
                    || m.getAwayTeam().equalsIgnoreCase(homeTeam)
            );
    }

    public List<Match> getMatches() {
        return matches;
    }
}
