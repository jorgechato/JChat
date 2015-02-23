package org.jorgechato;

import com.bolivia.label.CLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by jorge on 28/01/15.
 */
public class Window implements ActionListener,MouseListener{
    private JPanel chat;
    private JPanel panel1;
    private JPanel contactsRow;
    private JButton send;
    private JTextField textField1;
    private JPanel chatArea;
    private CLabel isConected;
    private JButton disconnect;
    private JTabbedPane tabbedPane1;
    private JScrollPane scrollarea;
    private Contact contact;
    private boolean connected;
    private Socket socket;
    private static int PORT = 6633;
    private PrintWriter out;
    public static BufferedReader in;
    private String nickName,color;
    private static JFrame frame;
    private ArrayList<Contact> arrayUser;
    private int sliderValue = 0;
    public static ArrayList<Contact> outcast;

    public JPanel getContactsRow() {
        return contactsRow;
    }

    public JPanel getChatArea() {
        return chatArea;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public static void main(String[] args) {
        frame = new JFrame("JChat");
        frame.setContentPane(new Window().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public Window() {
        send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        isConected.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        disconnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        send.addActionListener(this);
        isConected.addMouseListener(this);
        disconnect.addActionListener(this);
        isConected.setBackground(Color.decode("#" + Contact.GREEN));

        chatArea.setLayout(new BoxLayout(chatArea,BoxLayout.Y_AXIS));
        contactsRow.setLayout(new BoxLayout(contactsRow,BoxLayout.Y_AXIS));

        arrayUser = new ArrayList<Contact>();
        outcast = new ArrayList<Contact>();

        isConnected();

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (Connection.isConnectionAcepted()) {
                    out.println("/typing");
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (Connection.isConnectionAcepted() && !textField1.getText().equals("")) {
                        if (tabbedPane1.getSelectedIndex() == 0) {
                            out.println("/users " + textField1.getText());
                        }else {
                            out.println("/directMessage;"+ tabbedPane1.getTitleAt(tabbedPane1.getSelectedIndex()) +";"+ textField1.getText());
                        }
                    }
                    textField1.setText("");
                }
            }
        });
        scrollarea.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                JScrollBar bar = scrollarea.getVerticalScrollBar();
                bar.setValue(e.getAdjustable().getMaximum());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == disconnect){
            disconnect();
        }
        if (actionEvent.getSource() == send){
            if (Connection.isConnectionAcepted() && !textField1.getText().equals("")) {
                out.println("/users " + textField1.getText());
                out.print("/allniks");
            }
            textField1.setText("");
        }
    }

    private void disconnect() {
        try {
            out.println("/quit");
            Connection.setConnectionAcepted(false);
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean isConnected() {
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                led();
            }

            private void led() {
                if (Connection.isConnectionAcepted()) {
                    disconnect.setSelected(false);
                    if (!isConected.getBackground().equals(Color.lightGray)) {
                        isConected.setBackground(Color.lightGray);
                    } else {
                        isConected.setBackground(Color.decode("#" + Contact.GREEN));
                        connected = true;
                    }
                }else{
                    isConected.setBackground(Color.decode("#" + Contact.RED));
                    disconnect.setSelected(true);
                }
            }
        });timer.start();

/*
        Timer usersOnLine = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(Connection.isConnectionAcepted() || (Connection.isConnectionAcepted() && textField1.getText().equals("")) )
                    out.println("/allniks");
            }
        });usersOnLine.start();*/

        return connected;
    }

    public void connectToServer(){
        ServerThread thread = new ServerThread(this);
        thread.start();
    }

    public void serverConnection(String host,String nick, String color, char [] pass) {
        try {
            socket = new Socket(host, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Connection.setConnectionAcepted(true);

            out.println(nick);
            out.println(pass);
            out.println(color);
//            frame.setTitle("Hey "+nickName.toUpperCase()+" !");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() == isConected){
            showConnectios();
            return;
        }
    }

    private void showConnectios() {
        new Connection(this).getPanel1();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public static void addToBlackList(Contact contact){
        outcast.add(contact);
    }
    public static void removeFromBlackList(Contact contact){
        outcast.remove(contact);
    }

    public void addChat(String title){
        JPanel chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        tabbedPane1.addTab(title, chatArea);
    }

    public void addChat(Message message){
        JPanel chatArea = (JPanel) tabbedPane1.getComponentAt(1);
        chatArea.add(message.getPanel1());
        chatArea.updateUI();
    }

    public static ArrayList<Contact> getOutcast() {
        return outcast;
    }

    public ArrayList<Contact> getArrayUser() {
        return arrayUser;
    }
}
