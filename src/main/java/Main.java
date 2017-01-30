import org.xml.sax.SAXException;
import ui.AccountsPrinter;
import account.BankAccount;
import scraper.generic.BankScraper;
import scraper.MBankScraper;
import ui.CredentialsReader;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String args[]) throws IOException, SAXException {
        CredentialsReader credentialsReader = new CredentialsReader();
        BankScraper mBankScraper = new MBankScraper(credentialsReader.askForLogin(), credentialsReader.askForPassword());
        List<BankAccount> accounts =  mBankScraper.getAccounts();
        AccountsPrinter.printSorted(accounts);
    }
}
