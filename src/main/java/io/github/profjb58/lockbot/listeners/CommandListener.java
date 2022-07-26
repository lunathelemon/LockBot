package io.github.profjb58.lockbot.listeners;

import io.github.profjb58.lockbot.LockBot;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {

    private final LockBot instance;
    public CommandListener(LockBot instance) {
        this.instance = instance;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
    }
}
