package com.example.round_robin.model;

public class Match {
    private Player player1;
    private Player player2;
    private Integer player1Score;
    private Integer player2Score;

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }

    public Integer getPlayer1Score() { return player1Score; }
    public void setPlayer1Score(Integer score) { this.player1Score = score; }

    public Integer getPlayer2Score() { return player2Score; }
    public void setPlayer2Score(Integer score) { this.player2Score = score; }

    public void recordResult(int p1Score, int p2Score) {
        this.player1Score = p1Score;
        this.player2Score = p2Score;
        if (p1Score > p2Score) player1.addPoints(3);
        else if (p2Score > p1Score) player2.addPoints(3);
        else {
            player1.addPoints(1);
            player2.addPoints(1);
        }
    }
}