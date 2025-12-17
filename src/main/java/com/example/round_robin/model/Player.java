package com.example.round_robin.model;
public class Player {
    private String name;
    private int points = 0;

    public Player(String name) { this.name = name; }

    public String getName() { return name; }
    public int getPoints() { return points; }
    public void addPoints(int p) { this.points += p; }
}
