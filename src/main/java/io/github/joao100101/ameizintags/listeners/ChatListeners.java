package io.github.joao100101.ameizintags.listeners;

import com.nickuc.chat.api.events.PublicMessageEvent;
import io.github.joao100101.ameizintags.database.service.impl.Cache;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListeners implements Listener {

    private final Cache cache = new Cache();

    @EventHandler
    public void onChat(PublicMessageEvent e){
        TextComponent tagComponent = new TextComponent(TextComponent.fromLegacyText(cache.getTagAtual(e.getSender())));
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Compre sua tag no §a/tags"));
        tagComponent.setHoverEvent(hoverEvent);

        // Define a tag
        e.setTag("ameizintag", tagComponent);
    }

}
