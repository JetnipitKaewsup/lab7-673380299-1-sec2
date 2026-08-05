package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.demo.service.GameService;
import com.example.demo.model.Game;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    // Constructor Injection
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public String listGames(Model model) {
        model.addAttribute("games", gameService.getAllGames());
        return "games/list";
    }

    @GetMapping("/add")
    public String showAddGameForm(Model model) {
        model.addAttribute("game", new Game());
        return "games/add";
    }

    @PostMapping("/save")
    public String addGame(@ModelAttribute Game game) {
        gameService.addGame(game);
        return "redirect:/games";
    }

    @GetMapping("/edit/{id}")
    public String showEditGameForm(@PathVariable Long id, Model model) {
        model.addAttribute("game", gameService.getGameById(id));
        return "games/edit";
    }

    @PostMapping("/update/{id}")
    public String updateGame(@PathVariable Long id,
                             @ModelAttribute Game game) {
        gameService.updateGame(id, game);
        return "redirect:/games";
    }


    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id,Model model) {
        model.addAttribute("game", gameService.getGameById(id));
        return "games/delete";
    }
    

    @PostMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return "redirect:/games";
    }
}