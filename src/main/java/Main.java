import scraper.ui.AccountsPrinter;
import scraper.account.BankAccount;
import scraper.BankScraper;
import scraper.MBankScraper;
import scraper.ui.CredentialsReader;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String args[]) throws IOException {
        CredentialsReader credentialsReader = new CredentialsReader();
        BankScraper mBankScraper = new MBankScraper(credentialsReader.askForLogin(), credentialsReader.askForPassword());
        List<BankAccount> accounts =  mBankScraper.getAccounts();
        AccountsPrinter.printSorted(accounts);
    }
}
