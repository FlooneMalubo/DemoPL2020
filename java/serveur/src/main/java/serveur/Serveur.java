package serveur;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import metier.Compteur;

public class Serveur {


    private final SocketIOServer server;
    private final Compteur compteur;

    public static final void main(String [] args) {
        // config  com.corundumstudio.socketio.Configuration;
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(10101);

        // creation du serveur
        SocketIOServer server = new SocketIOServer(config);

        // obj metier
        Compteur cpt = new Compteur();
        
        Serveur s = new Serveur(server, cpt);
        s.démarrer();

    }

    public Serveur(SocketIOServer server, Compteur cpt) {

        this.compteur = cpt;
        this.server = server;
        this.server.addEventListener("add", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
                nouvelAjout(socketIOClient);
            }
        });
    }

    protected void nouvelAjout(SocketIOClient socketIOClient) {
        System.out.println("on ajoute");
        compteur.ajouter();
        socketIOClient.sendEvent("valeur",compteur.getCpt());
    }

    private void démarrer() {
        server.start();
    }


}
