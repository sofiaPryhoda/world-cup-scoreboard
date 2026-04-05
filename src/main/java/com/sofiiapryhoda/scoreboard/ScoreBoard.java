package com.sofiiapryhoda.scoreboard;

import com.sofiiapryhoda.scoreboard.exception.MatchAlreadyExistException;
import com.sofiiapryhoda.scoreboard.exception.MatchNotFoundException;
import com.sofiiapryhoda.scoreboard.model.Match;
import com.sofiiapryhoda.scoreboard.validation.MatchInputValidator;
import com.sofiiapryhoda.scoreboard.validation.TeamNameNormalizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreBoard {
    private final MatchInputValidator matchInputValidator = new MatchInputValidator();
    private final List<Match> matches = new ArrayList<>();

    public void startMatch(String homeTeam, String awayTeam) {
        String home = TeamNameNormalizer.normalizeTeamName(homeTeam);
        String away = TeamNameNormalizer.normalizeTeamName(awayTeam);
        this.matchInputValidator.validateMatch(home, awayTeam);
        if (this.isTeamInActiveMatch(home, away)) {
            throw new MatchAlreadyExistException(home, away);
        }
        this.matches.add(new Match(home, away));
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        String home = TeamNameNormalizer.normalizeTeamName(homeTeam);
        String away = TeamNameNormalizer.normalizeTeamName(awayTeam);
        this.matchInputValidator.validateMatch(home, away);
        Match match = this.matches.stream()
            .filter(m -> m.getHomeTeam().equalsIgnoreCase(home) && m.getAwayTeam().equalsIgnoreCase(away))
            .findFirst()
            .orElseThrow(() -> new MatchNotFoundException(home, away));
        match.updateScore(homeScore, awayScore);
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        String home = TeamNameNormalizer.normalizeTeamName(homeTeam);
        String away = TeamNameNormalizer.normalizeTeamName(awayTeam);
        this.matchInputValidator.validateMatch(home, away);
        Match match = this.matches.stream()
            .filter(m ->
                (m.getHomeTeam().equalsIgnoreCase(home) && m.getAwayTeam().equalsIgnoreCase(away)) ||
                (m.getHomeTeam().equalsIgnoreCase(away) && m.getAwayTeam().equalsIgnoreCase(home))
            ).findFirst().orElseThrow(() -> new MatchNotFoundException(home, away));
        this.matches.remove(match);
    }

    public List<Match> getSummary() {
        return this.matches.stream()
            .sorted(
                Comparator.comparingInt((Match m) -> m.getHomeScore() + m.getAwayScore())
                    .reversed()
                    .thenComparing(Comparator.comparingLong(Match::getStartTime).reversed())
            ).toList();
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
