/*
 * Copyright (c) 2014. Thomas Richner
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.k42.scriptrunner;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public class ScriptPlugin extends JavaPlugin
{
	private Listener listener;                  // The listener

	@Override
	public void onEnable()
	{
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        PluginCommand cmd = getCommand("sh");
        cmd.setExecutor(new SimpleShCommand(this));
        cmd = getCommand("bsh");
        cmd.setExecutor(new BlockingShCommand(this));
	}
	
	@Override
    public void onDisable()
    {
    }


    //==== API

    /**
     * Runs a script in the 'ScriptRunner' plugin folder
     * @param args script and arguments, first argument is scripts name
     * @return the scipts return value, usually 0 on success
     * @throws IOException
     * @throws InterruptedException
     */
    public int runScript(String[] args) throws IOException, InterruptedException {
        if(args.length<1){
            throw new IllegalArgumentException("SCRIPTRUNNER: too few arguments.");
        }
        Process p = Minions.spawnProcess(this.getDataFolder(),args);
        return p.waitFor();
    }

    /**
     * Starts a script and notifies the listener as soon as it exits
     * @param args script and arguments, first argument is scripts name
     * @param listener callback routine
     * @return true if the process was startet, otherwise false
     * @throws IOException
     */
    public void runAsyncScript(String[] args,ProcessListener listener) throws IOException {
        if(args.length<1){
            throw new IllegalArgumentException("SCRIPTRUNNER: too few arguments.");
        }
        Process p = Minions.spawnProcess(this.getDataFolder(), args);
        ProcessWatcher pw = new ProcessWatcher(p);
        pw.addListener(listener);
        pw.start();
    }
}
