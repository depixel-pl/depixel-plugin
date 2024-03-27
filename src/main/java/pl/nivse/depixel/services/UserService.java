package pl.nivse.depixel.services;

import org.bukkit.entity.Player;
import pl.nivse.depixel.objects.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<Player, User> users = new HashMap<>();

    /**
     * Adds a player to the player map.
     *
     * @param player The player to be added.
     */
    public void addPlayer(Player player) {
        this.users.put(player, new User(player));
    }

    /**
     * Retrieves the User instance associated with player.
     *
     * @param player The player whose User instance is to be retrieved.
     * @return The User instance of the given player.
     */
    public User getPlayer(Player player) {
        return this.users.get(player);
    }

    /**
     * Removes the player from the player map.
     *
     * @param player The player to be removed.
     */
    public void removePlayer(Player player) {
        this.users.remove(player);
    }

}
