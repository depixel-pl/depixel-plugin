package pl.nivse.depixel.services;

import org.bukkit.entity.Player;
import pl.nivse.depixel.objects.DepixelPlayer;

import java.util.HashMap;
import java.util.Map;

public class DepixelPlayerService {
    private final Map<Player, DepixelPlayer> players = new HashMap<>();

    /**
     * Adds a player to the player map.
     *
     * @param player The player to be added.
     */
    public void addPlayer(Player player) {
        this.players.put(player, new DepixelPlayer(player));
    }

    /**
     * Retrieves the DepixelPlayer instance associated with player.
     *
     * @param player DepixelPlayer instance of given player to retrieve
     * @return The DepixelPlayer instance of the given player.
     */
    public DepixelPlayer getPlayer(Player player) {
        return this.players.get(player);
    }

    /**
     * Removes the player from the player map.
     *
     * @param player The player to be removed.
     */
    public void removePlayer(Player player) {
        this.players.remove(player);
    }

}
