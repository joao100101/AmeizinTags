package io.github.joao100101.ameizintags.listeners;

import io.github.joao100101.ameizintags.database.service.impl.Cache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CacheListener implements Listener {
    private Cache cache;

    public CacheListener(Cache cache) {
        this.cache = cache;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        cache.loadCache(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        cache.saveCache(e.getPlayer());
    }


}
