package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

class ServerChatThread extends Thread implements Observer {

    private ChatServer server;
    private int index;
    private Socket socket;
    private PrintWriter out;

    ServerChatThread(ChatServer server, int index, Socket socket) {
        this.server = server;
        this.index = index;
        this.socket = socket;
    }

    @Override
    public void run() {
        //in - the stream that comes from the client
        try (BufferedReader in =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()))){

            //out - the stream that goes back to the client
            out = new PrintWriter(socket.getOutputStream(), true);

            //send welcome message
            out.println("Server > Welcome to our chat!");
            out.flush();
            String line;
            //while no new lines coming from the server the in.readLine will block the code
            //when a new line arrives, tell the server to update all the clients
            //if the client will terminate the session, the line will be null
            while ((line = in.readLine()) != null) {
                server.sendMessageToAllConnectedClients("Message from Client " + index + ": " + line);
            }
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        } finally {
            try {
                out.close();
                socket.close();
            } catch (Exception e) {
            }
            //tell the server to remove this client
            server.removeClient(index, this);
        }
    }

    @Override
    //this method will be called by the Observable (ChatServer) when updating all the clients
    public void update(Observable server, Object message) {
        out.println(message);
        out.flush();
    }
}