package pl.nivse.depixel.commands;

import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;
import pl.nivse.depixel.services.UserService;

@Route(name = "helpop", aliases = {"hop"})
public class HelpOpCommand {
    @Execute
    void execute(Player sender, @Joiner @Name("wiadomość") String message){
        UserService playerService = Depixel.getUserService();
        message = Depixel.getPlugin().getConfig().getString("helpOp.format")
                .replace("{sender}", playerService.getPlayer(sender.getPlayer()).getDisplayName())
                .replace("{message}", message);
        Component helpOpMessage = Depixel.getMiniMessage().deserialize(Utils.toMiniMessage(message));
        Bukkit.broadcast(helpOpMessage, "depixel.helpOp");
        sender.sendMessage(helpOpMessage);
    }
}
