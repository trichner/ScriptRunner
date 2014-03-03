package ch.k42.scriptrunner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;


/**
 * Created by Thomas on 02.03.14.
 */
public class SimpleShCommand implements CommandExecutor {

    private Plugin plugin;

    protected SimpleShCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length<1){
            commandSender.sendMessage("too few arguments.");
        }
        try {
            Process p = Minions.spawnProcess(plugin.getDataFolder(),args);
            ProcessWatcher pw = new ProcessWatcher(p,plugin.getDataFolder().getAbsolutePath() + File.separator + "lastlog.txt");
            pw.addListener(new ProcessListener() {
                @Override
                public void onProcessEnd(int ret) {
                    commandSender.sendMessage("Process exited with value: " + ret);
                }
            });
            pw.start();
            return true;
        } catch (IOException e) {
            Minions.sendException(commandSender,e);
        } catch (IllegalArgumentException e){
            Minions.sendException(commandSender, e);
        }
        return false;
    }
}
