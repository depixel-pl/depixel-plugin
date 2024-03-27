package pl.nivse.depixel.objects;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;

import java.util.ArrayList;
import java.util.Collection;

public class User {
    private final Player player;
    private User lastMessenger;
    private Group group;
    private final Collection<Group> groups = new ArrayList<>();
    private final Collection<Invite> invites = new ArrayList<>();

    public User(Player player) {
        this.player = player;
        this.lastMessenger = null;
    }

    public Player getPlayer() {
        return player;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }

    public Group getCurrentGroup(){
        return group;
    }

    public void setCurrentGroup(Group group1){
        group = group1;
    }

    public void addInvite(Invite invite){
        invites.add(invite);
    }

    public void removeInvite(Invite invite){
        invites.remove(invite);
    }
    /**
     * Retrieves the last player who sent a message to this player.
     *
     * @return The last player who messaged this player.
     */
    public User getLastMessenger(){
        return lastMessenger;
    }

    /**
     * Sets the last player who sent a message to this player.
     *
     * @param lastMessenger The last player who messaged this player.
     */
    public void setLastMessenger(User lastMessenger) {
        this.lastMessenger = lastMessenger;
    }

    /**
     * Retrieves the formatted display name of the player with MiniMessage compatible format.
     *
     * @return The display name of the player for chat messages.
     */
    public String getDisplayName() {
        String displayName = Depixel.getPlugin().getConfig().getString("chat.displayName")
                .replace("{player}", player.getName());
        displayName = PlaceholderAPI.setPlaceholders(player, displayName);
        displayName = Utils.toMiniMessage(displayName);

        return displayName;
    }
}
