package com.yarem.gui.ClassLabel;

import javax.swing.*;
import java.awt.*;

public class CircleLabel extends JLabel {

    private int number = 0;

    public CircleLabel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        g.setColor(new Color(7, 119, 32));
        g.fillOval(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String text = Integer.toString(number);
        int textWidth = g.getFontMetrics().stringWidth(text);
        int textHeight = g.getFontMetrics().getHeight();
        g.drawString(text, (width - textWidth) / 2, (height + textHeight / 2) / 2);
    }

    public void setNumber(int number){
        this.number = number;
        if(number > 0){
            setVisible(true);
        } else {
            setVisible(false);
        }
        repaint();
    }

    public int getNumber() {
        return number;
    }
}
