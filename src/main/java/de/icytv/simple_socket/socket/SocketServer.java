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
        //broadcast("New Client connected"); //MISSING NAME
        conn.send("SYSTEM:Please enter your name:");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Client " + conn.getRemoteSocketAddress().getHostName() + (reason.equals("") ? " disconnected!": (" disconnected, because:\n" + reason)));
        String name = conn.<String>getAttachment();
        if(name != null) {
            broadcast("SYSTEM:" + name + " disconnected");
        } else {
            //User hasn't entered their name yet
        }
    }

    //When sending messages, think about styling (<b>) server messages... How would you do that?
    @Override
    public void onMessage(WebSocket conn, String message) {
        if(conn.<String>getAttachment() == null) {
            conn.setAttachment(message.replace("SYSTEM:", ""));
            broadcast("SYSTEM:" + conn.<String>getAttachment() + " connected");
        } else {
            broadcast(conn.<String>getAttachment() + ": " + message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println(ex.getLocalizedMessage());
        ex.printStackTrace();
        try {
            conn.send("SYSTEM:Exception: " + ex.getMessage());
        } catch(Exception e) {
            System.err.println("User already disconnected");
        }
    }

    @Override
    public void onStart() {
        System.out.println("Socket Server has started");
    }

}