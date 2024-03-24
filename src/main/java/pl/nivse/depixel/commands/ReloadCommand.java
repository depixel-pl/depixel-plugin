package pl.nivse.depixel.commands;

import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;

@Route(name = "reload")
public class ReloadCommand {
    @Permission("depixel.reload")
    void execute(Player sender){
        Depixel.getPlugin().reloadConfig();
        sender.sendMessage(Depixel.getMiniMessage().deserialize(Utils.toMiniMessage("&aPrzeładowano plugin!")));
    }
}
