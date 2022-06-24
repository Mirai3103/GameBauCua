package server;

import client.view.Login;
import org.hibernate.Session;
import server.handler.ClientHandler;
import server.handler.HandleEvent;
import server.handler.LoginState;
import server.model.ClientSocket;
import server.model.User;
import server.utils.HibernateUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static void run() {
        ServerSocket serverSocket = null;
        final ArrayList<ClientSocket> clientSockets = new ArrayList<>();
        final HandleEvent handleEvent = new HandleEvent(clientSockets);


        try {
            serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                LoginState loginState = new LoginState(socket, handleEvent);
                loginState.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
