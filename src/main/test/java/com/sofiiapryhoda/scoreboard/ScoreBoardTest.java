package com.sofiiapryhoda.scoreboard;

import com.sofiiapryhoda.scoreboard.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        assertThrows(RuntimeException.class, () -> scoreBoard.startMatch(TEAM_A, TEAM_C));
    }

    @Test
    void testShouldNotAllowStartMatchWithSameTeam() {
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        assertThrows(RuntimeException.class, () -> scoreBoard.startMatch(TEAM_A, TEAM_C));
    }

    @Test
    void testShouldNotAllowStartMatchWithInvalidTeamName() {
        assertThrows(RuntimeException.class, () -> scoreBoard.startMatch("Team@123", TEAM_C));

    }

    @Test
    void testShouldNotAllowStartMatchWithEmptyTeamName() {
        assertThrows(RuntimeException.class, () -> scoreBoard.startMatch("", TEAM_C));
    }

    @Test
    void testShouldNotAllowStartMatchWithBlankTeamName() {
        assertThrows(RuntimeException.class, () -> scoreBoard.startMatch("  ", TEAM_C));
    }
}
