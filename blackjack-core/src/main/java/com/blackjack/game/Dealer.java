package com.blackjack.game;

public class Dealer extends Player {

    public Dealer() {
        super(0);
    }

    public void playTurn(Deck deck) {
        System.out.println("Dealer's starting hand: " + getHand()+ " (Value: " + Cards.getHandValue(getHand()) + ")");
        int hitCount = 0;
        while (Cards.getHandValue(getHand()) < 17) {
            hit(deck);
            hitCount++;
            if (Cards.getHandValue(getHand()) > 21) {
                System.out.println("Dealer busts with hand after hitting " + hitCount + " times: " + getHand()+ " (Value: " + Cards.getHandValue(getHand()) + ")");
                return;
            }
        }
        System.out.println("Dealer hits " + hitCount + " time(s): " + getHand()  + " (Value: " + Cards.getHandValue(getHand()) + ")");
    }
}
