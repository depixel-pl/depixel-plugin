package pl.nivse.depixel.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.object.DepixelPlayer;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        DepixelPlayer depixelPlayer = new DepixelPlayer(player);
        Depixel.depixelPlayers.put(player, depixelPlayer);
    }
}
