package account;

public class BankAccount implements Comparable{
    protected String number;
    protected String balance;

    @Override
    public String toString() {
        return number + ":\t" + balance;
    }

    @Override
    public int compareTo(Object other) {
        if (other == null)
            throw new NullPointerException();
        if (other instanceof BankAccount)
            return compare(this.balance, ((BankAccount) other).balance);
        else
            throw new ClassCastException();
    }

    private int compare(String balance1, String balance2) {
        if (balance1.length() < balance2.length())
            return -1;
        else if (balance1.length() > balance2.length())
            return 1;
        else
            return compareSameNumberOfDigits(balance1, balance2);
    }

    private int compareSameNumberOfDigits(String balance1, String balance2) {
        for (int i = 0; i < balance1.length(); ++i)
            if (balance1.charAt(i) < balance2.charAt(i))
                return -1;
            else if (balance1.charAt(i) < balance2.charAt(i))
                return 1;
        return 0;
    }

}
