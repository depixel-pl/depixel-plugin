package pl.nivse.depixel;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyFormat;

public class Utils {

    public static String toMiniMessage(String input) {
        char[] legacyColors = "0123456789abcdef".toCharArray();
        char[] legacyFormatting = "klmno".toCharArray();
        String output = input;

        // Replace RGB color codes with MiniMessage color format
        output = output.replaceAll("&#([A-Fa-f0-9]{6})", "<#$1>");

        // Replace legacy color codes with MiniMessage color format
        for (char x : legacyColors) {
            LegacyFormat format = LegacyComponentSerializer.parseChar(x);
            TextColor color = format.color();
            output = output.replaceAll("[&]" + x, "<" + color.asHexString() + ">");
        }

        // Replace legacy formatting codes with MiniMessage formatting
        for (char x : legacyFormatting) {
            LegacyFormat format = LegacyComponentSerializer.parseChar(x);
            TextDecoration decoration = format.decoration();
            output = output.replaceAll("[&]" + x, "<" + decoration.name().toLowerCase() + ">");
        }

        output = output.replaceAll("&r", "<reset>");
        return output;
    }

    public static Component parseMessage(String input) {
        return Depixel.getMiniMessage().deserialize(Utils.toMiniMessage(input));
    }

}
