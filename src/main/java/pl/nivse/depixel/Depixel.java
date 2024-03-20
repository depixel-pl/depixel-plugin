package pl.nivse.depixel;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.paper.LitePaperAdventureFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import pl.nivse.depixel.commands.BroadcastCommand;
import pl.nivse.depixel.commands.HelpOpCommand;
import pl.nivse.depixel.commands.MsgCommand;
import pl.nivse.depixel.commands.ReplyCommand;
import pl.nivse.depixel.database.DatabaseManager;
import pl.nivse.depixel.listeners.PlayerChat;
import pl.nivse.depixel.listeners.PlayerJoin;
import pl.nivse.depixel.listeners.PlayerLeave;
import pl.nivse.depixel.services.DepixelPlayerService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public final class Depixel extends JavaPlugin {
    private static JavaPlugin plugin;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private LiteCommands liteCommands;
    private static DepixelPlayerService DepixelPlayerService;
    private static DatabaseManager databaseManager;
    private HikariDataSource dataSource = new HikariDataSource();

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();
        DepixelPlayerService = new DepixelPlayerService();
        registerCommands();
        registerEvents();
        try {
            databaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerChat(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoin(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerLeave(), plugin);
    }

    public void registerCommands() {
        this.liteCommands = LitePaperAdventureFactory.builder(this.getServer(), "depixel")
                .commandInstance(new MsgCommand())
                .commandInstance(new ReplyCommand())
                .commandInstance(new HelpOpCommand())
                .commandInstance(new BroadcastCommand())
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("&cTa komenda jest dostępna tylko dla graczy!"))
                .argument(Player.class, new BukkitPlayerArgument<>(plugin.getServer(), "&cNie znaleziono gracza."))
                .register();
    }
    public void databaseConnection() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getConfig().getString("database.url"));
        config.setUsername(getConfig().getString("database.username"));
        config.setPassword(getConfig().getString("database.password"));
        dataSource = new HikariDataSource(config);
    }
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    @Override
    public void onDisable() {
        if(databaseManager != null){
            databaseManager.close();
            getLogger().log(Level.INFO, "Disconnected from DB.");
        }
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static MiniMessage getMiniMessage(){
        return miniMessage;
    }

    public static DepixelPlayerService getDepixelPlayerService() {
        return DepixelPlayerService;
    }
}