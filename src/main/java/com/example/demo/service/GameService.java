package com.example.demo.service;

import com.example.demo.model.Game;
import com.example.demo.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    // Read All
    public List<Game> getAllGames() {
        return repository.findAll();
    }

    // Read One
    public Game getGameById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Add
    public Game addGame(Game game) {
        return repository.save(game);
    }

    // Update
    public Game updateGame(Long id, Game game) {

        Game oldGame = repository.findById(id).orElse(null);

        if (oldGame != null) {
            oldGame.setTitle(game.getTitle());
            oldGame.setGenre(game.getGenre());
            oldGame.setPlatform(game.getPlatform());
            oldGame.setRating(game.getRating());
            oldGame.setReleaseDate(game.getReleaseDate());
            oldGame.setPrice(game.getPrice());
            oldGame.setDiscountType(game.getDiscountType());

            return repository.save(oldGame);
        }

        return null;
    }

    // Delete
    public boolean deleteGame(Long id) {

    if (repository.existsById(id)) {
        repository.deleteById(id);
        return true;
    }

    return false;
}
}