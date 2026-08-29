package com.blackjack.game;

import java.util.ArrayList;
import java.util.Map;

public class Cards {
    private static final String DIAMONDS = "Diamonds";
    private static final String HEARTS = "Hearts";
    private static final String SPADES = "Spades";
    private static final String CLUBS = "Clubs";

    private static final String[] SUITS = {DIAMONDS, SPADES, HEARTS, CLUBS};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};

    private static final Map<String, Integer> RANKS_VALUE = Map.ofEntries(
            Map.entry("2", 2),
            Map.entry("3", 3),
            Map.entry("4", 4),
            Map.entry("5", 5),
            Map.entry("6", 6),
            Map.entry("7", 7),
            Map.entry("8", 8),
            Map.entry("9", 9),
            Map.entry("T", 10),
            Map.entry("J", 10),
            Map.entry("Q", 10),
            Map.entry("K", 10),
            Map.entry("A", 11)
    );

    public static final Map<String, String> SUITS_ICONS = Map.of(
            DIAMONDS, "◆",
            HEARTS, "♥",
            SPADES, "♠",
            CLUBS, "♣"
    );

    public static int getRankValue(String rank) {
        return RANKS_VALUE.get(rank);
    }

    public static String getSuitIcon(String suit) {
        return SUITS_ICONS.get(suit);
    }

    public static Card[] getDeck() {
        Card[] deck = new Card[52];
        int index = 0;
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck[index++] = new Card(suit, rank);
            }
        }
        return deck;
    }

    public static int getHandValue(ArrayList<Card> hand) {
        int value = 0;
        int aceCount = 0;

        for (Card card : hand) {
            value += card.getValue();

            if (card.getRank().equals("A")) {
                aceCount++;
            }
        }

        while (value > 21 && aceCount > 0) {  // Adjust for Aces if hand value exceeds 21
            value -= 10; // Count Ace as 1 instead of 11
            aceCount--;
        }

        return value;
    }
}

