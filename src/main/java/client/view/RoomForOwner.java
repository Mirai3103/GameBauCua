package client.view;

import client.utils.GlobalVariable;
import client.view.component.IHaveNoIdea;
import lombok.Getter;
import shared.model.event.GameState;

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
    private JButton startButton;
    private JButton moDiaButton;
    private JButton resetGameButton;
    private int cuaBet = 0;
    private int bauBet = 0;
    private int tomBet = 0;
    private int caBet = 0;
    private int gaBet = 0;
    private int naiBet = 0;
    public GameState.State gameState;
    private JLabel stateLabel;
    public RoomForOwner(){
        GlobalVariable.eventHandler.setCurrentFrame(this);
        bauButton = new JButton("Bau " + bauBet+"$");
        cuaButton = new JButton("Cua " + cuaBet+"$");
        tomButton = new JButton("Tom " + tomBet+"$");
        caButton = new JButton("Ca " + caBet+"$");
        gaButton = new JButton("Ga "+ gaBet+"$");
        naiButton = new JButton("Nai "+ naiBet+"$");
        startButton = new JButton("Start");
        moDiaButton = new JButton("Mở đĩa");
        resetGameButton = new JButton("Reset Game");
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
        stateLabel = new JLabel("");

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
        temp.add(moDiaButton);
        temp.add(resetGameButton);
        temp.add(startButton);
        socButton.addActionListener(e->{
            try {
              String[] xucXac = iHaveNoIdea.reDraw();
                iHaveNoIdea.repaint();
                GlobalVariable.eventHandler.startGame(xucXac);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        mainPanel.add(temp, BorderLayout.SOUTH);
        mainPanel.add(stateLabel, BorderLayout.NORTH);
        add(mainPanel);
        add(panel1);
        setGameState(GameState.State.WAITING);
        setVisible(true);
    }
    public void reDrawButton(){
        bauButton.setText("Bau " + bauBet+"$");
        cuaButton.setText("Cua " + cuaBet+"$");
        tomButton.setText("Tom " + tomBet+"$");
        caButton.setText("Ca " + caBet+"$");
        gaButton.setText("Ga "+ gaBet+"$");
        naiButton.setText("Nai "+ naiBet+"$");
    }



    public void setGameState(GameState.State state){
        this.gameState = state;
        stateLabel.setText(state.toString());

        switch (state){
            case WAITING:
                socButton.setEnabled(true);
                moDiaButton.setEnabled(false);
                resetGameButton.setEnabled(true);
                startButton.setEnabled(true);
                break;
            case PLAYING:
                socButton.setEnabled(false);
                moDiaButton.setEnabled(false);
                resetGameButton.setEnabled(false);
                startButton.setEnabled(false);
                break;
            case END:
                moDiaButton.setEnabled(true);
                socButton.setEnabled(false);
                resetGameButton.setEnabled(false);
                startButton.setEnabled(false);
                break;
        }
    }


    public void plusBauBet(int bet){
        bauBet += bet;
    }
    public void plusCuaBet(int bet){
        cuaBet += bet;
    }
    public void plusTomBet(int bet){
        tomBet += bet;
    }
    public void plusCaBet(int bet){
        caBet += bet;
    }
    public void plusGaBet(int bet){
        gaBet += bet;
    }
    public void plusNaiBet(int bet){
        naiBet += bet;
    }
    public void clearBet(){
        bauBet = 0;
        cuaBet = 0;
        tomBet = 0;
        caBet = 0;
        gaBet = 0;
        naiBet = 0;
    }
    public static void main(String[] args) {
        RoomForOwner roomForOwner = new RoomForOwner();
        roomForOwner.setVisible(true);
    }
}
