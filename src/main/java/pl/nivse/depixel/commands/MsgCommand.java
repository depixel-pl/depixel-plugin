package pl.nivse.depixel.commands;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;
import pl.nivse.depixel.objects.User;
import pl.nivse.depixel.services.UserService;

@Route(name =  "msg")
public class MsgCommand {
    @Execute
    void execute(Server server, Player sender, @Arg @Name("gracz") Player reciever, @Joiner @Name("wiadomość") String message){
        UserService playerService = Depixel.getUserService();

        User messageReciever = playerService.getPlayer(reciever);
        User messageSender = playerService.getPlayer(sender);
        String recieverDisplayName = messageReciever.getDisplayName();
        String senderDisplayName = messageSender.getDisplayName();

        String format = Depixel.getPlugin().getConfig().getString("msg.format");

        format = format.replace("{sender}", senderDisplayName).replace("{reciever}", recieverDisplayName).replace("{message}", message);
        format = Utils.toMiniMessage(format);

        Component finalMessage = Depixel.getMiniMessage().deserialize(format);
        messageSender.getPlayer().sendMessage(finalMessage);
        messageReciever.getPlayer().sendMessage(finalMessage);
        messageReciever.setLastMessenger(playerService.getPlayer(sender));
    }
}
