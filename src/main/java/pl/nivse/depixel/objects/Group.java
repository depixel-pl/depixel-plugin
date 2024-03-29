package pl.nivse.depixel.objects;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class Group {

    private String name;
    private UUID leader;
    private final Collection<UUID> membersUUID = new ArrayList<>();
    private final Collection<Invite> invites = new ArrayList<>();

    public Group(String name, UUID leader) {
        this.leader = leader;
        this.name = name;
    }

    public UUID getLeader(){
        return leader;
    }

    public Collection<Player> getMembers(){
        Collection<Player> temp = new ArrayList<>();
        for (UUID uuid : membersUUID) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null) temp.add(player);
        }
        return temp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeader(Player newLeader){
        leader = newLeader.getUniqueId();
    }


    public Audience getAudience(){
        return Audience.audience(getMembers());
    }

    public boolean isLeader(Player member){
        return member.getUniqueId() == leader;
    }

    public boolean isMember(Player member){
        return membersUUID.contains(member.getUniqueId());
    }
    public void addMember(Player member) {
        membersUUID.add(member.getUniqueId());
    }

    public void removeMember(Player member) {
        membersUUID.remove(member.getUniqueId());
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

