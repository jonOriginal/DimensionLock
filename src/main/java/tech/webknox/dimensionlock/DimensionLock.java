package tech.webknox.dimensionlock;

import org.bukkit.plugin.java.JavaPlugin;
import tech.webknox.dimensionlock.Util.ConfigInterface;
import tech.webknox.dimensionlock.Util.ConfigManager;
import tech.webknox.dimensionlock.commands.DefaultCommandHandler;
import tech.webknox.dimensionlock.commands.EndHandler;
import tech.webknox.dimensionlock.commands.NetherHandler;

public final class DimensionLock extends JavaPlugin {

    private ConfigInterface configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        NetherHandler netherCommand = new NetherHandler(this, configManager);
        EndHandler endCommand = new EndHandler(this, configManager);
        DefaultCommandHandler defaultCommand = new DefaultCommandHandler(this, configManager);
    }

    @Override
    public void onDisable() {

    }
}
