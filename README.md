# World Cup Scoreboard 

A simple implementation of a Live Football World Cup Scoreboard.

This project allows users to manage ongoing matches, update scores, and retrieve a summary of games ordered by total score and recency.

---

## Features

The scoreboard supports the following operations:

- **Start a game**
  - Adds a new match with initial score `0 - 0`
  - Requires home team and away team

- **Finish a game**
  - Removes the match from the scoreboard

- **Update score**
  - Updates the score for an existing match

- **Get summary**
  - Returns matches ordered by:
    1. Total score (descending)
    2. Most recently started (for equal scores)
