package com.sofiiapryhoda.scoreboard;

import com.sofiiapryhoda.scoreboard.exception.InvalidMatchException;
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

        assertThrows(MatchAlreadyExistException.class, () -> scoreBoard.startMatch(TEAM_A, TEAM_C));
    }

    @Test
    void testShouldNotAllowStartMatchWithSameTeam() {
        assertThrows(MatchAlreadyExistException.class, () -> scoreBoard.startMatch(TEAM_A, TEAM_A));
    }

    @ParameterizedTest
    @MethodSource("invalidTeamsNamesTestData")
    void testShouldNotAllowStartMatchWithInvalidTeamName(String homeTeam, String awayTeam) {
        assertThrows(InvalidMatchException.class, () -> scoreBoard.startMatch(homeTeam, awayTeam));
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
        assertThrows(MatchNotFoundException.class, () -> scoreBoard.updateScore(TEAM_A, TEAM_B, -1, 1));
    }

    @Test
    void testShouldNotAllowNegativeScores() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);
        assertThrows(InvalidMatchException.class, () -> scoreBoard.updateScore(TEAM_A, TEAM_B, -1, 1));
    }

    @Test
    void testShouldNotAllowDecreaseScores() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);
        scoreBoard.updateScore(TEAM_A, TEAM_B, 3, 2);

        assertThrows(InvalidMatchException.class, () -> scoreBoard.updateScore(TEAM_A, TEAM_B, 2, 1));
    }

    private static Stream<Arguments> invalidTeamsNamesTestData() {
        return Stream.of(
            Arguments.of("Team@123", TEAM_C),
            Arguments.of("", TEAM_C),
            Arguments.of("  ", TEAM_C)
        );
    }
}
