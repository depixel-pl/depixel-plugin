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
    private boolean ignoresMentions;
    private final Collection<Invite> invites = new ArrayList<>();

    public User(Player player) {
        this.player = player;
        this.group = null;
        this.lastMessenger = null;
        this.ignoresMentions = false;
    }

    public boolean ignoresMentions() {
        return ignoresMentions;
    }

    public void setIgnoresMentions(boolean ignoresMentions) {
        this.ignoresMentions = ignoresMentions;
    }

    public Player getPlayer() {
        return player;
    }

    public Group getCurrentGroup(){
        return group;
    }

    public void setCurrentGroup(Group group){
        this.group = group;
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
