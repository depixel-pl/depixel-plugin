package pl.nivse.depixel.tasks;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AutoMessageTask extends BukkitRunnable {
    private static final ThreadLocalRandom trl = ThreadLocalRandom.current();
    @Override
    public void run() {
        int rnd = trl.nextInt(Depixel.getPlugin().getConfig().getStringList("autoMessage.messages").size());
        Audience audience = Audience.audience(Bukkit.getOnlinePlayers());

        audience.sendMessage(Utils.parseMessage(Depixel.getPlugin().getConfig().getStringList("autoMessage.messages").get(rnd)));
    }
}
