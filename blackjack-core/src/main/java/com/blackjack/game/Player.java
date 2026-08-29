package com.blackjack.game;

import java.util.ArrayList;

public class Player {
    private final Integer ID;  // Dealer ID is always 0, User ID is always 1
    private final ArrayList<Card> HAND = new ArrayList<>();
    private double MONEY = 1000;
    private double WAGER = 0;
    private double INSURANCE = 0;

    public Player (int id) {
        this.ID = id;
    }

    public int getID() {
        return ID;
    }

    public ArrayList<Card> getHand() {
        return HAND;
    }

    public void addCard(Card card) {
        HAND.add(card);
    }

    @Override
    public String toString() {
        if (ID == 0) {
            return "Dealer";
        } else if (ID == 1) {
            return "You";
        }
        return "Player " + ID;
    }

    public void setWager(double wager) {
        this.WAGER = wager;
    }

    public double getMoney() {
        return MONEY;
    }

    public double getWager() { return WAGER;}

    public void hit(Deck deck) {
        addCard(deck.dealCard());
    }

    public void clearHand() {
        HAND.clear();
    }

    public boolean placeBet(double amount) {
        if (amount <= 0 || amount > MONEY) {
            return false;
        }
        WAGER += amount;
        return true;
    }

    public boolean placeInsuranceBet() {
        double amount = WAGER / 2;
        if (amount > MONEY) {
            return false;
        }
        MONEY -= amount;
        INSURANCE = amount;
        return true;
    }

    public void pushBet() {
        MONEY += WAGER;
    }

    public void winBet() {
        MONEY += WAGER;
    }

    public void winBlackjack() {
        MONEY += WAGER * 1.5;
    }

    public void winInsurance() {
        MONEY += INSURANCE * 2;
        INSURANCE = 0;
    }

    public void loseBet() {
        MONEY -= WAGER + INSURANCE;
    }
}

