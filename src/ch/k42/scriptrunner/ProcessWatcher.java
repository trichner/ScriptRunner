package ch.k42.scriptrunner;

import org.bukkit.Bukkit;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by Thomas on 03.03.14.
 */
public class ProcessWatcher extends Thread{

    private final Process process;
    private List<ProcessListener> listeners = new Vector<>();

    public ProcessWatcher(Process process) {
        this.process = process;
    }

    public ProcessWatcher(Process process,String file){
        this.process = process;
    }

    public void addListener(ProcessListener listener){
        listeners.add(listener);
    }

    public List<ProcessListener> getListeners() {
        return listeners;
    }

    @Override
    public void run() {
        try {
            int ret = process.waitFor();
            List<ProcessListener> notifyList = Collections.unmodifiableList(listeners);
            for(ProcessListener l : notifyList){
                l.onProcessEnd(ret);
            }
        } catch (InterruptedException e) {
            Bukkit.getLogger().warning("Couldn't wait for process, was interrupted: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
