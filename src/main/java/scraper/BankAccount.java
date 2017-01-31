package scraper;

import java.math.BigDecimal;

public class BankAccount implements Comparable{
    protected String number;
    protected BigDecimal balance;
    protected BigDecimal availableBalance;
    protected String currency;

    @Override
    public String toString() {
        return number + ":\t" + balance.toString();
    }

    @Override
    public int compareTo(Object other) {
        if (other == null)
            throw new NullPointerException();
        if (other instanceof BankAccount)
            return this.balance.compareTo(((BankAccount) other).balance);
        else
            throw new ClassCastException();
    }
}
