package entity;

public class HappyHour {
    private int turnsRemaining;
    private boolean activatedThisGame;

    public HappyHour() {
        this.turnsRemaining = 0;
        this.activatedThisGame = false;
    }

    public boolean canActivate() {
        return !activatedThisGame && turnsRemaining == 0;
    }

    public void activate() {
        if (!canActivate()) {
            throw new IllegalStateException("Happy Hour can only be activated once per game.");
        }
        this.activatedThisGame = true;
        this.turnsRemaining = 2; // Lasts for the current turn and the next
    }

    public boolean isActive() {
        return turnsRemaining > 0;
    }

    public void decrementTurn() {
        if (turnsRemaining > 0) {
            turnsRemaining--;
        }
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    public int applyDiscount(int basePriceCents) {
        if (!isActive()) {
            return basePriceCents;
        }
        // Orders taken while active get 20% off locked at order time
        return (int) Math.round(basePriceCents * 0.80);
    }
}