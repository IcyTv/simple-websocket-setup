package de.icytv.simple_socket;

import java.io.IOException;

import de.icytv.simple_socket.http.HTTPServer;

public class Main {
    public static void main(String[] args) throws IOException {
        HTTPServer http = new HTTPServer(8080, 9090);
        http.start();
    }
}