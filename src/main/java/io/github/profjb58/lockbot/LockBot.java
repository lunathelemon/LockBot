package io.github.profjb58.lockbot;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.profjb58.lockbot.db.DatabaseManager;
import io.github.profjb58.lockbot.listeners.CommandListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

public class LockBot {

    public static final Logger LOGGER = LogManager.getLogger(LockBot.class);

    private final Dotenv envConfig;
    private final ShardManager shardManager;
    private final DatabaseManager dbManager;

    public LockBot() throws LoginException {
        envConfig = Dotenv.configure().load();

        // Split resources into different concurrent shards
        shardManager = DefaultShardManagerBuilder.createLight(envConfig.get("BOT_TOKEN"))
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("over the locked and denied"))
                .addEventListeners(new CommandListener(this))
                .build();
        dbManager = new DatabaseManager(this);
    }

    public static void main(String[] args) {
        try {
            new LockBot();
        } catch (LoginException e) {
            LOGGER.error("Failed to log into Discord", e);
        }
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public DatabaseManager getDBManager() {
        return dbManager;
    }

    public Dotenv getEnvConfig() {
        return envConfig;
    }
}