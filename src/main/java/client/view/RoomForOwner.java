package client.view;

import client.view.component.IHaveNoIdea;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
@Getter

public class RoomForOwner extends javax.swing.JFrame {
    private JButton bauButton;
    private JButton cuaButton;
    private JButton tomButton;
    private JButton caButton;
    private JButton gaButton;
    private JButton naiButton;
    private IHaveNoIdea iHaveNoIdea;
    private JPanel panel1;
    private JPanel mainPanel;
    private JButton socButton;
    public RoomForOwner(){
        setBackground(Color.DARK_GRAY);
        bauButton = new JButton("Bau");
        cuaButton = new JButton("Cua");
        tomButton = new JButton("Tom");
        caButton = new JButton("Ca");
        gaButton = new JButton("Ga");
        naiButton = new JButton("Nai");
        bauButton.setEnabled(false);
        cuaButton.setEnabled(false);
        tomButton.setEnabled(false);
        caButton.setEnabled(false);
        gaButton.setEnabled(false);
        naiButton.setEnabled(false);

        setLayout(new GridLayout(2, 1));
        setSize(500, 600);
        setResizable(false);
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        panel1 = new JPanel();
        iHaveNoIdea = new IHaveNoIdea("src/main/resources/images/caidiatrong.png");


        panel1.setLayout(new GridLayout(2, 3));
        panel1.add(bauButton);
        panel1.add(cuaButton);
        panel1.add(tomButton);
        panel1.add(caButton);
        panel1.add(gaButton);
        panel1.add(naiButton);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(iHaveNoIdea, BorderLayout.CENTER);
        JPanel temp = new JPanel();
        temp.setLayout(new FlowLayout());
        socButton = new JButton("Xoc dia");
        socButton.setSize(100, 300);
        temp.setLayout(new FlowLayout());
        temp.add(socButton);
        socButton.addActionListener(e->{
            try {
                iHaveNoIdea.reDraw();
                iHaveNoIdea.repaint();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        mainPanel.add(temp, BorderLayout.SOUTH);
        add(mainPanel);
        add(panel1);
    }
    public static void main(String[] args) {
        RoomForOwner roomForOwner = new RoomForOwner();
        roomForOwner.setVisible(true);
    }
}
