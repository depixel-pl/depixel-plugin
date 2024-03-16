package pl.nivse.depixel.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.nivse.depixel.Depixel;

public class PlayerLeave implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Depixel.depixelPlayers.remove(player);
    }
}
