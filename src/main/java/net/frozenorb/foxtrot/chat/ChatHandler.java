package net.frozenorb.foxtrot.chat;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.chat.listeners.ChatListener;

import java.util.concurrent.atomic.AtomicInteger;

public class ChatHandler {

    @Getter private static AtomicInteger publicMessagesSent = new AtomicInteger();

    public ChatHandler() {
        Foxtrot.getInstance().getServer().getPluginManager().registerEvents(new ChatListener(), Foxtrot.getInstance());
    }

}