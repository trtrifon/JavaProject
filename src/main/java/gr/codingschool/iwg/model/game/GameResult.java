package gr.codingschool.iwg.model.game;

/**
 * 
 */
public class GameResult {
    private boolean result;
    private int newBalance;
    private int oldBalance;
    private boolean enoughBalance;

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(int newBalance) {
        this.newBalance = newBalance;
    }

    public int getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(int oldBalance) {
        this.oldBalance = oldBalance;
    }

    public boolean getEnoughBalance() {
        return enoughBalance;
    }

    public void setEnoughBalance(boolean enoughBalance) {
        this.enoughBalance = enoughBalance;
    }
}
