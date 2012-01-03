package com.syd.antiugfarm;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ServerBlockListener extends BlockListener
{
    AntiUGFarm plugin;

    public ServerBlockListener(AntiUGFarm instance)
    {
        plugin = instance;
    }

    @SuppressWarnings("unchecked")
    public void onBlockPlace(BlockPlaceEvent event)
    {
        List<Integer> checked = AntiUGFarm.config.getList("checked", null);
        List<Integer> ignored = AntiUGFarm.config.getList("ignored", null);
        String notallowed = AntiUGFarm.config.getString("string.notallowed", "You are not allowed to plant trees under the earth!");

        if (!PermissionsSystem.hasPermission(event.getPlayer().getWorld().getName(), event.getPlayer().getName(), "antiugfarm.ignore"))
        {
            if (checked.contains(event.getBlock().getTypeId()))
            {
                Block block = event.getBlock();
                World world = block.getWorld();
                Location location = block.getLocation();
                int x = location.getBlockX();
                int y = location.getBlockY() + 1;
                int z = location.getBlockZ();

                for (; y < 128; y++)
                {
                    if (!ignored.contains(world.getBlockAt(x, y, z).getTypeId()))
                    {
                        event.getPlayer().sendMessage(notallowed);
                        event.setCancelled(true);
                        return;
                    }
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
                {
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
}
