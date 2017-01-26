package scraper.ui;

import java.io.Console;

public class CredentialsReader {
    private Console console;

    public CredentialsReader() {
        console = System.console();
        checkConsole();
    }

    private void checkConsole() {
        if (console == null) {
            System.out.println("Couldn't get Console instance - unable to ask for credentials. Try running " +
                    "the application from shell or edit CredentialsReader to provide the data other way.");
            System.exit(0);
        }
    }

    public String askForLogin() {
        console.printf("Login: ");
        return console.readLine();
    }

    public String askForPassword() {
        return new String(console.readPassword("Password: "));
    }
}
