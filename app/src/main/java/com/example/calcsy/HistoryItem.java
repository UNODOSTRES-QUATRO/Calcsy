package com.example.calcsy;

/**
 * Model data untuk item riwayat kalkulasi.
 */
public class HistoryItem {
    private int id;
    private String expression;
    private String result;

    public HistoryItem(int id, String expression, String result) {
        this.id = id;
        this.expression = expression;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }
}
