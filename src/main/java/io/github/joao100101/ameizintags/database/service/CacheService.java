package io.github.joao100101.ameizintags.database.service;

import org.bukkit.entity.Player;

public interface CacheService {


    String getTagAtual(Player p);
    String getTagAnterior(Player p);
    String getTagAtualName(Player p);
    void atualizarTag(Player p, String novaTag);

    void saveCache(Player p);
    void loadCache(Player p);

    void saveAllCaches();
    void loadAllCaches();

}
