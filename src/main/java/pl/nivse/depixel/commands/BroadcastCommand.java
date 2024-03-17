package pl.nivse.depixel.commands;

import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;

@Route(name = "broadcast")
public class BroadcastCommand {
    @Permission("depixel.broadcast")
    void execute(@Joiner @Name("wiadomość") String message){
        Audience audience = Audience.audience(Bukkit.getServer().getOnlinePlayers());
        message = Depixel.getPlugin().getConfig().getString("broadcast.format")
                .replace("{message}", message);

        audience.sendMessage(Depixel.getMiniMessage().deserialize(Utils.toMiniMessage(message)));
        audience.sendActionBar(Depixel.getMiniMessage().deserialize(Utils.toMiniMessage(message)));
    }
}
