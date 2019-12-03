package com.example.euchre;

import java.util.List;

public class Game {
    private Card trump;
    private Player a;
    private Player b;
    private Player c;
    private Player d;

    public Game(String userA, String userB, String userC, String userD, List<Card> deck) {
        a = new Player(userA, deck.subList(0, 5));
    }

    public void setTrump(Card c) {
        trump = c;
    }
}

class Player {
    private String user;
    private int tricks;
    private List<Card> cards;

    public Player(String s, List<Card> l) {
        tricks = 0;
        user = s;
        cards = l;
    }

    public void play(Card c) {
        cards.remove(c);
    }

    public String getUser() {
        return user;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getTricks() {
        return tricks;
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    public void win() {
        tricks++;
    }
}
