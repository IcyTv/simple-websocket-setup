package de.icytv.simple_socket.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import de.icytv.simple_socket.socket.SocketServer;


public class HTTPServer {

	public static String CLIENT_PATH = "client";

	final private int port;
	final private int socketPort;
	private boolean created;
	final private HttpServer server;
	final private InetSocketAddress address;
	final private ClientHandler mainHandler;
	final private SocketServer socketServer;

	public HTTPServer(int port, int socketPort) throws IOException {
		this.port = port;
		this.socketPort = socketPort;
		created = false;
		address = new InetSocketAddress(port);
		server = HttpServer.create(address, 0);
		mainHandler = new ClientHandler();
		socketServer = new SocketServer(socketPort);
		socketServer.setConnectionLostTimeout(30);
	}

	public void create() throws IOException {
		created = true;
		server.createContext("/", mainHandler);
		server.setExecutor(null);
	}

	public void start() throws IOException {
		if (!created)
			create();
		server.start();
		socketServer.start();
		System.out.println("Started server at " + port);
		System.out.println("Started SocketServer at " + socketPort);
	}

	public void stop() {
		server.stop(10);
	}


	static class ClientHandler implements HttpHandler {
		final static private Pattern urlPattern = Pattern.compile("^.*\\..*$");;
        public void handle(HttpExchange t) throws IOException {
            String root = "./public";
            URI uri = t.getRequestURI();
            System.out.println("looking for: "+ root + uri.getPath());
			String path = uri.getPath();
			if(path.equals("") || path.equals("/")) {
				path = "/index.html";
			}
			Matcher m = urlPattern.matcher(path);
			if(!m.matches()) {
				path += ".html";
			}

            File file = new File(root + path).getCanonicalFile();
    
            if (!file.isFile()) {
              // Object does not exist or is not a file: reject with 404 error.
              String response = "404 (Not Found)\n";
              t.sendResponseHeaders(404, response.length());
              OutputStream os = t.getResponseBody();
              os.write(response.getBytes());
              os.close();
            } else {
              // Object exists and is a file: accept with response code 200.
              String mime = "text/html";
              if(path.endsWith(".js")) mime = "application/javascript";
              if(path.endsWith("css")) mime = "text/css";            
    
              Headers h = t.getResponseHeaders();
              h.set("Content-Type", mime);
              t.sendResponseHeaders(200, 0);              
    
              OutputStream os = t.getResponseBody();
              FileInputStream fs = new FileInputStream(file);
              final byte[] buffer = new byte[0x10000];
              int count = 0;
              while ((count = fs.read(buffer)) >= 0) {
                os.write(buffer,0,count);
              }
              fs.close();
              os.close();
            }  
        }
    }


}