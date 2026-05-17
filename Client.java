import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {

        try {

            Socket socket = new Socket("localhost", 5000);

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            BufferedReader keyboard = new BufferedReader(
                    new InputStreamReader(System.in)
            );

            // Enter username
            System.out.print("Enter your name: ");

            String username = keyboard.readLine();

            output.println(username);

            // Thread for receiving messages
            Thread receiveThread = new Thread(() -> {

                try {

                    String message;

                    while ((message = input.readLine()) != null) {

                        // Move to new line cleanly
                        System.out.print("\r");

                        System.out.println(message);

                        // Show input prompt again
                        System.out.print("You: ");
                    }

                } catch (Exception e) {

                    System.out.println(e);
                }
            });

            receiveThread.start();

            String clientMessage;

            while (true) {

                System.out.print("You: ");

                clientMessage = keyboard.readLine();

                output.println(clientMessage);

                if (clientMessage.equalsIgnoreCase("bye")) {

                    break;
                }
            }

            socket.close();

        } catch (Exception e) {

            System.out.println(e);
        }
    }
}