package com.yarem.gui;

import javax.swing.*;
import java.awt.*;

public class SwingConsole {
    public static void run(final JFrame jFrame, int height, int width) {
        jFrame.setTitle(jFrame.getClass().getSimpleName());
//        jFrame.getContentPane().setPreferredSize(new Dimension(width, height));
        jFrame.pack();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
