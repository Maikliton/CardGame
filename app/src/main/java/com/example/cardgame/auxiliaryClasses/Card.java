package com.example.cardgame.auxiliaryClasses;

import java.util.ArrayList;
import java.util.Collections;

public class Card {
    public static boolean flip = false;
    private String f_color; // Есть 5 вариантов расцветки: green, yellow, red, blue & Wild
    private String b_color;
    private String f_type; // Есть 5 типов: default, draw, reverse, skip, flip
    private String b_type;
    private int f_number; // И любая цифра от 1 до 9
    private int b_number;
    public int hash; // Для определения идентичности (Иногда возникала ситуация в которой карты по значения были одинаковы, и из-за механики игры она не продолжалась)

    public Card(String f_color, String f_type, int f_number, String b_color, String b_type, int b_number, int hash) {
        this.f_color = f_color;
        this.f_type = f_type;
        this.b_color = b_color;
        this.b_type = b_type;
        this.f_number = f_number;
        this.b_number = b_number;
        this.hash = hash;
    }

    // Даёт значение в зависимости от того перевёрнуты ли карты
    public String getColorNow() {
        return flip ? b_color : f_color;
    }

    // Даёт значение в зависимости от того перевёрнуты ли карты
    public String getTypeNow() {
        return flip ? b_type : f_type;
    }

    // Проверяет возможно ли бросить карту на другую карту
    public boolean equals(Card card) {
        if(flip) {
            return card.b_color.equals(this.b_color) || this.b_color.equals("wild") || card.b_color.equals("wild") || card.b_number == this.b_number;
        }
        return card.f_color.equals(this.f_color) || this.f_color.equals("wild") || card.f_color.equals("wild") || card.f_number == this.f_number;
    }

    @Override
    public String toString() {
        return "Card{" +
                "f_color='" + f_color + '\'' +
                ", b_color='" + b_color + '\'' +
                ", f_type='" + f_type + '\'' +
                ", b_type='" + b_type + '\'' +
                ", f_number=" + f_number +
                ", b_number=" + b_number +
                '}';
    }

    // Даёт значение в зависимости от того перевёрнуты ли карты
    private int getNumberNow() {
        return flip ? b_number : f_number;
    }

    public String getNumberString() {
        return getTypeNow().equals("draw") ? "+" + getNumberNow() : String.valueOf(getNumberNow());
    }

    // Проверяет идентичность карты другой карте
    public boolean identical(Card card) {
        return this.hash == card.hash;
    }
}

@Deprecated
class Deck {
    private static ArrayList <Card> deck = new ArrayList<>();

    static void addToDeck(Card card) {
        deck.add(card);
    }

    static void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public static ArrayList getDeck() {
        return deck;
    }
}