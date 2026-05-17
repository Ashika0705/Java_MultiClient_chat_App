import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;

    public static ArrayList<ClientHandler> clients = new ArrayList<>();

    private String clientName;
// Server broadcast message
public static void serverBroadcast(String message) {

    for (ClientHandler client : clients) {

        client.output.println("[SERVER]: " + message);
    }
}
    public ClientHandler(Socket socket) {

        this.socket = socket;

        try {

            output = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            // First message is username
            clientName = input.readLine();

            clients.add(this);

            
            System.out.print("\r");
            System.out.println(clientName + " connected.");
            System.out.print("You: ");

        } catch (Exception e) {

            System.out.println(e);
        }
    }

    // Broadcast to everyone EXCEPT sender
    public void broadcastMessage(String message) {

        for (ClientHandler client : clients) {

            // Don't send message back to sender
            if (client != this) {

                client.output.println(message);
            }
        }
    }

    public void run() {

        try {

            broadcastMessage(clientName + " joined the chat!");

            String message;

            while ((message = input.readLine()) != null) {

                if (message.equalsIgnoreCase("bye")) {

                    broadcastMessage(clientName + " left the chat.");

                    break;
                }

              System.out.print("\r");
              System.out.println(clientName + ": " + message);
              System.out.print("You: ");

                broadcastMessage(clientName + ": " + message);
            }

            clients.remove(this);

            socket.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }
}