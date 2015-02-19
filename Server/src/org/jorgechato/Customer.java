package org.jorgechato;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jorge on 27/01/15.
 */
public class Customer extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Main server;
    private String nik,pass,color;

    public Customer(Socket socket,Main server) throws IOException{
        this.socket = socket;
        this.server = server;

        writer = new PrintWriter(socket.getOutputStream(),true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String getNik() {
        return nik;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public void run() {
        writer.println("/server Tu eres " + socket.getInetAddress().getHostName());
        try{
            String nick = reader.readLine();
            setNik(nick);
            String pass = reader.readLine();
            setPass(pass);
            String color = reader.readLine();
            setColor(color);
            writer.println("/server Hay " + server.numCustomer() + " usuarios conectados");

            server.sendAllNicksToAllUsers();
            String line = null;
            while ((line = reader.readLine()) != null){
                if (line.equals("/quit")){
                    socket.close();
                    server.removeCustomer(this);
                    server.sendAllNicksToAllUsers();
                    break;
                }
                if (line.equals("/typing")){
                    server.sendToAll("/typing " + nick);
                }
                if (line.startsWith("/users"))
                    server.sendToAll("/users " + nick + " " + color + " " +line);
            }
        }catch (Exception e){
//            e.printStackTrace();
            server.removeCustomer(this);
            server.sendAllNicksToAllUsers();
        }
    }
}
