package com.yarem.gui.JPanel;

import com.yarem.dbConnect.HibernateSessionFactory;
import com.yarem.dbConnect.entity.Category;
import com.yarem.dbConnect.entity.Product;
import com.yarem.gui.JbuttonClass.ButtonProduct;
import org.hibernate.Session;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.transaction.Status;
import javax.validation.ConstraintViolationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdmitProductPanel extends JPanel {

    AdmitProductPanel(AdminPanel checkAdminPanel){
        setBackground(Color.black);
        GridLayout layout = new GridLayout(5, 4);
        layout.setHgap(3);
        layout.setVgap(3);
        setLayout(layout);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkAdminPanel.checkAuthorize()){
                    WindowForInputData windowForInputData = new WindowForInputData((ButtonProduct) e.getSource());
                    windowForInputData.setModal(true);
                    windowForInputData.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(AdmitProductPanel.this,"Будь-ласка спочатку авторизуйтесь.");
                    for(Component c : AdmitProductPanel.this.getComponents()){
                        c.setEnabled(false);
                    }
                }
            }
        };

        // продукти
        for(int i = 0; i < 20; i++){
            ButtonProduct jButton = new ButtonProduct(new ImageIcon("src/main/resources/images/plus.jpg"), i);
            jButton.addActionListener(actionListener);
            add(jButton);
        }
    }
}



class WindowForInputData extends JDialog {

    private JButton putData = new JButton("ВНЕСТИ");

    public WindowForInputData(ButtonProduct sourceButton){
        super();
        setTitle("Внесення продукту у базу");
        setSize(new Dimension(200,200));
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        putData.setEnabled(false);
        putData.setPreferredSize(new Dimension(400,30));

        InputPanel inputPanel = new InputPanel();

        JPanel jPanelForPubData = new JPanel();
        jPanelForPubData.add(putData);
        jPanelForPubData.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(inputPanel);
        add(BorderLayout.PAGE_END,jPanelForPubData);

        putData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inputPanel.nameProdField.getText().length() < 1 || inputPanel.countProdField.getText().length() < 1 || inputPanel
                        .priceProdField.getText().length() < 1){
                    JOptionPane.showMessageDialog(null,"Будь-ласка заповніть усі обов'язкові стовпці - ім'я, к-сть, вартість");
                    return;
                }
                if(inputPanel.descriptionProdField.getText().length() > 20){
                    JOptionPane.showMessageDialog(null,"Опис продукту не може бути більше 20 символів.");
                    return;
                }
                Session session = HibernateSessionFactory.openSession();
                try {
                    session.getTransaction().begin();
                    Product productWithThisId = session.find(Product.class,inputPanel.nameProdField.getText());
                    session.getTransaction().commit();
                    if(productWithThisId != null){
                        JOptionPane.showMessageDialog(null,"У базі даних уже є даний продукт, якщо ви бажаєте змінити його, то оберіть відповідне меню.");
                        return;
                    }
                } catch (Exception ex){
                    System.err.println(ex);
                    session.getTransaction().rollback();
                    return;
                } finally {
                    session.close();
                }

