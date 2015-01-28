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
    private String nik,pass;

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

    @Override
    public void run() {
        writer.println("/server Tu eres " + socket.getInetAddress().getHostName());
        writer.println("/server Escribe tu nick");
        try{
            String nick = reader.readLine();
            setNik(nick);
            writer.println("/server Bienvenido " + nick);
            writer.println("/server Hay " + server.numCustomer() + " usuarios conectados");
            writer.println("/server Cuando escribas '/quit', abandonaras la conexión");

            server.sendAllNicksToAllUsers();
            String line = null;
            while ((line = reader.readLine()) != null){
                if (line.equals("/quit")){
                    writer.println("/server Saliendo");
                    socket.close();
                    server.removeCustomer(this);
                    break;
                }

                server.sendToAll("/users " + nick + line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
