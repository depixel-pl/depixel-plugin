package pl.nivse.depixel.commands;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;
import pl.nivse.depixel.objects.User;

@Route(name = "ignoreMentions")
public class IgnoreMentionsCommand {
    @Execute
    public void execute(Player sender) {
        User user = Depixel.getUserService().getPlayer(sender);
        if (user.ignoresMentions()) {
            user.setIgnoresMentions(false);
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("mentions.toggledFalse")));
        } else {
            user.setIgnoresMentions(true);
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("mentions.toggledTrue")));
        }
    }
}
