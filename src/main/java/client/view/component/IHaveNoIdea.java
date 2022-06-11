package client.view.component;

import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Getter
public class IHaveNoIdea extends JPanel {
    private BufferedImage imageBG;
    private JLabel xucXac1;
    private JLabel xucXac2;
    private JLabel xucXac3;
    private String[] xucXac = {"bau", "cua", "tom","ga", "ca", "nai"};
    private String source;

    public IHaveNoIdea(String path) {
        try {
            source = path;
            imageBG = ImageIO.read(new File(path));

            drawXucXac("src/main/resources/images/"+randomXucXac()+".png",70,30);
            drawXucXac("src/main/resources/images/"+randomXucXac()+".png",150,30);
            drawXucXac("src/main/resources/images/"+randomXucXac()+".png",105,85);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] reDraw() throws IOException {
        String[] xucXac = new String[3];
        xucXac[0] = randomXucXac();
        xucXac[1] = randomXucXac();
        xucXac[2] = randomXucXac();
        imageBG = ImageIO.read(new File(source));
        drawXucXac("src/main/resources/images/"+xucXac[0]+".png",70,30);
        drawXucXac("src/main/resources/images/"+xucXac[1]+".png",150,30);
        drawXucXac("src/main/resources/images/"+xucXac[2]+".png",105,85);
        return xucXac;
    }
    public void drawXucXac(String path1,int x, int y) throws IOException {
        BufferedImage image1 = ImageIO.read(new File(path1));
        Image tmp = image1.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        image1 = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image1.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        Graphics g1 = imageBG.getGraphics();
        g1.drawImage(image1, x, y, null);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(this.getWidth() / 2, this.getHeight() / 2);
        g2d.translate(-imageBG.getWidth(null) / 2, -imageBG.getHeight(null) / 2);
        g2d.drawImage(imageBG, 0, 0, null);
    }

    public String randomXucXac(){
        int random = (int) (Math.random() * 6);
        return xucXac[random];
    }

}
