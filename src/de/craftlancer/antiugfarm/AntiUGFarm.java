package de.craftlancer.antiugfarm;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiUGFarm extends JavaPlugin
{
    private FileConfiguration config;
    private FarmListener listener = new FarmListener(this);
    private Set<Material> checked = new HashSet<Material>();
    private Set<Material> ignored = new HashSet<Material>();
    private String notAllowed;
    
    @Override
    public void onEnable()
    {
        if (!new File(getDataFolder(), "config.yml").exists())
            saveDefaultConfig();
        
        config = getConfig();
        load();
        
        getServer().getPluginManager().registerEvents(listener, this);
        
        try
        {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e)
        {
        }
    }
    
    @Override
    public void onDisable()
    {
        getServer().getScheduler().cancelTasks(this);
    }
    
    private void load()
    {
        List<String> c = config.getStringList("checked");
        List<String> i = config.getStringList("ignored");
        notAllowed = config.getString("string.notallowed", "You are not allowed to create a farm here!");
        
        for (String str : c)
        {
            Material mat = Material.matchMaterial(str);
            if (mat != null)
                checked.add(mat);
            else
                getLogger().warning("Invalid Material: " + str);
        }
        
        for (String str : i)
        {
            Material mat = Material.matchMaterial(str);
            if (mat != null)
                ignored.add(mat);
            else
                getLogger().warning("Invalid Material: " + str);
        }
    }
    
    public Set<Material> getIgnored()
    {
        return ignored;
    }
    
    public Set<Material> getChecked()
    {
        return checked;
    }
    
    public String getNotAllowedStr()
    {
        return notAllowed;
    }
}
