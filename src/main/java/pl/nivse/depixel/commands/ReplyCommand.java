package pl.nivse.depixel.commands;

import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.route.Route;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;
import pl.nivse.depixel.objects.User;
import dev.rollczi.litecommands.command.execute.Execute;
import pl.nivse.depixel.services.UserService;

@Route(name = "reply", aliases = {"r"})
public class ReplyCommand {
    @Execute
    void execute(Server server, Player sender, @Joiner @Name("wiadomość") String message){
        UserService playerService = Depixel.getUserService();

        User messageSender = playerService.getPlayer(sender);
        User messageReciever = messageSender.getLastMessenger();
        if(messageReciever == null){
            messageSender.getPlayer().sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.playerNotOnline")));
            return;
        }
        String recieverDisplayName = messageReciever.getDisplayName();
        String senderDisplayName = messageSender.getDisplayName();

        String format = Depixel.getPlugin().getConfig().getString("msg.format");

        format = format.replace("{sender}", senderDisplayName).replace("{reciever}", recieverDisplayName).replace("{message}", message);

        Component finalMessage = Utils.parseMessage(format);
        messageSender.getPlayer().sendMessage(finalMessage);
        messageReciever.getPlayer().sendMessage(finalMessage);
        messageReciever.setLastMessenger(playerService.getPlayer(sender));
    }
}
