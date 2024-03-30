package pl.nivse.depixel.commands;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.entity.Player;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;
import pl.nivse.depixel.objects.Group;
import pl.nivse.depixel.objects.Invite;
import pl.nivse.depixel.services.GroupService;
import pl.nivse.depixel.services.UserService;

import java.util.Arrays;

@Route(name = "group")
public class GroupCommand {
    UserService userService = Depixel.getUserService();
    GroupService groupService = Depixel.getGroupService();
    Group group;

    @Execute(route = "create")
    void create(Player sender, @Arg @Name("nazwa") String groupName){
        if (groupService.getGroup(groupName) != null){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.groupAlreadyExists")));
            return;
        }
        group = new Group(groupName, sender.getUniqueId());
        group.addMember(sender);
        groupService.addGroup(groupName, group);
        sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.groupCreated").replace("{group}", group.getName())));
    }
    @Execute(route = "invite")
    void invite(Player sender, @Arg @Name("gracz") Player reciever){
        group = userService.getPlayer(sender).getCurrentGroup();

        if(userService.getPlayer(sender).getCurrentGroup() == null){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.groupNotSelected")));
            return;
        }
        if(!group.isLeader(sender)){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.notLeader").replace("{group}", group.getName())));
            return;
        }

        if(group.isMember(reciever)){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.alreadyInGroup").replace("{user}", reciever.getName())));
            return;
        }

        if(group.findInvite(reciever) != null){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.alreadyInvited").replace("{user}", reciever.getName())));
            return;
        }

        Invite invite = new Invite(sender, reciever, group);
        group.addInvite(invite);
        userService.getPlayer(reciever).addInvite(invite);

        sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.successfullyInvited").replace("{user}", reciever.getName())));
        reciever.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.invitePending").replace("{group}", group.getName())));
    }

    @Execute(route = "accept")
    void accept(Player sender, @Arg @Name("grupa") String groupName){
        group = Depixel.getGroupService().getGroup(groupName);

        Invite invite = group.findInvite(sender);
        if(invite == null){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.notInvited")));
            return;
        }
        group.removeInvite(invite);
        group.addMember(sender);
        userService.getPlayer(sender).removeInvite(invite);
        sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.inviteAccepted")));
        group.getAudience().sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.memberJoined").replace("{user}", sender.getName()).replace("{group}", group.getName())));
    }

    @Execute(route = "delete")
    void delete(Player sender){
        group = userService.getPlayer(sender).getCurrentGroup();

        if(group == null){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.groupNotSelected")));
            return;
        }

        if(!group.isLeader(sender)){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.notLeader").replace("{group}", group.getName())));
            return;
        }

        group.getAudience().sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.groupDeleted").replace("{group}", group.getName())));
        group.getMembers().forEach(player -> {
            if(userService.getPlayer(player).getCurrentGroup() == group){
                userService.getPlayer(player).setCurrentGroup(null);
                player.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.switchToGlobal")));
            }
        });
        groupService.removeGroup(group.getName());
    }

    @Execute(route = "set")
    void set(Player sender, @Arg @Name("grupa") String groupName){
        group = groupService.getGroup(groupName);
        if(group == null || !group.isMember(sender)){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.switchToGlobal")));
            return;
        }
        userService.getPlayer(sender).setCurrentGroup(group);
        sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.groupSet").replace("{group}", groupName)));
    }

    @Execute(route = "leave")
    void leave(Player sender, @Arg @Name("grupa") String groupName){
        group = groupService.getGroup(groupName);

        if(group.isLeader(sender)){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.leaderCantLeaveGroup").replace("{group}", group.getName())));
        }

        if(!group.isMember(sender)){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.groupDoesntExist")));
            return;
        }

        if(userService.getPlayer(sender).getCurrentGroup() == group){
            userService.getPlayer(sender).setCurrentGroup(null);
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.switchToGlobal")));
        }

        group.removeMember(sender);
        group.getAudience().sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.memberLeft").replace("{user}", sender.getName()).replace("{group}", group.getName())));
        sender.sendMessage(Utils.parseMessage(Utils.toMiniMessage(Depixel.getPlugin().getConfig().getString("messages.groupLeft").replace("{group}", groupName))));
    }

    @Execute(route = "kick")
    void kick(Player sender, @Arg @Name("gracz") Player kicked){
        group = userService.getPlayer(sender).getCurrentGroup();

        if(group == null){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.groupNotSelected")));
            return;
        }

        if(!group.isLeader(sender)){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.notLeader").replace("{group}", group.getName())));
            return;
        }

        if(!group.isMember(kicked)){
            sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.notInGroup").replace("{user}", kicked.getName())));
            return;
        }
        group.removeMember(kicked);

        sender.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.successfullyKicked").replace("{user}", kicked.getName())));
        kicked.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.gotKicked").replace("{group}", group.getName())));
        group.getAudience().sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getString("messages.memberGotKicked").replace("{user}", kicked.getName().replace("{group}", group.getName()))));
    }
}
