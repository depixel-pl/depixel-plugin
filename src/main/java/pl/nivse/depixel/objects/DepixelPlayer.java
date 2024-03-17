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
    public DepixelPlayer getLastMessenger(){
        return lastMessenger;
    }

    public void setLastMessenger(DepixelPlayer lastMessenger) {
        this.lastMessenger = lastMessenger;
    }

    public String getDisplayName() {
        String displayName = Depixel.getPlugin().getConfig().getString("chat.displayName")
                .replace("{player}", player.getName());
        displayName = PlaceholderAPI.setPlaceholders(player, displayName);
        displayName = Utils.toMiniMessage(displayName);

        return displayName;
    }
}
