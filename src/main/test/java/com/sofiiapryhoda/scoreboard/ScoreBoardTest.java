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

    @Test
    void testStartMatchShouldNotAllowDuplicateMatches() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        MatchAlreadyExistException exception = assertThrows(MatchAlreadyExistException.class, () -> scoreBoard.startMatch(TEAM_A, TEAM_C));

        assertEquals("Cannot start match. TeamA or TeamC already playing", exception.getMessage());
    }

    @Test
    void testShouldNotAllowStartMatchWithSameTeam() {
        assertThrows(MatchAlreadyExistException.class, () -> scoreBoard.startMatch(TEAM_A, TEAM_A));
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
       scoreBoard.updateScore(TEAM_A, TEAM_B, 1, 1);

       List<Match> matches = scoreBoard.getMatches();
       Match match = matches.get(0);
       assertEquals(1, match.getHomeScore());
       assertEquals(1, match.getAwayScore());
    }

    @Test
    void testShouldNotAllowUpdateScoreForNonExistingTeam() {
        MatchNotFoundException exception = assertThrows(MatchNotFoundException.class, () -> scoreBoard.updateScore(TEAM_A, TEAM_B, -1, 1));

        assertEquals(getExpectedErrorMessageForNonFoundMatch(TEAM_A, TEAM_B), exception.getMessage());
    }

    @Test
    void testShouldNotAllowNegativeScores() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        InvalidScoreException exception = assertThrows(InvalidScoreException.class, () -> scoreBoard.updateScore(TEAM_A, TEAM_B, -1, 1));

        assertEquals("Scores must be non-negative", exception.getMessage());
    }

    @Test
    void testShouldNotAllowDecreaseScores() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);
        scoreBoard.updateScore(TEAM_A, TEAM_B, 3, 2);

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

    @Test
    void testShouldNotAllowFinishNonExistingMatch() {
        MatchNotFoundException exception = assertThrows(MatchNotFoundException.class, () -> scoreBoard.finishMatch(TEAM_A, TEAM_B));

        assertEquals(getExpectedErrorMessageForNonFoundMatch(TEAM_A, TEAM_B), exception.getMessage());
    }

    @Test
    void testShouldNotAllowFinishMatchWithSameTeams() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        MatchNotFoundException exception = assertThrows(MatchNotFoundException.class, () -> scoreBoard.finishMatch(TEAM_A, TEAM_A));

        assertEquals(getExpectedErrorMessageForNonFoundMatch(TEAM_A, TEAM_A), exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidTeamsNamesTestData")
    void testShouldNotAllowFinishMatchWithInvalidTeamName(String homeTeam, String awayTeam) {
        MatchNotFoundException exception = assertThrows(MatchNotFoundException.class, () -> scoreBoard.finishMatch(homeTeam, awayTeam));

        assertEquals(getExpectedErrorMessageForNonFoundMatch(homeTeam, awayTeam), exception.getMessage());
    }

    private static Stream<Arguments> invalidTeamsNamesTestData() {
        return Stream.of(
            Arguments.of("Team@123", TEAM_C, "Team name must start from capital letter and contain only letters and spaces"),
            Arguments.of("", TEAM_C, "Team name cannot be null or empty"),
            Arguments.of("  ", TEAM_C, "Team name cannot be null or empty")
        );
    }

    private String getExpectedErrorMessageForNonFoundMatch(String homeTeam, String awayTeam) {
        return "Match between " + homeTeam + " and " + awayTeam + " not found";
    }
}
