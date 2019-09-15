package embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class EmbedMessage {

    private EmbedBuilder embed = new EmbedBuilder();

    public MessageEmbed sendTitleTextColor(String title, String text, Color color) {
        embed.setColor(color);
        embed.setTitle(title);
        embed.setDescription(text);
        return embed.build();
    }

}
