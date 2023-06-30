package tech.webknox.dimensionlock.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.webknox.dimensionlock.Util.ConfigInterface;

import java.text.MessageFormat;
import java.util.List;

public class DefaultCommandHandler implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;

    private final ConfigInterface configManager;

    public DefaultCommandHandler(JavaPlugin plugin, ConfigInterface configManager) {
        this.plugin = plugin;
        this.configManager = configManager;

        plugin.getCommand("dimensionlock").setExecutor(this);
        plugin.getCommand("dimensionlock").setTabCompleter(this);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessageFormat.format(
                    "Operator Bypass is currently {0}",
                    configManager.isOpBypassEnabled() ?
                            ChatColor.GREEN + "enabled" :
                            ChatColor.RED + "disabled"
            ));
            return true;
        }

        switch (args[0]) {
            case "allowop" -> {
                sender.sendMessage(ChatColor.GREEN + "Operator bypass is now enabled");
                configManager.setOpBypassEnabled(true);
                return true;
            }
            case "disallowop" -> {
                sender.sendMessage(ChatColor.RED + "Operator bypass is now disabled");
                configManager.setOpBypassEnabled(false);
                return true;
            }
            case "reload" -> {
                sender.sendMessage("DimensionLock Config reloaded");
                configManager.ReloadConfig();
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of("allowop", "disallowop", "reload");
    }
}
