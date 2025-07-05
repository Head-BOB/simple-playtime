package me.hunter.playtime;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Playtime extends JavaPlugin {

    @Override
    public void onEnable() {

        this.getComponentLogger().info(Component.text("PlaytimePlugin enabled!").color(NamedTextColor.GREEN));
    }

    @Override
    public void onDisable() {
        this.getComponentLogger().info(Component.text("PlaytimePlugin disabled!").color(NamedTextColor.RED));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("playtime")) {
            return false;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player player)) {

                sender.sendMessage(Component.text("From the console, you must specify a player's name.", NamedTextColor.RED));
                sender.sendMessage(Component.text("Usage: /playtime <playername>", NamedTextColor.YELLOW));
                return true;
            }
            sendPlaytime(player, player);
            return true;
        }

        if (args.length == 1) {
            String targetName = args[0];

            @SuppressWarnings("deprecation")
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);

            if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
                sender.sendMessage(Component.text("Player '" + targetName + "' not found or has never played on this server.", NamedTextColor.RED));
                return true;
            }

            sendPlaytime(sender, targetPlayer);
            return true;
        }

        sender.sendMessage(Component.text("Incorrect usage! Use: /playtime [playername]", NamedTextColor.RED));
        return true;
    }

    private void sendPlaytime(CommandSender sender, OfflinePlayer target) {
        int ticks = target.getStatistic(Statistic.PLAY_ONE_MINUTE);

        int totalSeconds = ticks / 20;
        int totalMinutes = totalSeconds / 60;
        int totalHours = totalMinutes / 60;
        int remainingMinutes = totalMinutes % 60;

        Component message;

        if (sender instanceof Player player && player.getUniqueId().equals(target.getUniqueId())) {
            message = Component.text("Your total playtime: ", NamedTextColor.GREEN)
                    .append(Component.text(totalHours + " hours " + remainingMinutes + " minutes", NamedTextColor.YELLOW));
        } else {
            String name = target.getName() != null ? target.getName() : "an unknown player";
            message = Component.text("Total playtime for " + name + ": ", NamedTextColor.GREEN)
                    .append(Component.text(totalHours + " hours " + remainingMinutes + " minutes", NamedTextColor.YELLOW));
        }

        sender.sendMessage(message);
    }
}