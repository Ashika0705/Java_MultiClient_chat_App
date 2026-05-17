import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {

        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket(5000);

            System.out.println("Server started...");
            System.out.println("Waiting for clients...");

            Thread serverInputThread = new Thread(() -> {

    try {

        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(System.in)
        );

        String serverMessage;

        while (true) {

            // Show prompt ONLY when ready to type
            System.out.print("You: ");

            serverMessage = keyboard.readLine();

            // Prevent empty messages
            if (serverMessage.trim().isEmpty()) {
                continue;
            }

            // Print server message locally
            System.out.println("You: " + serverMessage);

            // Broadcast to clients
            ClientHandler.serverBroadcast(serverMessage);
        }

    } catch (Exception e) {

        System.out.println(e);
    }
});

           

            serverInputThread.start();

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println("New client connected!");

                ClientHandler clientThread = new ClientHandler(socket);

                clientThread.start();
            }

        } catch (Exception e) {

            System.out.println(e);

        } finally {

            try {

                if (serverSocket != null) {

                    serverSocket.close();
                }

            } catch (Exception e) {

                System.out.println(e);
            }
        }
    }
}