package io.github.joao100101.ameizintags.database.service.impl;

import io.github.joao100101.ameizintags.AmeizinTags;
import io.github.joao100101.ameizintags.database.SQLite;
import io.github.joao100101.ameizintags.database.service.CacheService;
import io.github.joao100101.ameizintags.model.UserTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import static io.github.joao100101.ameizintags.AmeizinTags.tagCache;

public class Cache implements CacheService {

    private final SQLite sqLite;


    public Cache(SQLite sqLite) {
        this.sqLite = sqLite;
    }
    public Cache(){
        this.sqLite = new SQLite(AmeizinTags.getInstance().getDataFolder().getAbsolutePath() + "/data.db");
    }


    @Override
    public String getTagAtual(Player p) {
        String tagNameAtual = "";
        if(tagCache.containsKey(p)){
            tagNameAtual = tagCache.get(p).getTagAtual();
        }
        if(tagNameAtual == null || tagNameAtual.isEmpty()){
            return "";
        }
        return AmeizinTags.getInstance().getConfig().getString("Config.Tags." + tagNameAtual + ".prefixo").replace("&", "§");
    }

    @Override
    public String getTagAnterior(Player p) {
        String tagNameAnterior = "";
        if (tagCache.containsKey(p)){
            tagNameAnterior = tagCache.get(p).getTagAnterior();
        }
        if(tagNameAnterior == null || tagNameAnterior.isEmpty()){
            return "";
        }
        return tagNameAnterior;
    }

    @Override
    public String getTagAtualName(Player p) {
        String tagNameAtual = "";
        if (tagCache.containsKey(p)){
            tagNameAtual = tagCache.get(p).getTagAtual();
        }
        return tagNameAtual;
    }

    @Override
    public void atualizarTag(Player p, String novaTag) {
        UserTag userTag = new UserTag(novaTag, "");
        if(tagCache.containsKey(p)){
            userTag = new UserTag(novaTag, getTagAtualName(p));
        }
        tagCache.put(p, userTag);
    }

    @Override
    public void saveAllCaches() {
        tagCache.keySet().forEach(p -> sqLite.saveTag(p.getUniqueId().toString(), getTagAtualName(p), getTagAnterior(p)));
    }

    @Override
    public void loadAllCaches() {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
            String uuid = onlinePlayer.getUniqueId().toString();
            if(sqLite.existInTable(uuid)){
                tagCache.put(onlinePlayer, new UserTag(sqLite.getTagAtual(uuid), sqLite.getTagAnterior(uuid)));
            }else {
                tagCache.put(onlinePlayer, new UserTag("", ""));
            }
        }
    }
    @Override
    public void saveCache(Player p) {
        if (tagCache.containsKey(p)) {
            sqLite.saveTag(p.getUniqueId().toString(), getTagAtualName(p), getTagAnterior(p));
        } else {
            sqLite.saveTag(p.getUniqueId().toString(), "", "");
        }

    }

    @Override
    public void loadCache(Player p) {
        String uuid = p.getUniqueId().toString();
        if(sqLite.existInTable(uuid)){
            tagCache.put(p, new UserTag(sqLite.getTagAtual(uuid), sqLite.getTagAnterior(uuid)));
        }else {
            tagCache.put(p, new UserTag("", ""));
        }
    }



}