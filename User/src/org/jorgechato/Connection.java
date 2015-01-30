package org.jorgechato;

import org.apache.commons.validator.routines.UrlValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created by jorge on 28/01/15.
 */
public class Connection extends JDialog implements ActionListener{

    private JPanel panel1;
    private JTextField txthost;
    private JButton accept;
    private JButton cancel;
    private JTextField txtName;
    private static String host,nickname;
    private static boolean connectionAcepted;
    private Window window;

    public Connection(Window window) {
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);

        this.window = window;

        init();
    }

    private void init() {
        accept.addActionListener(this);
        cancel.addActionListener(this);
        accept.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        txthost.setText("localhost");
        String [] name = new String[10];
        name[0] = "Jorge";
        name[1] = "Sofia";
        name[2] = "Isabel";
        name[3] = "Daniel";
        name[4] = "David";
        name[5] = "Maria";
        name[6] = "Irene";
        name[7] = "Emma";
        name[8] = "Eva";
        name[9] = "Nacho";

        Random ran = new Random();
        txtName.setText(name[ran.nextInt(10)]);
    }

    public JPanel getPanel1() {
        setVisible(true);
        return panel1;
    }

    public static boolean isConnectionAcepted() {
        return connectionAcepted;
    }

    public static void setConnectionAcepted(boolean connectionAcepted) {
        Connection.connectionAcepted = connectionAcepted;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == accept){
            host();
            return;
        }
        if (actionEvent.getSource() == cancel){
            setVisible(false);
            return;
        }
    }

    private void host() {
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(txthost.getText()) && !txthost.getText().equals("localhost")){
            JOptionPane.showMessageDialog(null, "URL no válida",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            connectionAcepted = false;
            return;
        }
        host = txthost.getText();
        nickname = txtName.getText();
        window.connectToServer();
        setVisible(false);
    }

    public static String getNickname() {
        return nickname;
    }

    public static String getHost() {
        return host;
    }
}
