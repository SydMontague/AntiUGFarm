package com.syd.antiugfarm;

import org.bukkit.Server;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import de.bananaco.permissions.worlds.HasPermission;
import de.bananaco.permissions.worlds.WorldPermissionsManager;

public class PermissionsSystem
{

    Server server;
    AntiUGFarm plugin;
    public static PermissionHandler perm = null;
    public static PermissionManager permEX = null;
    public static WorldPermissionsManager bPerm = null;
    public static boolean permBukkit = false;

    public PermissionsSystem(AntiUGFarm plugin)
    {
    }

    public void start()
    {
        if (AntiUGFarm.server.getPluginManager().getPlugin("PermissionsEx") != null)
        {
            permEX = PermissionsEx.getPermissionManager();
            AntiUGFarm.log.info("[AntiUGFarm] " + "PermissionsEX detected");
        }
        else if (AntiUGFarm.server.getPluginManager().getPlugin("PermissionsBukkit") != null)
        {
            AntiUGFarm.log.warning("[AntiUGFarm] PermissionsBukkit detected");
            permBukkit = true;
        }
        else if (AntiUGFarm.server.getPluginManager().getPlugin("Permissions") != null)
        {
            AntiUGFarm.log.info("[AntiUGFarm] " + "Permissions detected");
            Permissions permissions = (Permissions) AntiUGFarm.server.getPluginManager().getPlugin("Permissions");
            perm = permissions.getHandler();
        }
        else if (AntiUGFarm.server.getPluginManager().getPlugin("bPermissions") != null)
        {
            AntiUGFarm.log.info("[AntiUGFarm] bPermissions detected");
            bPerm = de.bananaco.permissions.Permissions.getWorldPermissionsManager();
        }
        else
        {
            permBukkit = true;
        }
    }

    public static boolean hasPermission(String world, String player, String node)
    {
        if (permEX != null)
        {
            return permEX.getUser(player).has(node);
        }
        else if (perm != null)
        {
            return perm.has(world, player, node);
        }
        else if (bPerm != null)
        {
            return HasPermission.has(player, world, node);
        }
        else if (permBukkit == true)
        {
            return AntiUGFarm.server.getPlayerExact(player).hasPermission(node);
        }
        else
            return AntiUGFarm.server.getPlayerExact(player).hasPermission(node);
    }
    
}
