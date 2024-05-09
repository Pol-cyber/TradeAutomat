package com.yarem.gui.JPanel;

import com.yarem.dbConnect.HibernateSessionFactory;
import com.yarem.dbConnect.entity.AutomatAdmin;
import com.yarem.gui.JbuttonClass.AdmitToolButton;
import org.hibernate.Session;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminPanel extends JPanel {

    private JPanel checkAdmin;
    private AdmitToolButton adminTool;

    AdminPanel(){
        setBackground(Color.LIGHT_GRAY);
        setBorder(new LineBorder(Color.cyan));
        setLayout(new BorderLayout());
    }

    public void setPanel(AdmitProductPanel admitProductPanel){
        checkAdmin = new AuthorizePanel(this,admitProductPanel);
        adminTool = new AdmitToolButton();
        setCheckPanel();
    }

    public void setCheckPanel(){
        List<Component> components = getComponentsList();
        if(components.contains(adminTool)){
            remove(adminTool);
            add(checkAdmin);
            revalidate();
            repaint();
        } else {
            add(checkAdmin);
        }
    }

    public void setAdminTool(){
        List<Component> components = getComponentsList();
        if(components.contains(checkAdmin)){
            remove(checkAdmin);
            add(adminTool);
            revalidate();
            repaint();
        } else {
            add(adminTool);
        }
    }

    public List<Component> getComponentsList(){
        return new ArrayList<>(Arrays.asList(this.getComponents()));
    }

    public boolean checkAuthorize(){
        if(getComponentsList().contains(checkAdmin)){
            return false;
        } else {
            return true;
        }
    }
}


class AuthorizePanel extends JPanel {

    AuthorizePanel(AdminPanel adminPanel, AdmitProductPanel admitProductPanel){
        setLayout(null);

        JLabel authorizeText = new JLabel("Авторизуйтесь");
        authorizeText.setFont(new Font("TimesRoman", Font.BOLD, 23));
        authorizeText.setBounds(25,0,200,60);

        JLabel putLogin = new JLabel("Введіть логін");
        putLogin.setFont(new Font("TimesRoman", Font.PLAIN, 19));
        putLogin.setBounds(10,45,150,30);

        JTextField loginText = new JTextField("");
        loginText.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        loginText.setBounds(10,75,205,25);
        PromptSupport.setPrompt("Ваш логін...", loginText);
        PromptSupport.setForeground(new Color(139, 169, 129), loginText);

        JLabel putPassword = new JLabel("Введіть пароль");
        putPassword.setFont(new Font("TimesRoman", Font.PLAIN, 19));
        putPassword.setBounds(10,100,150,50);

        JTextField passwordText = new JTextField("");
        passwordText.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        passwordText.setBounds(10,140,205,25);
        PromptSupport.setPrompt("Ваш пароль...", passwordText);
        PromptSupport.setForeground(new Color(139, 169, 129), passwordText);

        JButton checkValid = new JButton("ВХІД");
        checkValid.setBackground(Color.cyan);
        checkValid.setFont(new Font("TimesRoman", Font.BOLD, 20));
        checkValid.setBounds(65,180,100,40);

        ActionListener checkValidData = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Session session = HibernateSessionFactory.openSession();
                session.getTransaction().begin();
                AutomatAdmin automatAdmin = session.find(AutomatAdmin.class,loginText.getText());
                session.getTransaction().commit();
                session.close();
                if(automatAdmin != null && automatAdmin.getPassword().equals(passwordText.getText())){
                    adminPanel.setAdminTool();
                    for(Component c : admitProductPanel.getComponents()){
                        c.setEnabled(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Ви ввели невірні дані");
                }
                passwordText.setText("");
                loginText.setText("");
            }
        };

        checkValid.addActionListener(checkValidData);

        add(authorizeText);
        add(putLogin);
        add(loginText);
        add(putPassword);
        add(passwordText);
        add(checkValid);
    }
}
