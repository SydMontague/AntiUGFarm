package com.syd.antiugfarm;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ServerBlockListener implements Listener
{
    AntiUGFarm plugin;

    public ServerBlockListener(AntiUGFarm instance)
    {
        plugin = instance;
    }
 
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        List<Integer> checked = AntiUGFarm.config.getIntegerList("checked");
        List<Integer> ignored = AntiUGFarm.config.getIntegerList("ignored");
        String notallowed = AntiUGFarm.config.getString("string.notallowed", "You are not allowed to create a farm here!");

        if (!event.getPlayer().hasPermission("antiugfarm.ignore"))
        {
            if (checked.contains(event.getBlock().getTypeId()))
            {
                Block block = event.getBlock();
                World world = block.getWorld();
                Location location = block.getLocation();
                int x = location.getBlockX();
                int y = location.getBlockY() + 1;
                int z = location.getBlockZ();
                int maxheight = world.getMaxHeight();

                for (; y < maxheight; y++)
                    if (!ignored.contains(world.getBlockAt(x, y, z).getTypeId()))
                    {
                        event.getPlayer().sendMessage(notallowed);
                        event.setCancelled(true);
                        return;
                    }
            }
            else if (!ignored.contains(event.getBlock().getTypeId()))
            {
                World world = event.getBlock().getWorld();
                Location location = event.getBlock().getLocation();
                int x = location.getBlockX();
                int y = location.getBlockY() - 1;
                int z = location.getBlockZ();

                for (; y > 0; y--)
                    if (checked.contains(world.getBlockAt(x, y, z).getTypeId()))
                    {
                        event.getPlayer().sendMessage(notallowed);
                        event.setCancelled(true);
                        return;
                    }
            }
        }
    }
}
