package com.yarem.gui.JPanel;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class FollowMousePanel extends JPanel implements MouseMotionListener {
    private int x, y;
    private int panelWidth;
    private int sizeRec;
    private int previusX;
    private JButton secretButton;
    private ImageIcon background;

    public FollowMousePanel(int panelHeight,JButton secretButton,ImageIcon imageIcon) {
        background = imageIcon;
        this.secretButton = secretButton;
        sizeRec = panelHeight;
        setSize(new Dimension(100,panelHeight));
        setBackground(Color.WHITE);
        addMouseMotionListener(this);
        panelWidth = getWidth();
        setBorder(new LineBorder(new Color(20, 107, 126)));
        setOpaque(false);
    }

    public void mouseMoved(MouseEvent e) {
        previusX = e.getX();
    }

    public void mouseDragged(MouseEvent e) {
        x = previusX - e.getX();
        if((int)getBounds().getX()-x >= 0 && (int)getBounds().getX()+sizeRec-x <= 100){
            if((int)getBounds().getX()-x < 10){
                secretButton.setEnabled(true);
            } else {
                secretButton.setEnabled(false);
            }
            setBounds((int)getBounds().getX()-x,0,sizeRec,sizeRec);
        }
//        System.out.println(e.getX());
//        x = e.getX()-(sizeRec/2);
//        if(x >= 0 && x+sizeRec <= panelWidth){
//            repaint();
//        }
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(),0,0,sizeRec,sizeRec,this);
//        g.setColor(Color.BLUE);
//        g.fillRect(x, 0, sizeRec, sizeRec);
    }

}
