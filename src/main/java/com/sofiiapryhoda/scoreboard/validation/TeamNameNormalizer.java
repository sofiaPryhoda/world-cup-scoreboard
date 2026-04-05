package com.sofiiapryhoda.scoreboard.validation;

public final class TeamNameNormalizer {
    private TeamNameNormalizer() {}

    public static String normalizeTeamName(String teamName) {
        return teamName == null ? null : teamName.trim().replaceAll("\\s+",  " ");
    }
}
