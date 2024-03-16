package pl.nivse.depixel;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.paper.LitePaperAdventureFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import pl.nivse.depixel.commands.HelpOpCommand;
import pl.nivse.depixel.commands.MsgCommand;
import pl.nivse.depixel.commands.ReplyCommand;
import pl.nivse.depixel.listeners.PlayerChat;
import pl.nivse.depixel.listeners.PlayerJoin;
import pl.nivse.depixel.listeners.PlayerLeave;
import pl.nivse.depixel.object.DepixelPlayer;

import java.util.HashMap;
import java.util.Map;

public final class Depixel extends JavaPlugin {
    private static JavaPlugin plugin;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private LiteCommands liteCommands;
    public static Map<Player, DepixelPlayer> depixelPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();
        registerEvents();

        this.liteCommands = LitePaperAdventureFactory.builder(this.getServer(), "depixel")
                .commandInstance(new MsgCommand())
                .commandInstance(new ReplyCommand())
                .commandInstance(new HelpOpCommand())
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("&cTa komenda jest dostępna tylko dla graczy!"))
                .argument(Player.class, new BukkitPlayerArgument<>(plugin.getServer(), "&cNie znaleziono gracza."))
                .register();
    }

    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerChat(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoin(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerLeave(), plugin);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static MiniMessage getMiniMessage(){
        return miniMessage;
    }
}
