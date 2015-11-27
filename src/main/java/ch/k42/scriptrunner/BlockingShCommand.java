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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.IOException;


/**
 * Created by Thomas on 02.03.14.
 */
public class BlockingShCommand implements CommandExecutor {

    private Plugin plugin;

    protected BlockingShCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length<1){
            commandSender.sendMessage("too few arguments.");
        }
        try {
            Process p = Minions.spawnProcess(plugin.getDataFolder(),args);
            commandSender.sendMessage("Process exited with: " +p.waitFor());
            return true;
        } catch (IOException e) {
            Minions.sendException(commandSender,e);
        } catch (IllegalArgumentException e){
            Minions.sendException(commandSender, e);
        } catch (InterruptedException e) {
            Minions.sendException(commandSender, e);
        }
        return false;
    }
}
