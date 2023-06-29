package tech.webknox.dimensionlock.Util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager implements ConfigInterface {

    private final Plugin plugin;

    private boolean IsEndEnabled;

    private boolean IsNetherEnabled;

    private boolean IsEyeSpoofEnabled;

    private boolean IsOpBypassEnabled;

    private Coordinates SpoofLocation;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        LoadConfig();
    }

    private void LoadConfig() {
        FileConfiguration config = plugin.getConfig();

        Object netherEnabled = config.get("nether-enabled");
        Object endEnabled = config.get("end-enabled");
        Object eyeSpoofEnabled = config.get("ender-eye-spoof");
        Object opBypassEnabled = config.get("allow-op");

        ConfigurationSection spoofLocation = config.getConfigurationSection("spoof-location");


        if (!(netherEnabled instanceof Boolean) || !(endEnabled instanceof Boolean) || !(eyeSpoofEnabled instanceof Boolean) || !(opBypassEnabled instanceof Boolean) || spoofLocation == null) {
            plugin.saveDefaultConfig();
            LoadConfig();
        } else {
            SpoofLocation = new Coordinates(
                    spoofLocation.getDouble("x"),
                    spoofLocation.getDouble("y"),
                    spoofLocation.getDouble("z")
            );
            IsNetherEnabled = (boolean) netherEnabled;
            IsEndEnabled = (boolean) endEnabled;
            IsEyeSpoofEnabled = (boolean) eyeSpoofEnabled;
            IsOpBypassEnabled = (boolean) opBypassEnabled;
        }
    }

    @Override
    public void ReloadConfig() {
        plugin.reloadConfig();
        LoadConfig();
    }

    @Override
    public boolean isEndEnabled() {
        return IsEndEnabled;
    }

    @Override
    public void setEndEnabled(boolean endEnabled) {
        IsEndEnabled = endEnabled;
        plugin.getConfig().set("end-enabled", endEnabled);
        plugin.saveConfig();
    }

    @Override
    public boolean isNetherEnabled() {
        return IsNetherEnabled;
    }

    @Override
    public void setNetherEnabled(boolean netherEnabled) {
        IsNetherEnabled = netherEnabled;
        plugin.getConfig().set("nether-enabled", netherEnabled);
        plugin.saveConfig();
    }

    @Override
    public boolean isEyeSpoofEnabled() {
        return IsEyeSpoofEnabled;
    }

    @Override
    public void setEyeSpoofEnabled(boolean eyeSpoofEnabled) {
        IsEyeSpoofEnabled = eyeSpoofEnabled;
        plugin.getConfig().set("ender-eye-spoof", eyeSpoofEnabled);
        plugin.saveConfig();
    }

    @Override
    public boolean isOpBypassEnabled() {
        return IsOpBypassEnabled;
    }

    @Override
    public void setOpBypassEnabled(boolean opBypassEnabled) {
        IsOpBypassEnabled = opBypassEnabled;
        plugin.getConfig().set("allow-op", opBypassEnabled);
        plugin.saveConfig();
    }

    private void UpdateSpoofLocationConfig(Coordinates spoofLocation) {
        ConfigurationSection configSection = plugin.getConfig().getConfigurationSection("spoof-location");
        if (configSection == null) {
            LoadConfig();
        }
        configSection.set("x", spoofLocation.getX());
        configSection.set("y", spoofLocation.getY());
        configSection.set("z", spoofLocation.getZ());
        plugin.saveConfig();
    }

    @Override
    public Coordinates getSpoofLocation() {
        return SpoofLocation;
    }

    @Override
    public void setSpoofLocation(Coordinates spoofLocation) {
        SpoofLocation = spoofLocation;
        UpdateSpoofLocationConfig(spoofLocation);
    }
}
