package com.sofiiapryhoda.scoreboard;

import com.sofiiapryhoda.scoreboard.exception.InvalidMatchException;
import com.sofiiapryhoda.scoreboard.exception.InvalidScoreException;
import com.sofiiapryhoda.scoreboard.exception.MatchAlreadyExistException;
import com.sofiiapryhoda.scoreboard.exception.MatchNotFoundException;
import com.sofiiapryhoda.scoreboard.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.sofiiapryhoda.scoreboard.TestValues.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreBoardTest {
    private ScoreBoard scoreBoard;

    @BeforeEach
    void init() {
        scoreBoard = new ScoreBoard();
    }

    @Test
    void testShouldStartMatchWithZeroScore() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        List<Match> matches = scoreBoard.getMatches();
        assertEquals(1, matches.size());
        Match match = matches.get(0);

        assertEquals(TEAM_A, match.getHomeTeam());
        assertEquals(TEAM_B, match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @ParameterizedTest
    @MethodSource("normalizationCasesTestData")
    void testShouldNormalizeTeamsNamesAndSuccessfullyStartMatch(String homeTeam, String awayTeam) {
        scoreBoard.startMatch(homeTeam, awayTeam);

        List<Match> matches = scoreBoard.getMatches();
        assertEquals(1, matches.size());
        Match match = matches.get(0);

        assertEquals("Team A", match.getHomeTeam());
        assertEquals("Team B", match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    void testStartMatchShouldNotAllowDuplicateMatches() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        List<Match> matches = scoreBoard.getMatches();
        assertEquals(1, matches.size());
        Match match = matches.get(0);

        assertEquals(TEAM_A, match.getHomeTeam());
        assertEquals(TEAM_B, match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());

        MatchAlreadyExistException exception = assertThrows(MatchAlreadyExistException.class, () -> scoreBoard.startMatch(TEAM_A, TEAM_C));

        assertEquals("Cannot start match. TeamA or TeamC already playing", exception.getMessage());
    }

    @Test
    void testShouldNotAllowStartMatchWithSameTeam() {
        assertThrows(InvalidMatchException.class, () -> scoreBoard.startMatch(TEAM_A, TEAM_A));
    }

    @ParameterizedTest
    @MethodSource("invalidTeamsNamesTestData")
    void testShouldNotAllowStartMatchWithInvalidTeamName(String homeTeam, String awayTeam, String expectedErrorMessage) {
        InvalidMatchException exception = assertThrows(InvalidMatchException.class, () -> scoreBoard.startMatch(homeTeam, awayTeam));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void testShouldUpdateScore() {
       scoreBoard.startMatch(TEAM_A, TEAM_B);

       List<Match> matches = scoreBoard.getMatches();
       Match match = matches.get(0);
       assertEquals(0, match.getHomeScore());
       assertEquals(0, match.getAwayScore());

       scoreBoard.updateScore(TEAM_A, TEAM_B, 1, 1);

       assertEquals(1, match.getHomeScore());
       assertEquals(1, match.getAwayScore());
    }

    @ParameterizedTest
    @MethodSource("normalizationCasesTestData")
    void testShouldNormalizeTeamsNamesAndSuccessfullyUpdateScore(String homeTeam, String awayTeam) {
        scoreBoard.startMatch(homeTeam, awayTeam);

        List<Match> matches = scoreBoard.getMatches();
        Match match = matches.get(0);
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());

        scoreBoard.updateScore(homeTeam, awayTeam, 1, 1);

        assertEquals(1, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    void testShouldNotAllowUpdateScoreForNonExistingTeam() {
        MatchNotFoundException exception = assertThrows(MatchNotFoundException.class, () -> scoreBoard.updateScore(TEAM_A, TEAM_B, 1, 1));

        assertEquals(getExpectedErrorMessageForNonFoundMatch(TEAM_A, TEAM_B), exception.getMessage());
    }

    @Test
    void testShouldNotAllowNegativeScores() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        List<Match> matches = scoreBoard.getMatches();
        Match match = matches.get(0);
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());

        InvalidScoreException exception = assertThrows(InvalidScoreException.class, () -> scoreBoard.updateScore(TEAM_A, TEAM_B, -1, 1));

        assertEquals("Scores must be non-negative", exception.getMessage());
    }

    @Test
    void testShouldNotAllowDecreaseScores() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        List<Match> matches = scoreBoard.getMatches();
        Match match = matches.get(0);
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());

        scoreBoard.updateScore(TEAM_A, TEAM_B, 3, 2);

        assertEquals(3, match.getHomeScore());
        assertEquals(2, match.getAwayScore());

        InvalidScoreException exception = assertThrows(InvalidScoreException.class, () -> scoreBoard.updateScore(TEAM_A, TEAM_B, 2, 1));

        assertEquals("Scores cannot decrease", exception.getMessage());
    }

    @Test
    void testShouldRemoveMatchOnceFinished() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);
        scoreBoard.startMatch(TEAM_C, TEAM_D);

        List<Match> matches = scoreBoard.getMatches();
        assertEquals(2, matches.size());

        scoreBoard.finishMatch(TEAM_A, TEAM_B);

        assertEquals(1, matches.size());

        Match match = matches.get(0);
        assertEquals(TEAM_C, match.getHomeTeam());
        assertEquals(TEAM_D, match.getAwayTeam());
    }

    @ParameterizedTest
    @MethodSource("normalizationCasesTestData")
    void testShouldNormalizeTeamsNamesAndSuccessfullyRemoveMatchOnceFinished(String homeTeam, String awayTeam) {
        scoreBoard.startMatch("Team A", "Team B");
        scoreBoard.startMatch(TEAM_C, TEAM_D);

        List<Match> matches = scoreBoard.getMatches();
        assertEquals(2, matches.size());

        scoreBoard.finishMatch(homeTeam, awayTeam);

        assertEquals(1, matches.size());

        Match match = matches.get(0);
        assertEquals(TEAM_C, match.getHomeTeam());
        assertEquals(TEAM_D, match.getAwayTeam());
    }

    @Test
    void testShouldSuccessfullyRemoveMatchWhenTeamNamesAreSwaped() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);
        scoreBoard.startMatch(TEAM_C, TEAM_D);

        List<Match> matches = scoreBoard.getMatches();
        assertEquals(2, matches.size());

        scoreBoard.finishMatch(TEAM_B, TEAM_A);

        assertEquals(1, matches.size());

        Match match = matches.get(0);
        assertEquals(TEAM_C, match.getHomeTeam());
        assertEquals(TEAM_D, match.getAwayTeam());
    }

    @Test
    void testShouldNotAllowFinishNonExistingMatch() {
        MatchNotFoundException exception = assertThrows(MatchNotFoundException.class, () -> scoreBoard.finishMatch(TEAM_A, TEAM_B));

        assertEquals(getExpectedErrorMessageForNonFoundMatch(TEAM_A, TEAM_B), exception.getMessage());
    }

    @Test
    void testShouldNotAllowFinishMatchWithSameTeams() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        InvalidMatchException exception = assertThrows(InvalidMatchException.class, () -> scoreBoard.finishMatch(TEAM_A, TEAM_A));

        assertEquals("Home team and away team cannot be the same", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidTeamsNamesTestData")
    void testShouldNotAllowFinishMatchWithInvalidTeamName(String homeTeam, String awayTeam, String errorMessage) {
        InvalidMatchException exception = assertThrows(InvalidMatchException.class, () -> scoreBoard.finishMatch(homeTeam, awayTeam));

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testGetSummaryShouldReturnMatchesInScoreOrderInCaseScoresAreDifferent() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);
        scoreBoard.startMatch(TEAM_C, TEAM_D);

        scoreBoard.updateScore(TEAM_A, TEAM_B, 1, 1);
        scoreBoard.updateScore(TEAM_C, TEAM_D, 2, 2);

        List<Match> matches = scoreBoard.getSummary();

        assertEquals(2, matches.size());

        assertEquals(TEAM_C, matches.get(0).getHomeTeam());
        assertEquals(TEAM_D, matches.get(0).getAwayTeam());
        assertEquals(2, matches.get(0).getHomeScore());
        assertEquals(2, matches.get(0).getAwayScore());

        assertEquals(TEAM_A, matches.get(1).getHomeTeam());
        assertEquals(TEAM_B, matches.get(1).getAwayTeam());
        assertEquals(1, matches.get(1).getHomeScore());
        assertEquals(1, matches.get(1).getAwayScore());
    }

    @Test
    void testGetSummaryShouldReturnMatchesInStartTimeOrderWhenScoresAreEqual() throws InterruptedException {
        scoreBoard.startMatch(TEAM_A, TEAM_B);
        Thread.sleep(3);
        scoreBoard.startMatch(TEAM_C, TEAM_D);

        scoreBoard.updateScore(TEAM_A, TEAM_B, 1, 1);
        scoreBoard.updateScore(TEAM_C, TEAM_D, 1, 1);

        List<Match> matches = scoreBoard.getSummary();

        assertEquals(2, matches.size());

        assertEquals(TEAM_C, matches.get(0).getHomeTeam());
        assertEquals(TEAM_D, matches.get(0).getAwayTeam());
        assertEquals(1, matches.get(0).getHomeScore());
        assertEquals(1, matches.get(0).getAwayScore());

        assertEquals(TEAM_A, matches.get(1).getHomeTeam());
        assertEquals(TEAM_B, matches.get(1).getAwayTeam());
        assertEquals(1, matches.get(1).getHomeScore());
        assertEquals(1, matches.get(1).getAwayScore());
    }

    @Test
    void testGetSummaryShouldReturnEmptyListWhenNoActiveMatches() {
        assertEquals(0, scoreBoard.getSummary().size());
    }

    private static Stream<Arguments> invalidTeamsNamesTestData() {
        return Stream.of(
            Arguments.of("Team@123", TEAM_C, "Invalid team name format (contains invalid characters): Team@123"),
            Arguments.of("", TEAM_C, "Team name cannot be null or empty"),
            Arguments.of("  ", TEAM_C, "Team name cannot be null or empty")
        );
    }

    private static Stream<Arguments> normalizationCasesTestData() {
        return Stream.of(
            Arguments.of("    Team A", "Team B    "),
            Arguments.of("Team     A", "   Team   B    ")
        );
    }

    private String getExpectedErrorMessageForNonFoundMatch(String homeTeam, String awayTeam) {
        return "Match between " + homeTeam + " and " + awayTeam + " not found";
    }
}
