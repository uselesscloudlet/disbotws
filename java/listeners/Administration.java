package listeners;

import config.BotConfig;
import embed.EmbedMessage;
import entities.Player;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import repositories.PlayerRepository;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Administration extends ListenerAdapter {

    private EmbedMessage embed = new EmbedMessage();
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;
        String content = event.getMessage().getContentRaw().replaceAll("\\s{2,}", " ").trim();
        String command = content.split(" ")[0].toLowerCase();

        if (event.getAuthor().getIdLong() == 214404737708064769L) {
            if (command.equals("+exit"))
                System.exit(1);
        }

        if (command.equals("+help")) {
            event.getChannel().sendMessage("**Доступные команды: **" + "\n" +
                    "*+help* -" + " `выдаёт всю информацию по доступным командам бота`" + "\n" +
                    "*+info* -" + " `информация о команде`" + "\n" +
                    "*+addrecruit @НИКНЕЙМ* -" + " `добавляет нового рекрута в команду`" + "\n" +
                    "*+rankup @НИКНЕЙМ* -" + " `повышает игрока до следующего звания`" + "\n" +
                    "*+rankdown @НИКНЕЙМ* -" + " `понижает игрока до предыдущего звания`" + "\n" +
                    "*+delrole @НИКНЕЙМ @РОЛЬ* -" + " `снимает роль с человека`" + "\n" +
                    "*+clear* -" + " `полностью очищает чат`" + "\n").queue(
                    (channel) -> channel.delete().queueAfter(10, TimeUnit.MINUTES)
            );
        }

        if (command.equals("+info")) {
            event.getChannel().sendMessage("**Тут инфа про команду**").queue();
        }

        boolean access = false;

        for (Role role : Objects.requireNonNull(event.getMember()).getRoles()) {
            if (role.getName().equals("Head Officer"))
                access = true;
            if (role.getName().equals("Senior Officer"))
                access = true;
        }

        if (command.equals("+delrole")) {
            if (!access) {
                event.getChannel().sendMessage("**У Вас не достаточно прав!**").queue();
                return;
            }

            if (event.getMessage().getMentionedMembers().isEmpty()){
                event.getChannel().sendMessage("**Вы не ввели имя!**").queue();
                return;
            }

            if (event.getMessage().getMentionedRoles().isEmpty()){
                event.getChannel().sendMessage("**Вы не ввели роль!**").queue();
                return;
            }

            if (!event.getMessage().getMentionedMembers().get(0).getRoles().contains(event.getMessage().getMentionedRoles().get(0))){
                event.getChannel().sendMessage("**Роль не найдена у данного человека.**").queue();
                return;
            }

            if (event.getMessage().getMentionedRoles().get(0).getName().contains("Head Officer")){
                event.getChannel().sendMessage("**Вы не можете снять роль Head Officer / Senior Officer!**").queue();
                return;
            }

            if (event.getMessage().getMentionedRoles().get(0).getName().contains("Senior Officer")){
                event.getChannel().sendMessage("**Вы не можете снять роль Head Officer / Senior Officer!**").queue();
                return;
            }

            Member member = event.getMessage().getMentionedMembers().get(0);
            Role role = event.getMessage().getMentionedRoles().get(0);
            member.getGuild().removeRoleFromMember(member, role).queue();
            event.getChannel().sendMessage("**Вы успешно удалили роль у указанного человека.**").queue();

            String text = "Никнейм: **" + member.getEffectiveName() + "**\n" +
                    "Дата снятия: `" + LocalDate.now() + "`\n" +
                    "Роль: `" + role.getName() + "`\n";

            Color color = new Color(220, 20, 60);

            event.getGuild().getTextChannelById(598568017907023922L).sendMessage(embed.sendTitleTextColor("Снятие роли", text, color)).queue();
        }

        if (command.equals("+addrecruit")) {
            if (!access) {
                event.getChannel().sendMessage("**У Вас не достаточно прав!**").queue();
                return;
            }

            if (event.getMessage().getMentionedMembers().size() == 0) {
                event.getChannel().sendMessage("Вы забыли упомянуть человека!").queue();
                return;
            }

            Role role = event.getGuild().getRolesByName("Recruit", true).get(0);
            Member member = event.getMessage().getMentionedMembers().get(0);

            member.getGuild().addRoleToMember(member, role).queue();

            String text = "Никнейм: **" + member.getEffectiveName() + "**\n" +
                    "Дата принятия: `" + LocalDate.now() + "`\n" +
                    "Дата окончания: `" + LocalDate.now().plusDays(14L) + "`";

            Color color = new Color(0, 153, 255);

            event.getGuild().getTextChannelById(598568017907023922L).sendMessage(embed.sendTitleTextColor("Новый рекрут", text, color)).queue();

            Player player = new Player();
            player.setDiscord_id(member.getIdLong());
            player.setNickname(member.getEffectiveName());
            player.setJoined_clan(LocalDate.now());

            new PlayerRepository().savePlayer(player);

        }

        if (command.equals("+clear")) {
            if (!access) {
                event.getChannel().sendMessage("**У Вас не достаточно прав!**").queue();
                return;
            }

            event.getChannel().getIterableHistory().takeAsync(10).thenAccept(event.getChannel()::purgeMessages);
        }


        if (command.equals("+rankup")) {
            if (!access) {
                event.getChannel().sendMessage("**У Вас не достаточно прав!**").queue();
                return;
            }

            if (event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessage("**Вы не ввели имя!**").queue();
                return;
            }

            String rolesName = BotConfig.getProperty("roles_list").trim();
            String[] splittedRoles = rolesName.split(",");

            String reqRoles = event.getMessage().getMentionedMembers().get(0).getRoles().toString();
            int maxInt = splittedRoles.length - 1;
            String maxIntSt = Integer.toString(maxInt);

            int neededInt = 0;
            for (int i=0; i <= maxInt; i++) {
                if (reqRoles.contains(splittedRoles[i])) {
                    neededInt = i;
                }
            }

            String needIntSt = Integer.toString(neededInt);
            if (maxIntSt.equals(needIntSt)) {
                event.getChannel().sendMessage(event.getMessage().getMentionedMembers().get(0).getAsMention() + " уже имеет максимальный ранг!").queue();
            } else {
                Member member = event.getMessage().getMentionedMembers().get(0);

                String oldRoleName = splittedRoles[neededInt];
                Role oldrole = event.getGuild().getRolesByName(oldRoleName, true).get(0);
                member.getGuild().removeRoleFromMember(member, oldrole).queue();

                String newRoleName = splittedRoles[neededInt+1];
                Role newRole = event.getGuild().getRolesByName(newRoleName, true).get(0);
                member.getGuild().addRoleToMember(member, newRole).queue();

                event.getChannel().sendMessage(event.getMessage().getMentionedMembers().get(0).getAsMention() + " был повышен!").queue();

                String text = "Никнейм: **" + member.getEffectiveName() + "**\n" +
                        "Старая роль: `" + oldrole.getName() + "`\n" +
                        "Новая роль: `" + newRole.getName() + "`\n";

                Color color = new Color(190, 42, 220);

                event.getGuild().getTextChannelById(598568017907023922L).sendMessage(embed.sendTitleTextColor("Повышение ранга", text, color)).queue();
            }
        }

        if (command.equals("+rankdown")) {
            if (!access) {
                event.getChannel().sendMessage("**У Вас не достаточно прав!**").queue();
                return;
            }

            if(event.getMessage().getMentionedMembers().isEmpty()){
                event.getChannel().sendMessage("**Вы не ввели имя!**").queue();
                return;
            }

            String rolesName = BotConfig.getProperty("roles_list").trim();
            String[] splittedRoles = rolesName.split(",");


            String reqRoles = event.getMessage().getMentionedMembers().get(0).getRoles().toString();
            int maxInt = splittedRoles.length - 1;

            int neededInt = 0;
            for (int i=0; i <= maxInt; i++) {
                if (reqRoles.contains(splittedRoles[i])) {
                    neededInt = i;
                }
            }

            String needIntSt = Integer.toString(neededInt);
            if (needIntSt.equals("0")){
                event.getChannel().sendMessage(event.getMessage().getMentionedMembers().get(0).getAsMention() + " уже имеет минимальный ранг!").queue();
            } else {
                Member member = event.getMessage().getMentionedMembers().get(0);

                String oldRoleName = splittedRoles[neededInt];
                Role oldRole = event.getGuild().getRolesByName(oldRoleName, true).get(0);
                member.getGuild().removeRoleFromMember(member, oldRole).queue();

                String newRoleName = splittedRoles[neededInt-1];
                Role newRole = event.getGuild().getRolesByName(newRoleName, true).get(0);
                member.getGuild().addRoleToMember(member, newRole).queue();

                event.getChannel().sendMessage(event.getMessage().getMentionedMembers().get(0).getAsMention() + " был понижен!").queue();

                String text = "Никнейм: **" + member.getEffectiveName() + "**\n" +
                        "Старая роль: `" + oldRole.getName() + "`\n" +
                        "Новая роль: `" + newRole.getName() + "`\n";

                Color color = new Color(5, 219, 220);

                event.getGuild().getTextChannelById(598568017907023922L).sendMessage(embed.sendTitleTextColor("Понижение ранга", text, color)).queue();
            }
        }
    }
}