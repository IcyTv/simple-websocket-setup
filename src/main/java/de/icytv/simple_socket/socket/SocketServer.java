package de.icytv.simple_socket.socket;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


/**
 * Socket Server.
 * 
 * 
 * @see https://github.com/TooTallNate/Java-WebSocket/wiki
 */
public class SocketServer extends WebSocketServer {

    private final int port;

    public SocketServer(int port) throws UnknownHostException {
		this(new InetSocketAddress(port));
	}

	public SocketServer(InetSocketAddress addr) throws UnknownHostException {
        super(addr);
        this.port = addr.getPort();
	}


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection " + conn.getRemoteSocketAddress().getHostName());
        broadcast("New Client connected"); //MISSING NAME
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Client " + conn.getRemoteSocketAddress().getHostName() + (reason.equals("") ? "disconnected!": ("disconnected, because:\n" + reason)));
        broadcast("... Disconnected");
    }

    //When sending messages, think about styling (<b>) server messages... How would you do that?
    @Override
    public void onMessage(WebSocket conn, String message) {
        // EDIT HERE
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println(ex.getLocalizedMessage());
        ex.printStackTrace();
        //IS THERE ANYTHING ELSE YOU NEED TO DO WHEN AN ERROR OCCURS?
    }

    @Override
    public void onStart() {
        System.out.println("Socket Server has started");
    }

}