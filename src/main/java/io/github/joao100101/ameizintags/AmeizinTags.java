package io.github.joao100101.ameizintags;

import io.github.joao100101.ameizintags.commands.Comandos;
import io.github.joao100101.ameizintags.database.service.impl.Cache;
import io.github.joao100101.ameizintags.database.SQLite;
import io.github.joao100101.ameizintags.listeners.CacheListener;
import io.github.joao100101.ameizintags.listeners.ChatListeners;
import io.github.joao100101.ameizintags.menu.TagMenu;
import io.github.joao100101.ameizintags.model.UserTag;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class AmeizinTags extends JavaPlugin {
    public static final Map<Player, UserTag> tagCache = new HashMap<>();
    private static TagMenu tagMenu;
    private static Economy econ = null;
    private static Permission perms = null;
    private SQLite sqLite = new SQLite(getDataFolder().getAbsolutePath() + "/data.db");
    private Cache cache = new Cache(sqLite);
    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();
        sqLite.createTable();
        setupPermissions();

        tagMenu = new TagMenu();
        registerEvents();
        registerCommands();
        cache.loadAllCaches();
    }

    @Override
    public void onDisable() {
        cache.saveAllCaches();
    }
    public void registerCommands(){
        getCommand("tags").setExecutor(new Comandos());
    }
    public void registerEvents(){
        Bukkit.getPluginManager().registerEvents(tagMenu, this);
        Bukkit.getPluginManager().registerEvents(new ChatListeners(), this);
        Bukkit.getPluginManager().registerEvents(new CacheListener(new Cache(sqLite)), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    public static Economy getEcon(){
        return econ;
    }

    public static Permission getPerms(){
        return perms;
    }

    public static TagMenu getTagMenu(){
        return tagMenu;
    }


    public static AmeizinTags getInstance(){
        return (AmeizinTags) Bukkit.getPluginManager().getPlugin("AmeizinTags");
    }
}
