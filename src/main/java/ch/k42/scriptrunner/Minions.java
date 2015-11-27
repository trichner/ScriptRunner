package ch.k42.scriptrunner;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thomas on 03.03.14.
 */
public class Minions {
    public static final boolean validateFilename(String script){
        Pattern pattern = Pattern.compile("[A-Za-z\\.\\-_0-9]+");
        Matcher matcher = pattern.matcher(script);
        return matcher.matches();
    }

    public static final Process spawnProcess(File directory, String[] args) throws IOException {
        boolean existing = false;
        for(File f : directory.listFiles()){
            if(f.getName().equals(args[0])){
                existing = true;
                break;
            }
        }
        if(!existing) throw new IllegalArgumentException("Script not found in directory.");

        if(Minions.validateFilename(args[0])){
            if(isWindows()){
                String[] tmp = new String[args.length+2];
                tmp[0] = "cmd";
                tmp[1] = "/c";
                System.arraycopy(args,0,tmp,2,args.length);
                args = tmp;
            }else if(isUnix()){ // FIXME this only works with bash...
                String[] tmp = new String[args.length+1];
                tmp[0] = "bash";
                System.arraycopy(args,0,tmp,1,args.length);
                args = tmp;
            }
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.directory(directory);
            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(new File(directory.getAbsolutePath() + File.separator + "lastlog.txt")));
            return pb.start();
        }else{
            throw new IllegalArgumentException("The script has an illegal filename. Allowed characters: [A-Za-z\\.\\-_0-9]");
        }

    }

    public static final void sendException(CommandSender sender,Exception e){
        sender.sendMessage(ChatColor.DARK_RED + "FAIL: " + e.getMessage());
    }

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }
}
