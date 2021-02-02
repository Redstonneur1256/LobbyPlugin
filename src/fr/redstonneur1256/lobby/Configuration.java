package fr.redstonneur1256.lobby;

public class Configuration {

    public String lobbyAddress;
    public int lobbyPort;

    public void applyDefaults() {
        lobbyAddress = "127.0.0.1";
        lobbyPort = 6567;
    }

}
