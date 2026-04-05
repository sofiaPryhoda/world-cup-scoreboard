package com.sofiiapryhoda.scoreboard.validation;

import com.sofiiapryhoda.scoreboard.exception.InvalidMatchException;

import java.util.regex.Pattern;

public class MatchInputValidator {
    private static final Pattern TEAM_PATTERN_NAME = Pattern.compile("^[\\p{L}0-9 .\\-']+$");

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
            throw new InvalidMatchException("Invalid team name format (contains invalid characters): " + teamName);
        }
    }

    private void validateTeamsAreDistinct(String homeTeam, String awayTeam) {
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new InvalidMatchException("Home team and away team cannot be the same");
        }
    }
}
