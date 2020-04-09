package com.example.ratingapplication;

public class GameRating {
    private int id;
    private String gameName;
    private String producerName;
    private String gameGenre;
    private boolean gameMultiplayer;
    private int rating;

    public boolean isGameMultiplayer() {
        return gameMultiplayer;
    }

    public void setGameMultiplayer(boolean gameMultiplayer) { this.gameMultiplayer = gameMultiplayer; }

    public int getId() { return id; }

    public void setId(int id) {this.id = id; }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getGameGenre() {
        return gameGenre;
    }

    public void setGameGenre(String gameGenre) {
        this.gameGenre = gameGenre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public GameRating() {
        gameName = "";
        producerName = "";
        gameGenre = "";
        gameMultiplayer = false;
        rating = 0;
    }

    @Override
    public String toString() {
        return "Game Rating: \n" +
                "Game Name: " + gameName + "\n" +
                "Producer Name: " + producerName + "\n" +
                "Game Genre: " + gameGenre + "\n" +
                "Multiplayer: " + gameMultiplayer + "\n" +
                "Rating: " + rating;
    }
}