                ImageIcon imageIcon = inputPanel.imageIcon;
                BufferedImage bufferedImage = new BufferedImage(
                        imageIcon.getIconWidth(),
                        imageIcon.getIconHeight(),
                        BufferedImage.TYPE_INT_RGB
                );
                Graphics2D g2d = bufferedImage.createGraphics();
                imageIcon.paintIcon(null, g2d, 0, 0);
                g2d.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(bufferedImage, "png", baos);
                } catch (IOException ex) {
                    throw new RuntimeException("При завантажені зображення продукту виникла помилка");
                }
                byte[] imageBytes = baos.toByteArray();

                Category category;
                session = HibernateSessionFactory.openSession();
                try {
                    session.getTransaction().begin();
                    // назва категорії в БД унікальна тому помилок із SingleResult бути немає
                    category = (Category) session.createQuery("SELECT c FROM Category  c WHERE c.categoryName = :categParam").setParameter("categParam",
                            inputPanel.categoryProdComboBox.getSelectedItem()).getSingleResult();
                    session.getTransaction().commit();
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"При звернені до БД щоб отримати категорію виникла помилка.");
                    try {
                        session.getTransaction().rollback();
                    } catch (Exception rbEx) {
                        System.err.println("Rollback of transaction failed, trace follows!");
                        rbEx.printStackTrace(System.err);
                    }
                    return;
                } finally {
                   session.close();
                }

                if(category == null){
                    JOptionPane.showMessageDialog(null,"При завантажені категорії із бази даних виникла помилка - будь-ласка " +
                            "зверніться до програмістів.");
                    return;
                }

                session = HibernateSessionFactory.openSession();
                try {
                    Product product = new Product(inputPanel.nameProdField.getText(), inputPanel.descriptionProdField.getText(),
                            Integer.parseInt(inputPanel.countProdField.getText()), sourceButton.getIndex(), imageBytes, false,
                            Double.parseDouble(inputPanel.priceProdField.getText()), category);
                    session.getTransaction().begin();
                    session.persist(product);
                    session.getTransaction().commit();
                    JOptionPane.showMessageDialog(null, "Продукт успішно додано!");

                } catch (NumberFormatException ne){
                    JOptionPane.showMessageDialog(null,"Ви ввели неправильний формат для к-сті чи вартості. Вводьте числові дані.");
                } catch (ConstraintViolationException cv) {
                    JOptionPane.showMessageDialog(null, "Значення к-сті повинно бути більше або рівне 0, а значення ціни суворо більше 0.");
                    try {
                        session.getTransaction().rollback();
                    } catch (Exception rbEx) {
                        System.err.println("Rollback of transaction failed, trace follows!");
                        rbEx.printStackTrace(System.err);
                    }
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"При додаванні продукту у базу даних виникла помилка.");
                    try {
                        session.getTransaction().rollback();
                    } catch (Exception rbEx) {
                        System.err.println("Rollback of transaction failed, trace follows!");
                        rbEx.printStackTrace(System.err);
                    }
                } finally {
                    session.close();
                }
            }
        });

        revalidate();
        repaint();
        pack();
    }

    private class InputPanel extends JPanel{
        private ImageIcon imageIcon;
        private JComboBox<String> categoryProdComboBox;
        private InputField nameProdField;
        private InputField descriptionProdField;
        private InputField countProdField;
        private InputField priceProdField;
        InputPanel(){
            GridLayout gridLayout = new GridLayout(2,5);
            gridLayout.setHgap(6);
            gridLayout.setVgap(6);
            setLayout(gridLayout);

            NameField nameProd = new NameField("Імя");
            nameProdField = new InputField();

            NameField descriptionProd = new NameField("Опис");
            descriptionProdField = new InputField();
            PromptSupport.setPrompt("Менше 20 символів...", descriptionProdField);

            NameField countProd = new NameField("К-сть");
            countProdField = new InputField();

            NameField priceProd = new NameField("Вартість");
            priceProdField = new InputField();


            Session session = HibernateSessionFactory.openSession();
            session.getTransaction().begin();
            List<Category> categories = session.createQuery("SELECT c FROM Category c").getResultList();
            session.getTransaction().commit();
            session.close();

            JPanel panelCategory = new JPanel();
            panelCategory.setLayout(new GridLayout(1,2,3,0));
            NameField categoryProd = new NameField("Категорія");
            categoryProdComboBox = new JComboBox<>();
            if(categories.size() == 0){
                categoryProdComboBox.setEnabled(false);
            } else {
                for (Category c : categories){
                    categoryProdComboBox.addItem(c.getCategoryName());
                }
            }
            categoryProdComboBox.setMaximumRowCount(5);
            JButton plusCategory = new JButton("Нова категорія");
            JScrollPane jScrollPane = new JScrollPane(categoryProdComboBox);
            panelCategory.add(jScrollPane);
            panelCategory.add(plusCategory);


            NameField chooseFileImage = new NameField("Оберіть файл");
            JButton buttonChooseFile = new JButton("Нажміть");
            buttonChooseFile.setFont(new Font("TimesRoman", Font.ITALIC, 14));
            buttonChooseFile.setPreferredSize(new Dimension(150,30));

            add(nameProd);
            add(descriptionProd);
            add(countProd);
            add(priceProd);
            add(categoryProd);
            add(chooseFileImage);
            add(nameProdField);
            add(descriptionProdField);
            add(countProdField);
            add(priceProdField);
            add(panelCategory);
            add(buttonChooseFile);
            buttonChooseFile.setToolTipText("Можливі розширення файлів - jpg, jpeg, png. Максимальний розмір 800x800");

            buttonChooseFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Images", "jpg", "jpeg", "png");
                    fileChooser.setFileFilter(filter);
                    int returnVal = fileChooser.showOpenDialog(InputPanel.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        imageIcon = new ImageIcon(file.getAbsolutePath());
                        if((imageIcon.getIconHeight() > 800 || imageIcon.getIconWidth() > 800)){
                            imageIcon = null;
                            JOptionPane.showMessageDialog(null,"Розмір файлу не підтримується.");
                            return;
                        } else if((imageIcon.getIconWidth() <= 0 || imageIcon.getIconHeight() <= 0)){
                            imageIcon = null;
                            JOptionPane.showMessageDialog(null,"Ми не можемо завантажити ваше зображення. Будь-ласка змініть його.");
                            return;
                        }
                        putData.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(InputPanel.this,"З вашим файлом виникла помилка");
                    }
                }
            });

            plusCategory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = JOptionPane.showInputDialog(null,"Введіть назву нової категорії");
                    if(s == null || s.length() <= 0){
                        JOptionPane.showMessageDialog(null,"Введіть коректну назву категорії.");
                        return;
                    }
                    Session session  = HibernateSessionFactory.openSession();
                    session.getTransaction().begin();
                    List categoryWithThisName = session.createQuery("SELECT c FROM Category c WHERE c.categoryName = :newCategry").
                            setParameter("newCategry",s).getResultList();
                    session.getTransaction().commit();
                    session.close();

                    if(categoryWithThisName.size() > 0){
                        JOptionPane.showMessageDialog(null,"Ви ввели категорію яка вже існує");
                        return;
                    } else {
                        try {
                            session = HibernateSessionFactory.openSession();
                            session.getTransaction().begin();
                            Category category = new Category();
                            category.setCategoryName(s);
                            session.persist(category);
                            session.getTransaction().commit();
                            categoryProdComboBox.addItem(category.getCategoryName());
                        } catch (Exception ex){
                            JOptionPane.showMessageDialog(null,"При внесені даних у БД виникла помилка - повторіть спробу.");
                            try {
                                    session.getTransaction().rollback();
                            } catch (Exception rbEx) {
                                System.err.println("Rollback of transaction failed, trace follows!");
                                rbEx.printStackTrace(System.err);
                            }
                            return;
                        } finally {
                            session.close();
                        }
                        JOptionPane.showMessageDialog(null,"Категорію успішно додано");
                        categoryProdComboBox.setEnabled(true);
                    }

                }
            });

        }
    }

    private static class NameField extends JLabel{
        NameField(String s){
            super(s);
            setFont(new Font("TimesRoman", Font.BOLD, 17));
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private static class InputField extends JTextField {
        InputField(){
            setPreferredSize(new Dimension(100,20));
        }
    }
}