/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messageclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ryan & Ribka
 * TODO: make this abstract later?
 */
public class JoinSocket implements Runnable {

    private Socket socket;
    private String host;
    private String userName;

    public JoinSocket(String userhost, String serverHost, int port, String userName) {
        try {
            socket = new Socket(serverHost, port);
            this.host = userhost;
            this.userName = userName;
            PrintWriter joinOutput = null;
            joinOutput = new PrintWriter(socket.getOutputStream(), true);
            joinOutput.println(MesssageType.JOIN + "::" + userName + "::" + host);
        } catch (IOException ex) {
            Logger.getLogger(JoinSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //test for server, use messageTo in actual assignment
    public boolean sendMessage(String message) {
        try {
            PrintWriter joinOutput = null;
            joinOutput = new PrintWriter(socket.getOutputStream(), true);
            joinOutput.println(MesssageType.MESSAGE + "::" + userName + "::" + message);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(JoinSocket.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //use this to broadcast to all, using messageType "BROADCAST"
    public boolean broadcast(String message) {
        try {
            PrintWriter joinOutput = null;
            joinOutput = new PrintWriter(socket.getOutputStream(), true);
            joinOutput.println(MesssageType.BROADCAST + "::" + userName + "::" + message);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(JoinSocket.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //use this to message to specific person, second param should be person to be sent to
    public boolean messageTo(String message/* ADD USER TO BE SENT TO HERE AS SEC PARAM*/) {
        try {
            PrintWriter joinOutput = null;
            joinOutput = new PrintWriter(socket.getOutputStream(), true);
            joinOutput.println(MesssageType.MESSAGETO + "::" + userName + "::" + message);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(JoinSocket.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //use this when disconnecting from chatroom
    public boolean disconnect() {
        try {
            PrintWriter joinOutput = null;
            joinOutput = new PrintWriter(socket.getOutputStream(), true);
            joinOutput.println(MesssageType.DISCONNECT + "::" + userName);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(JoinSocket.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void run() {

    }

}
