package client.service;


import client.utils.GlobalVariable;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import client.model.ExceptionFromServer;
import client.model.LoginPayLoad;

import shared.model.LoginReturned;


import javax.swing.*;

import java.io.*;

public class AuthService {
    private final JFrame parentFrame;

    public AuthService(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public LoginReturned Login(LoginPayLoad loginPayLoad) throws IOException, ClassNotFoundException {


        GlobalVariable.objectOutputStream.writeObject(loginPayLoad);
        GlobalVariable.objectOutputStream.flush();
        LoginReturned loginReturned = (LoginReturned)  GlobalVariable.objectInputStream.readObject();
        System.out.println(loginReturned.getUserInfo().getUsername());
        if (loginReturned.getUserInfo() == null) {
            JOptionPane.showMessageDialog(null, "wrong username or password", "Error", JOptionPane.ERROR_MESSAGE);

        }else {
            System.out.println(loginReturned.getUserInfo());
            return loginReturned;
        }
        return null;
    }
}