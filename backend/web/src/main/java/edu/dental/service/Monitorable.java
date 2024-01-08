package edu.dental.service;

public abstract class Monitorable {

    private final int key;

    private long lastAction;

    protected Monitorable(int key) {
        this.key = key;
    }

    public long lastAction() {
        return lastAction;
    }

    public void updateLastAction() {
        this.lastAction = System.currentTimeMillis();
    }

    public int getKey() {
        return key;
    }
}
