package com.example.round_robin.controller;

import com.example.round_robin.model.Match;
import com.example.round_robin.model.Player;
import com.example.round_robin.service.LeagueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LeagueController {
    private final LeagueService leagueService;
    private final String ADMIN_PASSWORD = "RVPOOL";

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    // Set players - admin only
    @PostMapping("/players")
    public String setPlayers(@RequestBody Map<String, Object> body) {
        String password = (String) body.get("password");
        if(!ADMIN_PASSWORD.equals(password)) return "Unauthorized";

        List<String> names = (List<String>) body.get("names");
        leagueService.setPlayers(names);
        return "Players set and fixtures generated!";
    }

    @PostMapping("/fixtures/{group}/{index}/winner")
    public String recordWinner(
            @PathVariable String group,
            @PathVariable int index,
            @RequestBody Map<String, String> body
    ) {
        String password = body.get("password");
        if(!ADMIN_PASSWORD.equals(password)) return "Unauthorized";

        String winner = body.get("winner"); // "player1", "player2", "draw"
        Match match = group.equalsIgnoreCase("A") ? leagueService.getMatchesA().get(index)
                : leagueService.getMatchesB().get(index);

        if("player1".equalsIgnoreCase(winner)) match.recordResult(1,0);
        else if("player2".equalsIgnoreCase(winner)) match.recordResult(0,1);
        else match.recordResult(0,0);

        return "Result recorded!";
    }


    // Viewers can get fixtures and league table (no password required)
    @GetMapping("/fixtures/{group}")
    public List<Match> getFixtures(@PathVariable String group) {
        return group.equalsIgnoreCase("A") ? leagueService.getMatchesA() : leagueService.getMatchesB();
    }

    @GetMapping("/table/{group}")
    public List<Player> getTable(@PathVariable String group) {
        return leagueService.getLeagueTable(group.toUpperCase());
    }
}
