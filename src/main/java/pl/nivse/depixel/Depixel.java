package pl.nivse.depixel;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.paper.LitePaperAdventureFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import pl.nivse.depixel.commands.*;
import pl.nivse.depixel.database.DatabaseManager;
import pl.nivse.depixel.listeners.PlayerChat;
import pl.nivse.depixel.listeners.PlayerJoin;
import pl.nivse.depixel.listeners.PlayerLeave;
import pl.nivse.depixel.services.UserService;
import pl.nivse.depixel.services.GroupService;
import pl.nivse.depixel.tasks.AutoMessageTask;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public final class Depixel extends JavaPlugin {
    private static JavaPlugin plugin;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private LiteCommands liteCommands;
    private static UserService userService;
    private static GroupService groupService;
    private static DatabaseManager databaseManager;
    private HikariDataSource dataSource = new HikariDataSource();

    public static GroupService getGroupService() {
        return groupService;
    }

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();
        userService = new UserService();
        groupService = new GroupService();
        registerCommands();
        registerEvents();
        scheduleTasks();
        try {
            databaseConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void scheduleTasks(){
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new AutoMessageTask() ,20L, (getConfig().getInt("autoMessage.delay") * 20L));
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
                .commandInstance(new ReloadCommand())
                .commandInstance(new GroupCommand())
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("&cTa komenda jest dostępna tylko dla graczy!"))
                .argument(Player.class, new BukkitPlayerArgument<>(plugin.getServer(), "&cNie znaleziono gracza."))
                .permissionHandler((commandSender, liteInvocation, requiredPermissions) -> commandSender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage("&cNie masz permisji by wykonać tą komendę."))))
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

    public static UserService getUserService() {
        return userService;
    }
}