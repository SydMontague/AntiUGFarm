package com.syd.antiugfarm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiUGFarm extends JavaPlugin
{
    public final PermissionsSystem permSys = new PermissionsSystem(this);
    public static Server server;
    public static AntiUGFarm plugin;
    public static FileConfiguration config;
    public final ServerBlockListener BlockListener = new ServerBlockListener(this);
    public static YamlConfiguration skilltree;
    public final static Logger log = Logger.getLogger("Minecraft");

    public void onEnable()
    {
        PluginDescriptionFile pdffile = this.getDescription();
        server = getServer();

        // initializing config.yml
        config = getConfig();

        String version = config.getString("version");
        if (version != pdffile.getVersion())
        {
            if (config.get("version") == null)
            {
                List<Integer> checked = new ArrayList<Integer>();
                checked.add(6); // sapling
                checked.add(37); // yellow flower
                checked.add(38); // red flower
                checked.add(59); // seeds
                checked.add(81); // Cacti
                checked.add(83); // sugar cane
                checked.add(104); // Pumpkin seeds
                checked.add(105); // Melon seeds
                config.addDefault("checked", checked);

                List<Integer> ignored = new ArrayList<Integer>();
                ignored.add(0); // AIR
                ignored.add(18); // Leaves
                ignored.add(20); // Glass
                config.addDefault("ignored", ignored);

                config.addDefault("string.notallowed", "You are not allowed to create a farm here!");
                config.addDefault("version", "0.3");
            }
            else if (version != pdffile.getVersion())
            {
                config.set("version", "0.3");
            }
            config.options().copyDefaults(true);
            saveConfig();
        }
        // end of config.yml

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.BLOCK_PLACE, BlockListener, Event.Priority.High, this);
        
        log.info("[AntiUGFarm] " + pdffile.getName() + " " + pdffile.getVersion() + " enabled");

        permSys.start();
    }

    public void onDisable()
    {
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[AntiUGFarm] " + pdfFile.getName() + " disabled");
    }
}
