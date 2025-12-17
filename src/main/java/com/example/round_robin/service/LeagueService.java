package com.example.round_robin.service;

import com.example.round_robin.model.Match;
import com.example.round_robin.model.Player;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class LeagueService {
    private List<Player> groupA = new ArrayList<>();
    private List<Player> groupB = new ArrayList<>();
    private List<Match> matchesA = new ArrayList<>();
    private List<Match> matchesB = new ArrayList<>();

    public void setPlayers(List<String> names) {
        groupA.clear();
        groupB.clear();
        matchesA.clear();
        matchesB.clear();

        for (int i = 0; i < names.size(); i++) {
            if (i % 2 == 0) groupA.add(new Player(names.get(i)));
            else groupB.add(new Player(names.get(i)));
        }
        generateFixtures();
    }

    private void generateFixtures() {
        matchesA = generateShuffledMatches(groupA);
        matchesB = generateShuffledMatches(groupB);
    }

    /**
     * Generates a round-robin schedule and shuffles matches
     * to avoid consecutive matches for the same player
     */
    private List<Match> generateShuffledMatches(List<Player> group) {
        List<Match> matches = new ArrayList<>();

        // Generate all round-robin matches
        for (int i = 0; i < group.size(); i++) {
            for (int j = i + 1; j < group.size(); j++) {
                matches.add(new Match(group.get(i), group.get(j)));
            }
        }

        // Shuffle matches while trying to avoid same player back-to-back
        List<Match> shuffled = new ArrayList<>();
        Random rand = new Random();

        while (!matches.isEmpty()) {
            for (int i = 0; i < matches.size(); i++) {
                Match candidate = matches.get(i);
                if (shuffled.isEmpty() || !shuffled.get(shuffled.size() - 1).getPlayer1().equals(candidate.getPlayer1())
                        && !shuffled.get(shuffled.size() - 1).getPlayer2().equals(candidate.getPlayer1())
                        && !shuffled.get(shuffled.size() - 1).getPlayer1().equals(candidate.getPlayer2())
                        && !shuffled.get(shuffled.size() - 1).getPlayer2().equals(candidate.getPlayer2())) {

                    shuffled.add(candidate);
                    matches.remove(i);
                    break;
                }
                // If no candidate avoids back-to-back, just pick the first
                if (i == matches.size() - 1) {
                    shuffled.add(matches.remove(0));
                }
            }
        }

        return shuffled;
    }


    public List<Player> getGroupA() { return groupA; }
    public List<Player> getGroupB() { return groupB; }
    public List<Match> getMatchesA() { return matchesA; }
    public List<Match> getMatchesB() { return matchesB; }

    public void recordMatch(String group, int matchIndex, int p1Score, int p2Score) {
        Match match = group.equals("A") ? matchesA.get(matchIndex) : matchesB.get(matchIndex);
        match.recordResult(p1Score, p2Score);
    }

    public List<Player> getLeagueTable(String group) {
        List<Player> table = group.equals("A") ? new ArrayList<>(groupA) : new ArrayList<>(groupB);
        table.sort((p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()));
        return table;
    }
}

