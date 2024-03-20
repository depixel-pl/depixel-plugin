package pl.nivse.depixel.objects;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;

public class DepixelPlayer {
    private final Player player;
    private DepixelPlayer lastMessenger;

    public DepixelPlayer(Player player) {
        this.player = player;
        this.lastMessenger = null;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the last player who sent a message to this player.
     *
     * @return The last player who messaged this player.
     */
    public DepixelPlayer getLastMessenger(){
        return lastMessenger;
    }

    /**
     * Sets the last player who sent a message to this player.
     *
     * @param lastMessenger The last player who messaged this player.
     */
    public void setLastMessenger(DepixelPlayer lastMessenger) {
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
