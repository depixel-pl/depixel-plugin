package pl.nivse.depixel.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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
import pl.nivse.depixel.objects.Group;
import pl.nivse.depixel.services.UserService;

import java.util.HashMap;

public class PlayerChat implements Listener {
    MiniMessage miniMessage = Depixel.getMiniMessage();
    HashMap<Permission, TagResolver> formattingPermissions = new HashMap<>();
    private final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();
    UserService userService = Depixel.getUserService();

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

    //Parse string without checking formatting permissions
    private Component parseString(Player player, String s){
        s = PlaceholderAPI.setPlaceholders(player, s);
        s = Utils.toMiniMessage(s);

        return miniMessage.deserialize(s);
    }

    //Parse string while checking formatting permissions
    private Component parseStringWithPermissions(Player player, String s){
        TagResolver.Builder tagResolver = TagResolver.builder();
        formattingPermissions.forEach((permission, resolver) -> {
            if(player.hasPermission(permission)){
                tagResolver.resolver(resolver);
            }
        });

        if(player.hasPermission("depixel.legacy")){
            s = Utils.toMiniMessage(s);
        }

        MiniMessage parser = MiniMessage.builder().tags(tagResolver.build()).build();
        return parser.deserialize(s);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerChatEvent(AsyncChatEvent e){
        Component displayName = parseString(e.getPlayer(), Depixel.getPlugin().getConfig().getString("chat.displayName").replace("{player}", e.getPlayer().getName()));
        Component delimiter = parseString(e.getPlayer(), Depixel.getPlugin().getConfig().getString("chat.delimiter"));
        Component message = parseStringWithPermissions(e.getPlayer(), plainTextComponentSerializer.serialize(e.originalMessage()));
        if(userService.getPlayer(e.getPlayer()).getCurrentGroup() != null){
            Group group = userService.getPlayer(e.getPlayer()).getCurrentGroup();
            group.getAudience().sendMessage(Component.empty().append(parseString(e.getPlayer(), "<yellow>[</yellow>" + group.getName() + "<yellow>]</yellow>")).append(Component.text(" ")).append(displayName).append(Component.text(" ")).append(delimiter).append(Component.text(" ")).append(message));
            e.setCancelled(true);
            return;
        }
        e.renderer(((player, sourceDisplayName, originalMessage, audience) -> Component.empty().append(displayName).append(Component.text(" ")).append(delimiter).append(Component.text(" ")).append(message)));
    }
}
