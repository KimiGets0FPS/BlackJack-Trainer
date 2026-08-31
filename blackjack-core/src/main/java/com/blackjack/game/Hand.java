package com.blackjack.game;

import com.blackjack.Kattio;

import java.util.ArrayList;

public class Hand {
    private final Deck deck = new Deck();

    public static void main(String[] args) {

    }

    public void playHand(Player user, int numPlayers, Kattio io) {  // Game logic for a single hand of blackjack
        Player[] players = createPlayersAndDeal(user, numPlayers);  // Dealer -> User -> Other Players
        Dealer dealer = (Dealer) players[0];

        handleWager(players[1], io);

        System.out.println("Dealer's top card: " + dealer.getHand().get(0) +
                " (Value: " + dealer.getHand().get(0).getRankValue() +
                ")\nYour cards: " + players[1].getHand()+ " (Value: " +
                Cards.getHandValue(players[1].getHand()) + ")"
        );

        // Insurance
        boolean insurance = false;
        if (Cards.getHandValue(players[1].getHand()) != 21 && dealer.getHand().get(0).getRankValue() == 11) {  // Offers insurance if user doesn't have blackjack
            insurance = handleInsurance(players[1], io);
        }
        if (Cards.getHandValue(dealer.getHand()) != 21) {  // Dealer doesn't have blackjack
            // TODO : what if dealer has a face card as an up card, but has blackjack
            if (dealer.getHand().get(0).getRankValue() == 11) {
                System.out.println("Dealer does not have blackjack");
            }
            // Split
            if (!handleSplit(players[1], io)) {
                // User takes their normal turn when they don't split
                playUserTurn(players[1], io);
            }

            // Other players take their turns

            for (int i = 2; i < players.length; i++) {
                System.out.println(players[i] + "'s cards: " + players[i].getHand() + " (Value: " +
                        Cards.getHandValue(players[i].getHand()) + ")");
//                playUserTurn(players[i], io);
            }

            // Dealer take their turn
            dealer.playTurn(deck);
        }
        else {  // Dealer automatically wins if they have blackjack
            System.out.println("Dealer has blackjack!");

            if (insurance) {  // User wins insurance if they took it
                players[1].winInsurance();
                System.out.println("You win insurance!");
            }
        }

        // Player Hand Results
        displayResults(players, dealer);

        System.out.println("You have $" + players[1].getMoney() + " remaining.");
    }

    public void resetDeck() {
        deck.reset();
    }

    public void handleWager(Player player, Kattio io) {
        player.setWager(0);
        System.out.print("Enter your wager (You have $" + player.getMoney() + "): ");
        double wager = io.nextDouble();
        while (!player.placeBet(wager)) {
            System.out.print("Invalid wager. Enter a valid wager (You have $" + player.getMoney() + "): ");
            wager = io.nextDouble();
        }
        player.setWager(wager);
    }

    public Player[] createPlayersAndDeal(Player user, int numPlayers) {
        Player[] players = new Player[numPlayers];
        players[0] = new Dealer();
        players[1] = user;

        for (int i=2; i<numPlayers; i++) {
            players[i] = new Player(i);
        }

        for (Player player : players) {
            player.clearHand();
        }

        for (int i=0; i<2; i++) {
            for (Player player : players) {
                player.hit(deck);
            }
        }
        return players;
    }

    public boolean handleInsurance(Player player, Kattio io) {
        System.out.print("Do you want insurance (y/n): ");
        if (!io.next().equalsIgnoreCase("y")) {
            return false;
        }
        if (!player.placeInsuranceBet()) {
            System.out.println("You don't have enough money for insurance.");
            return false;
        }
        return true;
    }

    public boolean handleSplit(Player player, Kattio io) {  // Recursion?
        if (!canSplit(player)) {
            return false;
        }
        System.out.println("Do you want to split (y/n): ");
        if (!io.next().equalsIgnoreCase("y")) {
            return false;
        }
        // Create two new hands for the player

        // TODO: Still needs fixing

        Player splitHand1 = new Player(player.getID());
        Player splitHand2 = new Player(player.getID());


        splitHand1.addCard(player.getHand().get(0));
        splitHand1.addCard(player.getHand().get(1));


        splitHand1.hit(deck);
        System.out.println("First split hand: " + splitHand1.getHand() + " (Value: " +
                Cards.getHandValue(splitHand1.getHand()) + ")");
        handleSplit(splitHand1, io);
        playUserTurn(splitHand1, io);


        splitHand2.hit(deck);
        System.out.println("Second split hand: " + splitHand2.getHand() + " (Value: " +
                Cards.getHandValue(splitHand2.getHand()) + ")");
        handleSplit(splitHand2, io);
        playUserTurn(splitHand2, io);

        return true;
    }

