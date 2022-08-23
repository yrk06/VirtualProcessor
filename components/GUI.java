package components;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GUI extends Component {

    GUIPanel panel;
    JFrame frame;

    int xRegister;
    int yRegister;
    int colorRegister;

    public GUI() {
        frame = new JFrame("runtime gui");
        panel = new GUIPanel(196, 196);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public int access(int address, int data, int flags) {
        int vAddress = address - mountAddrStart;
        switch (vAddress) {
            case 0:
                xRegister = data;
                break;
            case 1:
                yRegister = data;
                break;
            case 2:
                colorRegister = data;
                break;
            case 3:
                panel.setPixel(xRegister, yRegister, colorRegister);
                panel.repaint();
                break;
            case 4:
                panel.save();
                break;
        }
        return 0;
    }

}

class GUIPanel extends JPanel {
    private BufferedImage canvas;

    public GUIPanel(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
    }

    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }

    public void setPixel(int x, int y, int color) {
        canvas.setRGB(x, y, color);

    }

    public void save() {
        File outputfile = new File("image.jpg");
        try {
            outputfile.createNewFile();
            ImageIO.write(canvas, "jpg", outputfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
