package pl.nivse.depixel.objects;

import org.bukkit.entity.Player;

public class Invite {
    private final Player sender;
    private final Player recipient;
    private final Group group;

    public Invite(Player sender, Player recipient, Group group) {
        this.sender = sender;
        this.recipient = recipient;
        this.group = group;
    }

    // Getter methods
    public Player getSender() {
        return sender;
    }

    public Player getRecipient() {
        return recipient;
    }

    public Group getGroup() {
        return group;
    }
}
