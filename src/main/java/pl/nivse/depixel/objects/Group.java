package pl.nivse.depixel.objects;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Group {

    private String name;
    private Player leader;
    private final Collection<Player> members = new ArrayList<>();
    private final Collection<Invite> invites = new ArrayList<>();

    public Group(String name, Player leader) {
        this.leader = leader;
        this.name = name;
    }

    public Player getLeader(){
        return leader;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeader(Player newLeader){
        leader = newLeader;
    }

    public Collection<Player> getMembers() {
        return members;
    }

    public Audience getAudience(){
        return Audience.audience(members);
    }

    public boolean isLeader(Player player){
        return player == leader;
    }

    public boolean isMember(Player player){
        return members.contains(player);
    }
    public void addMember(Player member) {
        members.add(member);
    }

    public void removeMember(Player player) {
        members.remove(player);
    }

    public void addInvite(Invite invite){
        invites.add(invite);
    }

    public void removeInvite(Invite invite){
        invites.remove(invite);
    }

    public Invite findInvite(Player recipient){
        for (Invite invite: invites) {
            if(invite.getRecipient() == recipient){
                return invite;
            } else {
                return null;
            }
        }
        return null;
    }
}

