package tech.webknox.dimensionlock.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.webknox.dimensionlock.Util.ConfigInterface;
import tech.webknox.dimensionlock.Util.Coordinates;

import java.text.MessageFormat;
import java.util.List;

public class EndHandler implements CommandExecutor, Listener, TabCompleter {

    private final JavaPlugin plugin;

    private final ConfigInterface configManager;

    public EndHandler(JavaPlugin plugin, ConfigInterface manager) {
        this.plugin = plugin;
        this.configManager = manager;
        plugin.getCommand("end").setExecutor(this);
        plugin.getCommand("end").setTabCompleter(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(
                    MessageFormat.format(
                            "End is currently {0}",
                            configManager.isEndEnabled() ?
                                    ChatColor.GREEN + "enabled" :
                                    ChatColor.RED + "disabled"
                    )
            );
            commandSender.sendMessage(MessageFormat.format(
                    "Eye spoofing is currently {0}",
                    configManager.isEyeSpoofEnabled() ?
                            ChatColor.GREEN + "enabled" :
                            ChatColor.RED + "disabled"
                    )
            );
            commandSender.sendMessage(MessageFormat.format(
                    "Spoof location is currently at x:{0} y:{1} z:{2}",
                    configManager.getSpoofLocation().getX(),
                    configManager.getSpoofLocation().getY(),
                    configManager.getSpoofLocation().getZ()
                    )
            );

            return true;
        }

        switch (strings[0]) {
            case "lock" -> {
                configManager.setEndEnabled(false);
                commandSender.sendMessage(ChatColor.RED + "End is now locked");
                return true;
            }
            case "unlock" -> {
                configManager.setEndEnabled(true);
                commandSender.sendMessage(ChatColor.GREEN + "End is now unlocked");
                return true;
            }
            case "spoofon" -> {
                configManager.setEyeSpoofEnabled(true);
                commandSender.sendMessage(ChatColor.GREEN + "Eye spoofing is now enabled");
                return true;
            }
            case "spoofoff" -> {
                configManager.setEyeSpoofEnabled(false);
                commandSender.sendMessage(ChatColor.RED + "Eye spoofing is now disabled");
                return true;
            }
            case "setspoof" -> {
                if (commandSender instanceof Player player) {
                    configManager.setSpoofLocation(new Coordinates(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
                    commandSender.sendMessage(ChatColor.GREEN + "Spoof location set");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of("lock", "unlock", "spoofon", "spoofoff", "setspoof");
    }

    @EventHandler
    public void onEnderEyeThrow(EntitySpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!configManager.isEyeSpoofEnabled()) {
            return;
        }

        if (event.getEntityType() != org.bukkit.entity.EntityType.ENDER_SIGNAL) {
            return;
        }

        Entity entity = event.getEntity();

        if (!(entity instanceof EnderSignal eye)) {
            return;
        }

        Coordinates coordinates = configManager.getSpoofLocation();
        Location location = new Location(eye.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ());

        eye.setTargetLocation(location);
    }

    @EventHandler
    public void onEndPortalEnter(PlayerPortalEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (configManager.isEndEnabled()) {
            return;
        }

        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            return;
        }

        if (configManager.isOpBypassEnabled() && event.getPlayer().isOp()) {
            return;
        }

        event.setCancelled(true);
    }
}