    private boolean canSplit(Player player) {
        return player.getHand().size() == 2 &&
                player.getHand().get(0).getRankValue() == player.getHand().get(1).getRankValue();
    }

    public void playUserTurn(Player player, Kattio io) {
        int userCardValue = Cards.getHandValue(player.getHand());
        boolean userBust = false;
        boolean doubleDown = false;
        boolean blackjack = player.getHand().size() == 2 && userCardValue == 21;

        while (!userBust && !doubleDown && !blackjack) {
            System.out.print("What do you want to do?\n1. Hit\n2. Double\n3. Stand\nEnter your choice (1-3): ");
            int in = io.nextInt();

            if (in == 1) {  // Hit
                player.hit(deck);
                userCardValue = Cards.getHandValue(player.getHand());
                System.out.println("Your cards: " + player.getHand() + " (Value: " + userCardValue + ")");
            }
            else if (in == 2) {  // Double
                if (player.getHand().size() == 2) {  // Double down is only allowed on the first two cards
                    player.hit(deck);
                    userCardValue = Cards.getHandValue(player.getHand());
                    System.out.println("Your cards: " + player.getHand() + " (Value: " + userCardValue + ")");
                    player.placeBet(player.getWager());
                    doubleDown = true;
                }
                else {  // Already hit at least once, cannot double down
                    System.out.println("You can only double down on your first two cards.");
                }
            }
            else if (in == 3) {  // Stand
                break;
            }

            else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }

            if (userCardValue > 21) {
                userBust = true;
            }
        }
    }

    public void displayResults(Player[] players, Dealer dealer) {
        Result userResult = determineResult(players[1], dealer);
        switch (userResult) {
            case WIN -> {
                System.out.println("You win!");
                players[1].winBet();
            }
            case LOSE -> {
                System.out.println("Dealer wins!");
                players[1].loseBet();
            }
            case PUSH -> {
                System.out.println("You push against the dealer");
                players[1].pushBet();
            }
            case BUST -> {
                System.out.println("You bust! Dealer wins!");
                players[1].loseBet();
            }
            case BLACKJACK -> {
                System.out.println("You have blackjack! You win!");
                players[1].winBlackjack();
            }
        }
        if (players.length > 2) {
            ArrayList<Player> winners = new ArrayList<>(), losers = new ArrayList<>(), pushed = new ArrayList<>();
            for (int i = 2; i < players.length; i++) {
                Result result = determineResult(players[i], dealer);

                switch (result) {
                    case WIN -> {
                        winners.add(players[i]);
                        players[i].winBet();
                    }
                    case LOSE, BUST -> {
                        losers.add(players[i]);
                        players[i].loseBet();
                    }
                    case PUSH -> {
                        pushed.add(players[i]);
                        players[i].pushBet();
                    }
                    case BLACKJACK -> {
                        winners.add(players[i]);
                        players[i].winBlackjack();
                    }
                }
            }
            System.out.println("Won against dealer: " + winners +
                    "\nLost against dealer: " + losers +
                    "\nPushed against dealer: " + pushed
            );
        }
    }

    private enum Result {
        WIN, LOSE, PUSH, BUST, BLACKJACK
    }

    private static Result determineResult(Player player, Dealer dealer) {
        int playerHandValue = Cards.getHandValue(player.getHand());
        int dealerHandValue = Cards.getHandValue(dealer.getHand());

        boolean playerBlackjack = playerHandValue == 21 && player.getHand().size() == 2;
        boolean dealerBlackjack = dealerHandValue == 21 && dealer.getHand().size() == 2;

        if (playerBlackjack && dealerBlackjack) {  // Both dealer and player have blackjack
            return Result.PUSH;
        }

        else if (!playerBlackjack && dealerBlackjack) {  // Dealer has blackjack
            return Result.LOSE;
        }
        else if (playerBlackjack) {  // Player has blackjack
            return Result.BLACKJACK;
        }

        // Neither dealer nor player has blackjack
        if (playerHandValue > 21) {
            return Result.BUST;
        }
        else if (dealerHandValue > 21) {
            return Result.WIN;
        }

        else if (playerHandValue > dealerHandValue) {
            return Result.WIN;
        }
        else if (playerHandValue < dealerHandValue) {
            return Result.LOSE;
        }

        return Result.PUSH;
    }
}
