package client.view;

import client.view.component.DiaForPlayer;
import client.view.component.IHaveNoIdea;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class RoomForPlayer extends JFrame {
    private JButton bauButton;
    private JButton cuaButton;
    private JButton tomButton;
    private JButton caButton;
    private JButton gaButton;
    private JButton naiButton;
    private DiaForPlayer diaForPlayer;
    private JPanel panel1;
    private JPanel mainPanel;
    private JButton socButton;
    public RoomForPlayer(){
        setBackground(Color.DARK_GRAY);
        bauButton = new JButton("Bau");
        cuaButton = new JButton("Cua");
        tomButton = new JButton("Tom");
        caButton = new JButton("Ca");
        gaButton = new JButton("Ga");
        naiButton = new JButton("Nai");


        setLayout(new GridLayout(2, 1));
        setSize(500, 600);
        setResizable(false);
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        panel1 = new JPanel();
        diaForPlayer = new DiaForPlayer("src/main/resources/images/caidiatrong.png");


        panel1.setLayout(new GridLayout(2, 3));
        panel1.add(bauButton);
        panel1.add(cuaButton);
        panel1.add(tomButton);
        panel1.add(caButton);
        panel1.add(gaButton);
        panel1.add(naiButton);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(diaForPlayer, BorderLayout.CENTER);

        add(mainPanel);
        add(panel1);
    }
    public static void main(String[] args) {
        RoomForPlayer roomForOwner = new RoomForPlayer();
        roomForOwner.setVisible(true);
    }
}
