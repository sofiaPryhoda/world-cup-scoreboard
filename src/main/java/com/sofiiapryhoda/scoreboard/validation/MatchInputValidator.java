package com.sofiiapryhoda.scoreboard.validation;

import com.sofiiapryhoda.scoreboard.exception.InvalidMatchException;
import com.sofiiapryhoda.scoreboard.exception.MatchAlreadyExistException;

import java.util.regex.Pattern;

public class MatchInputValidator {
    private static final Pattern TEAM_PATTERN_NAME = Pattern.compile("^[A-Z][a-zA-Z\\s]*$");

    public void validateMatch(String homeTeam, String awayTeam) {
        validateTeamName(homeTeam);
        validateTeamName(awayTeam);
        validateTeamsAreDistinct(homeTeam, awayTeam);
    }

    private void validateTeamName(String teamName) {
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new InvalidMatchException("Team name cannot be null or empty");
        }
        if (!TEAM_PATTERN_NAME.matcher(teamName).matches()) {
            throw new InvalidMatchException("Team name must start from capital letter and contain only letters and spaces");
        }
    }

    private void validateTeamsAreDistinct(String homeTeam, String awayTeam) {
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new MatchAlreadyExistException("Home team and away team cannot be the same");
        }
    }
}
