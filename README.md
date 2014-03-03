ScriptRunner
============

This is a Bukkit Minecraft plugin that bridges bash scripts and batch files with minecraft.

The plugin adds two new commands:

`/sh [script] [args]`
starts a script asynchronously, you will be notified when the script exits. Use this command for scripts that might take some time.


`/bsh [script] [args]`
starts a script synchronously. This will block the servers main thread until the script exits. If the script isn't very
short, this might timeout the whole server. Use it wisely.


The output of your scripts is appended to a file named `lastlog.txt` in the plugins folder. All scripts you want to run have to be in the plugins data folder i.e. `plugins/ScriptRunner/` and need appropriate file permissions. Only scripts in this folder can be executed and script naming is whitelisted. Valid characters in a script filename are: A-Z,a-z,0-9, '-', '_', '.'
