package client.service;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import client.model.ExceptionFromServer;
import client.model.LoginPayLoad;
import client.model.RegisterPayLoad;
import shared.model.LoginReturned;
import server.model.User;

import javax.swing.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class AuthService {
    private static final String url = "http://localhost:8080/api/auth";
    private static final String CHARSET = "UTF-8";
    private final JFrame parentFrame;
    private final Gson gson ;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    public AuthService(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.gson = new Gson();
    }
    public AuthService(JFrame parentFrame, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.parentFrame = parentFrame;
        this.gson = new Gson();
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;


    }
    public String Login(LoginPayLoad loginPayLoad) throws IOException {
        HttpURLConnection con = (HttpURLConnection)(new URL(url+"/login")).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);
        try {
            con.getOutputStream().write(loginPayLoad.toJson().getBytes());
            if (con.getResponseCode() != 200) {
                ExceptionFromServer exceptionFromServer = getExceptionFromServer(con);
                JOptionPane.showMessageDialog(null, exceptionFromServer.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                Token token = getDataFromResponse(con,Token.class);
                return token.getToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            con.disconnect();
        }

        return "";
    }
    public User Register(RegisterPayLoad registerPayLoad) throws IOException {
        HttpURLConnection con = (HttpURLConnection)(new URL(url+"/register")).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try {
            con.getOutputStream().write(registerPayLoad.toJson().getBytes());
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                return getDataFromResponse(con,User.class);
            }else {
                ExceptionFromServer exceptionFromServer = getExceptionFromServer(con);
                JOptionPane.showMessageDialog(null, exceptionFromServer.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            con.disconnect();
        }
        return null;
    }

    public ExceptionFromServer getExceptionFromServer(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return new Gson().fromJson(content.toString(), ExceptionFromServer.class);
    }
    public <T extends Object> T getDataFromResponse(HttpURLConnection con, Class<T> clazz) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (con.getInputStream())));
        String output;
        StringBuilder sb = new StringBuilder();
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        System.out.println(sb.toString());
        return gson.fromJson(sb.toString(), clazz);
    }
    public LoginReturned login(LoginPayLoad loginPayLoad) throws IOException, ClassNotFoundException {


        objectOutputStream.writeObject(loginPayLoad);
        objectOutputStream.flush();
        LoginReturned loginReturned = (LoginReturned) objectInputStream.readObject();
        System.out.println(loginReturned.getUserInfo().getUsername());
        if (loginReturned.getUserInfo() == null) {
            JOptionPane.showMessageDialog(null, "wrong username or password", "Error", JOptionPane.ERROR_MESSAGE);

        }else {
            System.out.println(loginReturned.getUserInfo());
            return loginReturned;
        }
        return null;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static
    class Token {
        private String token;
    }
}