package com.blackjack;

import com.blackjack.game.Hand;
import com.blackjack.game.Player;

public class Main {
    public static void main(String[] args) {
        Hand hand = new Hand();
        Player user = new Player(1);
        Kattio io = new Kattio();
        for (int i = 0; i < 10; i++) {
            hand.playHand(user, 2, io);
            hand.resetDeck();
        }
        io.close();
    }
}
