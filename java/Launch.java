import config.BotConfig;
import listeners.Administration;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Launch {
    public static void main(String[] args) {
        try {
            JDA api = new JDABuilder(AccountType.BOT)
                    .setToken(BotConfig.getProperty("bot_token"))
                    .setAutoReconnect(true)
                    .setActivity(Activity.watching("anime"))
                    .build().awaitReady();
            api.addEventListener(new Administration());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
