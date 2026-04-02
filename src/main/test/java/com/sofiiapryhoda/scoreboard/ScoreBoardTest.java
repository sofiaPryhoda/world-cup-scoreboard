package com.sofiiapryhoda.scoreboard;

import com.sofiiapryhoda.scoreboard.model.Match;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.sofiiapryhoda.scoreboard.TestValues.TEAM_A;
import static com.sofiiapryhoda.scoreboard.TestValues.TEAM_B;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreBoardTest {

    @Test
    void testShouldStartMatchWithZeroScore() {
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startMatch(TEAM_A, TEAM_B);

        List<Match> matches = scoreBoard.getMatches();

        assertEquals(1, matches.size());
        Match match = matches.get(0);

        assertEquals(TEAM_A, match.getHomeTeam());
        assertEquals(TEAM_B, match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }
}
