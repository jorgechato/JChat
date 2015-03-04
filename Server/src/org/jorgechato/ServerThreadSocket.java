package org.jorgechato;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

/**
 * Created by jorge on 27/01/15.
 */
public class ServerThreadSocket {
    public static final int PORT = 6633;
    private TrayIcon trayIcon;
    private Main server;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerThreadSocket window = new ServerThreadSocket();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ServerThreadSocket() {
        init();
        start();
    }

    private void init() {
        server = new Main(PORT);
        trayIcon = new TrayIcon(getImage("off.png", "icono"));

        PopupMenu popup = new PopupMenu();
        MenuItem startItem = new MenuItem("Start");
        startItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        MenuItem stopItem = new MenuItem("Stop");
        stopItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        popup.add(startItem);
        popup.add(stopItem);

        trayIcon.setPopupMenu(popup);

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException awte) {
            awte.printStackTrace();
        }
    }

    public void start(){
        Thread serverThread = new Thread(new Runnable() {
            public void run() {
                trayIcon.setImage(getImage("on.png","start"));
                Customer customer = null;

                Main.loadLog();

                try{
                    server.connect();

                    while (server.isConnected()){
                        customer = new Customer(server.listen(),server);
                        boolean isConnected = false;

                        for (Customer customer1 : server.getArrayCustomer())
                            if(customer.getSocket().getInetAddress().getHostAddress().equals(customer1.getSocket().getInetAddress().getHostAddress())){
                                isConnected = true;
                            }

                        if (!isConnected) {
                            server.addCustomer(customer);
                            customer.start();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
    }

    private Image getImage(String ruta, String descripcion) {

        URL url = ServerThreadSocket.class.getResource(ruta);

        if (url == null) {
            System.err.println("Imagen no encontrada: " + ruta);
            return null;
        } else {
            return (new ImageIcon(url, descripcion)).getImage();
        }
    }

    private void stop() {
        try {
            server.disconect();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        trayIcon.setImage(getImage("off.png","stop"));
    }
}
