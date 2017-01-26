import scraper.AccountsPrinter;
import scraper.account.BankAccount;
import scraper.BankScraper;
import scraper.MBankScraper;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String args[]) throws IOException {
        BankScraper mBankScraper = new MBankScraper();
        List<BankAccount> accounts =  mBankScraper.getAccounts();
        AccountsPrinter accountsPrinter = new AccountsPrinter();
        accountsPrinter.printSorted(accounts);
    }
}
