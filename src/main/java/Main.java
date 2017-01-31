import org.xml.sax.SAXException;
import scraper.mbank.Scraper;
import ui.AccountsPrinter;
import scraper.BankAccount;
import scraper.BankScraper;
import ui.CredentialsReader;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String args[]) throws IOException, SAXException {
        CredentialsReader credentialsReader = new CredentialsReader();
        BankScraper mBankScraper = new Scraper(credentialsReader.askForLogin(), credentialsReader.askForPassword());
        List<BankAccount> accounts =  mBankScraper.getAccounts();
        AccountsPrinter.printSorted(accounts);
    }
}
