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
    private BufferedWriter outputFile = null;

    public ProcessWatcher(Process process) {
        this.process = process;
        this.outputFile = new BufferedWriter(new OutputStreamWriter(new OutputStream() {
            @Override
            public void write(int b) throws IOException {/*do nothing, blackhole redirector */}
        }));
    }

    public ProcessWatcher(Process process,String file){
        this.process = process;
        try {
            this.outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("Couldn't find output file: " + e.getMessage());
            this.outputFile = new BufferedWriter(new OutputStreamWriter(new OutputStream() {
                @Override
                public void write(int b) throws IOException {/*do nothing, blackhole redirector */}
            }));
        }
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

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            try {
                while ( (line = br.readLine()) != null){
                    outputFile.write(line);
                }
            } catch (IOException e) {
                //e.printStackTrace(); // EAT IT!
            }
            int ret = process.waitFor();
            List<ProcessListener> notifyList = Collections.unmodifiableList(listeners);
            for(ProcessListener l : notifyList){
                l.onProcessEnd(ret);
            }
            outputFile.close();
        } catch (InterruptedException e) {
            Bukkit.getLogger().warning("Couldn't wait for process, was interrupted: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Bukkit.getLogger().warning("Couldn't close logfile: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
