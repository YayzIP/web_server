package ahnx33.scuola;

public class Session implements Runnable {

    private final ClientHandler client;

    public Session(ClientHandler client) {
        this.client = client;
    }

    @Override
    public void run() {
    }

}
