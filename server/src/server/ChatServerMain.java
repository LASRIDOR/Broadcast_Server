package server;

public class ChatServerMain {

  // Usage: java examples.streams.advanced.server.ChatServer <port>
  public static void main(String[] args) {
    int port = 2323;
    if (args == null || args.length == 0) {
      System.out.println("Usage: java examples.streams.advanced.server.ChatServer <port>; Default port 2323 will be used.");
    } else {
      port = Integer.parseInt(args[0]);
    }

    //create a new instance of ChatServer and start handling clients
    ChatServer chatServer = new ChatServer(port);
    chatServer.startServer();
  }
}
