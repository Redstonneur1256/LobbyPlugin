package fr.redstonneur1256.lobby;

import arc.Events;
import arc.files.Fi;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.serialization.Json;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.mod.Plugin;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LobbyPlugin extends Plugin {

    private static final Json json;

    static {
        json = new Json();
        json.setIgnoreUnknownFields(true);
    }

    private Configuration configuration;

    public LobbyPlugin() {
        Events.on(EventType.ServerLoadEvent.class, this::enable);
    }

    private void enable(EventType.ServerLoadEvent event) {
        try {
            reloadConfiguration();
        }catch(Exception exception) {
            Log.err("Failed to load configuration", exception);
        }
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        CommandHandler.CommandRunner<Player> command = (args, player) -> {
            if(configuration.lobbyAddress == null) {
                player.sendMessage("[red]The lobby server is not defined, please contact a server administrator.");
                return;
            }

            Call.connect(player.con, configuration.lobbyAddress, configuration.lobbyPort);
        };

        handler.register("lobby", "Connects you to the lobby server", command);
        handler.register("hub", "Connects you to the lobby server", command);
    }

    public void saveConfiguration() throws IOException {
        try(OutputStream output = getConfig().write()) {
            output.write(json.prettyPrint(configuration).getBytes(StandardCharsets.UTF_8));
        }
    }

    public void reloadConfiguration() throws Exception {
        Fi configFile = getConfig();

        if(!configFile.exists()) {
            configuration = new Configuration();
            configuration.applyDefaults();
            saveConfiguration();
            return;
        }

        configuration = json.fromJson(Configuration.class, configFile);

    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
