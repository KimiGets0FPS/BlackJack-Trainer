package com.blackjack.game;

public class Card {
    private final String suit;
    private final String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getRankValue() {
        return Cards.getRankValue(rank);
    }

    @Override
    public String toString() {
        return Cards.getSuitIcon(suit) + rank;
    }
}
