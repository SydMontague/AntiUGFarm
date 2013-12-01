package de.craftlancer.antiugfarm;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class FarmListener implements Listener
{
    private AntiUGFarm plugin;
    
    public FarmListener(AntiUGFarm instance)
    {
        plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getPlayer().hasPermission("antiugfarm.ignore"))
            return;
        
        if (plugin.getIgnored().contains(event.getBlock().getType()))
            return;
        
        Block block = event.getBlock();
        int maxHeight = block.getWorld().getMaxHeight();
        
        if (plugin.getChecked().contains(event.getBlock().getType()))
        {
            for (Block b = block.getRelative(0, 1, 0); b.getY() < maxHeight - 1; b = b.getRelative(0, 1, 0))
            {
                if (!plugin.getIgnored().contains(b.getType()))
                {
                    event.getPlayer().sendMessage(plugin.getNotAllowedStr());
                    event.setCancelled(true);
                    return;
                }
            }
        }
        else
        {
            for (Block b = block.getRelative(0, -1, 0); b.getY() > 0; b = b.getRelative(0, -1, 0))
            {
                if (plugin.getChecked().contains(b.getType()))
                {
                    event.getPlayer().sendMessage(plugin.getNotAllowedStr());
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
