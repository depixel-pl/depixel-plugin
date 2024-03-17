package pl.nivse.depixel.services;

import org.bukkit.entity.Player;
import pl.nivse.depixel.objects.DepixelPlayer;

import java.util.HashMap;
import java.util.Map;

public class DepixelPlayerService {
    private final Map<Player, DepixelPlayer> players = new HashMap<>();

    public void addPlayer(Player player){
        this.players.put(player, new DepixelPlayer(player));
    }

    public DepixelPlayer getPlayer(Player player){
        return this.players.get(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }
}
