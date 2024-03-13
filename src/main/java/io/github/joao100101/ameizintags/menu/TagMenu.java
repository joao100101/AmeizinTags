package io.github.joao100101.ameizintags.menu;

import io.github.joao100101.ameizintags.AmeizinTags;
import io.github.joao100101.ameizintags.database.service.impl.Cache;
import io.github.joao100101.ameizintags.model.Tag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TagMenu implements Listener {
    private final Inventory inv;
    private final List<Tag> tags = new ArrayList<>();
    private final Cache cache = new Cache();

    public TagMenu() {
        int tamanho = AmeizinTags.getInstance().getConfig().getInt("Config.TamanhoMenu");
        loadTags();
        if (tamanho <= 6 && tamanho >= 1) {
            this.inv = Bukkit.createInventory(null, 9 * tamanho, "TAGS");
        } else {
            this.inv = Bukkit.createInventory(null, 9 * 6, "§cCONFIG ERROR");
        }
    }

    private void loadItems(Player p) {
        String tagAtual = cache.getTagAtualName(p);
        for (Tag tag : tags) {
            ItemStack item = new ItemStack(Material.getMaterial(tag.getItem()));
            ItemMeta meta = item.getItemMeta();

            if (tagAtual.equalsIgnoreCase(tag.getName()) && !meta.getEnchants().containsKey(Enchantment.ARROW_INFINITE)) {
                meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
            }

            meta.setDisplayName(tag.getPrefixo());
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            setTagLore(p, tag);

            meta.setLore(tag.getLore());

            item.setItemMeta(meta);


            if (tag.getSlot() < inv.getSize() - 1 && tag.getSlot() >= 0) {
                inv.setItem(tag.getSlot(), item);
            }
        }


        ItemStack limpar = new ItemStack(Material.BARRIER);
        ItemMeta limparMeta = limpar.getItemMeta();
        limparMeta.setDisplayName("§cRetirar Tag Atual");
        limpar.setItemMeta(limparMeta);
        inv.setItem(inv.getSize() - 1, limpar);
    }


    public void setTagLore(Player p, Tag tag) {
        String tagAtual = cache.getTagAtualName(p);
        String tagAnterior = cache.getTagAnterior(p);
        String tagsPath = "Config.Tags." + tag.getName();

        Double valorTag = AmeizinTags.getInstance().getConfig().getDouble(tagsPath + ".valor");
        List<String> lore = new ArrayList<>();

        if (p.hasPermission("ameizintags.tag." + tag.getName())) {
            lore.add("");
            lore.add("§7Você possui essa tag.");
            lore.add("");
        } else {
            AmeizinTags.getInstance().getConfig().getStringList(tagsPath + ".lore").forEach(linha -> lore.add(linha.replace("&", "§").replace("%valor%", valorTag.toString()).replace("%prefixo%", tag.getPrefixo().replace("&", "§"))));
        }
        if (tag.getName().equalsIgnoreCase(tagAtual) && !lore.contains("§aATIVA")) {
            lore.add("");
            lore.add("§aATIVA");
            lore.add("");
        }
        tag.setLore(lore);
    }

    private void loadTags() {
        Set<String> configKeys = AmeizinTags.getInstance().getConfig().getConfigurationSection("Config.Tags").getKeys(false);
        String tagsPath = "Config.Tags.";
        for (String key : configKeys) {
            Tag tag = new Tag();
            tag.setItem(AmeizinTags.getInstance().getConfig().getString(tagsPath + key + ".item"));
            tag.setSlot(AmeizinTags.getInstance().getConfig().getInt(tagsPath + key + ".slot"));
            tag.setName(key);
            tag.setValor(AmeizinTags.getInstance().getConfig().getDouble(tagsPath + key + ".valor"));
            tag.setPrefixo(AmeizinTags.getInstance().getConfig().getString(tagsPath + key + ".prefixo").replace("&", "§"));
            tag.setVip(AmeizinTags.getInstance().getConfig().getBoolean(tagsPath + key + ".vip"));

            ArrayList<String> lore = new ArrayList<>();
            for (String line : AmeizinTags.getInstance().getConfig().getStringList(tagsPath + key + ".lore")) {
                lore.add(line.replace("&", "§").replace("%valor%", tag.getValor().toString()).replace("%prefixo%", tag.getPrefixo()));
            }
            tag.setLore(lore);
            tags.add(tag);
        }

    }

    public void openInventory(Player p) {
        p.openInventory(inv);
        loadItems(p);
    }

    @EventHandler
    private void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();


        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();
        if (e.getRawSlot() == this.inv.getSize() - 1) {
            if (!cache.getTagAtualName(p).isEmpty()) {
                cache.atualizarTag(p, "");
            }
            p.closeInventory();
            p.sendMessage(AmeizinTags.getInstance().getConfig().getString("Config.Mensagens.TagRemovida").replace("&", "§"));
            return;
        }

        for (Tag tag : tags) {
            if (tag.getSlot() == e.getRawSlot()) {
                if (tag.isVip() && !p.hasPermission("ameizintags.vip")) {
                    p.closeInventory();
                    p.sendMessage(AmeizinTags.getInstance().getConfig().getString("Config.Mensagens.TagVip").replace("&", "§"));
                    return;
                }
                if (!p.hasPermission("ameizintags.tag." + tag.getName()) && AmeizinTags.getEcon().getBalance(p) >= tag.getValor()) {
                    AmeizinTags.getPerms().playerAdd(p, "ameizintags.tag." + tag.getName());
                    AmeizinTags.getEcon().withdrawPlayer(p, tag.getValor());
                    cache.atualizarTag(p, tag.getName());
                    p.sendMessage(AmeizinTags.getInstance().getConfig().getString("Config.Mensagens.TagAdquirida").replace("&", "§").replace("%tag%", tag.getPrefixo().replace("&", "§")).replace("%valor%", tag.getValor() + ""));
                } else if (p.hasPermission("ameizintags.tag." + tag.getName())) {
                    cache.atualizarTag(p, tag.getName());
                    loadItems(p);
                } else {
                    p.closeInventory();
                    p.sendMessage(AmeizinTags.getInstance().getConfig().getString("Config.Mensagens.FaltaMoney").replace("&", "§").replace("%valor%", (tag.getValor() - AmeizinTags.getEcon().getBalance(p)) + ""));
                }
                return;
            }
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    private void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}
