package ch.k42.scriptrunner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Thomas on 03.03.14.
 *
 * This shows how a plugin can use scriptrunners API
 */
public class ExamplePlugin extends JavaPlugin {
    private static final String PLUGIN = "ScriptRunner";
    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        ScriptPlugin plugin = (ScriptPlugin) pm.getPlugin(PLUGIN);

        if (!pm.isPluginEnabled(plugin))
            pm.enablePlugin(plugin);

    }
}
