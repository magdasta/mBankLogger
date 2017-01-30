package scraper;

import org.xml.sax.SAXException;
import account.BankAccount;

import java.io.IOException;
import java.util.List;

public interface BankScraper {
    List<BankAccount> getAccounts() throws IOException, SAXException;
}
