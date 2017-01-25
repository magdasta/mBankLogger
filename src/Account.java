import org.json.JSONObject;

class Account implements Comparable{
    private String name;
    private String number;
    private String balance;

    Account(JSONObject jsonAccountData) {
        name = jsonAccountData.getString("ProductName");
        number = jsonAccountData.getString("AccountNumber");
        balance = jsonAccountData.getString("Balance");
    }

    @Override
    public String toString() {
        return name + " " + number + ":\t" + balance;
    }

    @Override
    public int compareTo(Object other) {
        if (other == null)
            throw new NullPointerException();
        if (other instanceof Account)
            return compare(this.balance, ((Account) other).balance);
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
