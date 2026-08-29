package com.blackjack.game;

import java.util.Arrays;
import java.util.Collections;

public class Deck {
    private final Card[] cards;
    private int cardIndex = 0;

    public Deck() {
        cards = Cards.getDeck();
        shuffle();
    }

    public Card dealCard() {
        return cards[cardIndex++];
    }

    public void shuffle() {
        Collections.shuffle(Arrays.asList(cards));
        cardIndex = 0;
    }

    public void reset() {
        shuffle();
    }
}
