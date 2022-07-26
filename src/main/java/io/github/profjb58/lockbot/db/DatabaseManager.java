package io.github.profjb58.lockbot.db;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.profjb58.lockbot.LockBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final List<Connection> shardDBConnections;

    public DatabaseManager(LockBot instance) {
        setupConnections(instance.getShardManager(), instance.getEnvConfig());
        initDB();
    }

    private void setupConnections(ShardManager shardManager, Dotenv envConfig) {
        for (var shard : shardManager.getShards()) {
            try {
                var connection = DriverManager.getConnection("jdbc:mysql://" +
                        envConfig.get("DB_HOST"), envConfig.get("DB_USERNAME"), envConfig.get("DB_PASSWORD")
                );
                shardDBConnections.add(shard.getShardInfo().getShardId(), connection);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to initialize SQL database for shard with id: " + shard.getShardInfo().getShardId()
                        + ". Check the credentials and host are correct", e);
            }
        }
    }

    private void initDB() {
        try {
            getDBConnection().createStatement().execute("CREATE DATABASE IF NOT EXISTS lockbot;");
        } catch (SQLException e) {
            LockBot.LOGGER.error("Failed to setup and create a database schema and associated tables", e);
        }
    }

    @Nonnull
    public Connection getDBConnection() {
        return shardDBConnections.get(0);
    }

    @Nonnull
    public Connection getShardDBConnection(JDA jda) {
        return shardDBConnections.get(jda.getShardInfo().getShardId());
    }

    static {
        shardDBConnections = new ArrayList<>();
    }
}
