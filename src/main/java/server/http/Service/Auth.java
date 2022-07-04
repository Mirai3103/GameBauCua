//package server.http.Service;
//
//import client.model.LoginPayLoad;
//import org.hibernate.Session;
//import org.slf4j.Logger;
//import server.handler.ClientHandler;
//import server.model.ClientSocket;
//import server.model.User;
//import server.utils.HibernateUtils;
//import shared.model.LoginReturned;
//import shared.model.UserInfo;
//
//public class Auth {
//    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Auth.class);
//    public static void login(LoginPayLoad loginPayLoad) {
//        Session session = HibernateUtils.getSessionFactory().openSession();
//        User user = session.createQuery("from User where username = :username and password =:password", User.class)
//                .setParameter("username", loginPayLoad.getUsername())
//                .setParameter("password", loginPayLoad.getPassword())
//                .uniqueResult();
//        session.close();
//        if (user != null) {
//            logger.info("User " + loginPayLoad.getUsername() + " logged in");
//            System.out.println("LoginReturned: " + user);
//            ClientSocket clientSocket = new ClientSocket(new UserInfo(user), socket, objectInputStream, objectOutputStream);
//            UserInfo userInfo =new UserInfo(user);
//            LoginReturned loginReturned = new LoginReturned( userInfo);
//            System.out.println("LoginReturned: " + loginReturned);
//            objectOutputStream.writeObject(loginReturned);
//            handleEvent.getClientSockets().add(clientSocket);
//            ClientHandler clientHandler = new ClientHandler(clientSocket, handleEvent);
//            clientHandler.start();
//            this.join();
//            break;
//        } else {
//            logger.error("A user login failed");
//            LoginReturned loginReturned = new LoginReturned(null);
//            objectOutputStream.writeObject(loginReturned);
//        }
//    }
//}
