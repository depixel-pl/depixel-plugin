package pl.nivse.depixel.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import pl.nivse.depixel.Depixel;
import pl.nivse.depixel.Utils;

import java.util.HashMap;

public class PlayerChat implements Listener {
    MiniMessage miniMessage = Depixel.getMiniMessage();
    HashMap<Permission, TagResolver> formattingPermissions = new HashMap<>();
    private final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

    public PlayerChat(){
        formattingPermissions.put(new Permission("depixel.color"), StandardTags.color());
        formattingPermissions.put(new Permission("depixel.bold"), StandardTags.decorations(TextDecoration.BOLD));
        formattingPermissions.put(new Permission("depixel.italic"), StandardTags.decorations(TextDecoration.ITALIC));
        formattingPermissions.put(new Permission("depixel.underlined"), StandardTags.decorations(TextDecoration.UNDERLINED));
        formattingPermissions.put(new Permission("depixel.strikethrough"), StandardTags.decorations(TextDecoration.STRIKETHROUGH));
        formattingPermissions.put(new Permission("depixel.obfuscated"), StandardTags.decorations(TextDecoration.OBFUSCATED));
        formattingPermissions.put(new Permission("depixel.gradient"), StandardTags.gradient());
        formattingPermissions.put(new Permission("depixel.rainbow"), StandardTags.rainbow());
        formattingPermissions.put(new Permission("depixel.hover"), StandardTags.hoverEvent());
        formattingPermissions.put(new Permission("depixel.click"), StandardTags.clickEvent());
    }

    private Component parseDisplayName(Player player, String displayName){

        displayName = displayName.replace("{player}", player.getName());
        displayName = PlaceholderAPI.setPlaceholders(player, displayName);
        displayName = Utils.toMiniMessage(displayName);

        return miniMessage.deserialize(displayName);
    }

    private Component parseMessage(Player player, String message){

        TagResolver.Builder tagResolver = TagResolver.builder();
        formattingPermissions.forEach((permission, resolver) -> {
            if(player.hasPermission(permission)){
                tagResolver.resolver(resolver);
            }
        });

        if(player.hasPermission("depixel.legacy")){
            message = Utils.toMiniMessage(message);
        }

        MiniMessage parser = MiniMessage.builder().tags(tagResolver.build()).build();
        return parser.deserialize(message);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerChatEvent(AsyncChatEvent e){
        e.renderer(((player, sourceDisplayName, originalMessage, audience) -> {
            Component displayName = parseDisplayName(player, Depixel.getPlugin().getConfig().getString("chat.displayName"));
            Component delimiter = parseDisplayName(player, Depixel.getPlugin().getConfig().getString("chat.delimiter"));
            Component message = parseMessage(player, plainTextComponentSerializer.serialize(originalMessage));

            Component renderedMessage = Component.empty().append(displayName).append(Component.text(" ")).append(delimiter).append(Component.text(" ")).append(message);
            return renderedMessage;
        }));
    }
}
