package ui;

import scraper.BankAccount;

import java.util.List;

import static java.util.Collections.sort;

public class AccountsPrinter {
    public static void printSorted(List<BankAccount> accounts) {
        sort(accounts);
        for (BankAccount account : accounts)
            System.out.println(account.toString());
    }
}
