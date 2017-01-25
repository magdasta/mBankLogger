import java.io.IOException;
import mBankLogger.MBankLogger;

public class Main {
    public static void main(String args[]) throws IOException {
        MBankLogger mBankLogger = new MBankLogger();
        mBankLogger.loginAndPrintAccounts();
    }
}
