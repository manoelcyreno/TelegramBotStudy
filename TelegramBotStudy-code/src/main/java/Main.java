import java.util.logging.Level;
import java.util.logging.Logger;

import com.mashape.unirest.http.exceptions.UnirestException;

public class Main {

    public static void main(String[] args) {
        TelegramBot tb = new TelegramBot("555593219:AAHaMiMhxjdhUfn3lCpjGhjEB4qsWdQ-gmw");
        try {
            tb.run();
        } catch (UnirestException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}