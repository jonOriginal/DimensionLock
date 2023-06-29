package tech.webknox.dimensionlock.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.webknox.dimensionlock.Util.ConfigInterface;

import java.text.MessageFormat;
import java.util.List;

public class NetherHandler implements CommandExecutor, Listener, TabCompleter {

    private final JavaPlugin plugin;

    private final ConfigInterface configManager;

    public NetherHandler(JavaPlugin plugin, ConfigInterface configManager) {
        this.plugin = plugin;
        this.configManager = configManager;

        plugin.getCommand("nether").setExecutor(this);
        plugin.getCommand("nether").setTabCompleter(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(
                    MessageFormat.format(
                            "Nether is currently {0}",
                            configManager.isNetherEnabled() ?
                                    ChatColor.GREEN + "enabled" :
                                    ChatColor.RED + "disabled"
                    )
            );
            return true;
        }
        switch (strings[0]) {
            case "lock":
                configManager.setNetherEnabled(false);
                commandSender.sendMessage(ChatColor.RED + "Nether is now locked");
                return true;
            case "unlock":
                configManager.setNetherEnabled(true);
                commandSender.sendMessage(ChatColor.GREEN + "Nether is now unlocked");
                return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of("lock", "unlock");
    }


    @EventHandler
    public void onPortalCreate(org.bukkit.event.world.PortalCreateEvent event) {
        if (event.isCancelled()) return;
        if (event.getReason() != PortalCreateEvent.CreateReason.FIRE) return;

        if (event.getEntity() instanceof Player player) {
            if (configManager.isOpBypassEnabled()) {
                if (player.isOp()) return;
            }
        }
        if (!configManager.isNetherEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPortal(org.bukkit.event.player.PlayerPortalEvent event) {
        if (event.isCancelled()) return;
        if (event.getCause() != org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return;
        Player player = event.getPlayer();

        if (configManager.isOpBypassEnabled()) {
            if (player.isOp()) return;
        }

        if (!configManager.isNetherEnabled()) {
            event.setCancelled(true);
        }
    }
}
