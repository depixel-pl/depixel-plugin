package pl.nivse.depixel.commands;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;
import pl.nivse.depixel.objects.Group;
import pl.nivse.depixel.objects.Invite;
import pl.nivse.depixel.services.GroupService;
import pl.nivse.depixel.services.UserService;

import javax.annotation.Nullable;

@Route(name = "group")
public class GroupCommand {
    UserService userService = Depixel.getUserService();
    GroupService groupService = Depixel.getGroupService();
    MiniMessage miniMessage = Depixel.getMiniMessage();
    Group group;

    @Execute(route = "create")
    void create(Player sender, @Arg @Name("nazwa") String groupName){
        if (groupService.getGroup(groupName) != null){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.groupAlreadyExists"))));
            return;
        }

        group = new Group(groupName, sender);
        group.addMember(sender);
        groupService.addGroup(groupName, group);
        userService.getPlayer(sender).addGroup(group);
        sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.groupCreated").replace("{group}", group.getName()))));
    }
    @Execute(route = "invite")
    void invite(Player sender, @Arg @Name("gracz") Player reciever){
        group = userService.getPlayer(sender).getCurrentGroup();

        if(userService.getPlayer(sender).getCurrentGroup() == null){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.groupNotSelected"))));
            return;
        }
        if(!group.isLeader(sender)){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.notLeader").replace("{group}", group.getName()))));
            return;
        }

        if(group.isMember(reciever)){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.alreadyInGroup").replace("{user}", reciever.getName()))));
            return;
        }

        if(group.findInvite(reciever) != null){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.alreadyInvited").replace("{user}", reciever.getName()))));
            return;
        }

        Invite invite = new Invite(sender, reciever, group);
        group.addInvite(invite);
        userService.getPlayer(reciever).addInvite(invite);

        sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.successfullyInvited").replace("{user}", reciever.getName()))));
        reciever.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.invitePending").replace("{group}", group.getName()))));
    }

    @Execute(route = "accept")
    void accept(Player sender, @Arg @Name("grupa") String groupName){
        group = Depixel.getGroupService().getGroup(groupName);

        Invite invite = group.findInvite(sender);
        if(invite == null){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.notInvited"))));
            return;
        }
        group.removeInvite(invite);
        group.addMember(sender);
        userService.getPlayer(sender).removeInvite(invite);
        userService.getPlayer(sender).addGroup(group);
        sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.inviteAccepted"))));
        group.getAudience().sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.memberJoined")).replace("{user}", sender.getName()).replace("{group}", group.getName())));
    }

    @Execute(route = "set")
    void set(Player sender, @Arg @Name("grupa") String groupName){
        group = groupService.getGroup(groupName);
        if(!userService.getPlayer(sender).getGroups().contains(group) || group == null){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.switchToGlobal"))));
            return;
        }
        userService.getPlayer(sender).setCurrentGroup(group);
        sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.groupSet").replace("{group}", groupName))));
    }

    @Execute(route = "leave")
    void leave(Player sender, @Arg @Name("grupa") String groupName){

        group = groupService.getGroup(groupName);
        if(group.isLeader(sender)){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.leaderCantLeaveGroup").replace("{group}", group.getName()))));
        }
        if(!userService.getPlayer(sender).getGroups().contains(group)){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.groupDoesntExist"))));
            return;
        }
        if(userService.getPlayer(sender).getCurrentGroup() == group){
            userService.getPlayer(sender).setCurrentGroup(null);
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.switchToGlobal"))));
        }
        group.removeMember(sender);
        userService.getPlayer(sender).removeGroup(group);
        group.getAudience().sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.memberLeft").replace("{user}", sender.getName()).replace("{group}", group.getName()))));
        sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.groupLeft").replace("{group}", groupName))));
    }

    void kick(Player sender, @Arg @Name("gracz") Player kicked){
        if(!group.isLeader(sender)){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.notLeader").replace("{group}", group.getName()))));
            return;
        }

        if(!group.isMember(kicked)){
            sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.notInGroup").replace("{user}", kicked.getName()))));
            return;
        }

        group.removeMember(kicked);
        userService.getPlayer(sender).removeGroup(group);

        sender.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.successfullyKicked").replace("{user}", kicked.getName()))));
        kicked.sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.gotKicked").replace("{group}", group.getName()))));
        group.getAudience().sendMessage(miniMessage.deserialize(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.memberGotKicked").replace("{user}", kicked.getName().replace("{group}", group.getName())))));
    }
}
