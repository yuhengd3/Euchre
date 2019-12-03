package com.example.euchre;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck {
    private List<Card> d;

//    public Deck() {
//        for (Suit s : Suit.values()) {
//            for (Rank r : Rank.values()) {
//                d.add(new Card(s, r));
//            }
//        }
//        Collections.shuffle(d);
//    }

    public Deck(List<Card> cards) {
        d = cards;
    }

    public List<Card> getAllCards() {
        return d;
    }
}

enum Suit {
    DIAMOND,
    HEART,
    CLUB,
    SPADE
}

enum Rank {
    NINE,
    TEN,
    JACK,
    Queen,
    KING,
    ACE
}

class Card {
    private Suit s;
    private Rank r;
    private int value;

    public Card(Suit s, Rank r) {
        this.s = s;
        this.r = r;
        switch (r) {
            case NINE:
                value = 9;
                break;
            case TEN:
                value = 10;
                break;
            case JACK:
                value = 11;
                break;
            case Queen:
                value = 12;
                break;
            case KING:
                value = 13;
                break;
            case ACE:
                value = 14;
                break;
        }

    }

    public Suit getSuit() {
        return s;
    }

    public Rank getRank() {
        return r;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return r.toString() + " Of " + s.toString();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Card)) {
            return false;
        }
        Card card = (Card) o;
        return s.equals(card.getSuit()) && r.equals(card.getRank());
    }
}