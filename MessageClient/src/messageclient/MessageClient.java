/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messageclient;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author Ryan & Ribka
 */
//TODO:
//- GUI to connect to the server
//- Holds a list of other currently connected clients obtained via UDP
//- Send to specific other client, or broadcast to all via TCP
//- Userlist should be updated on GUI via receiving UDP datagrams from server
//- GUI will notify when a user has connected/disconnected
//- 

public class MessageClient implements Runnable{
    
    private static final int JOIN_PORT = 84;
    private Socket sendMessageSocket = null;
    private InetAddress localAddress ;
    private String serverAddress; 
    private JoinSocket serverSocket;
    
    //GUI contents
   public static JFrame mainFrame = null;
   public static JTextArea chatText = null;
   public static JTextField chatBar = null;
   public static JLabel statusBar = null;
   public static JRadioButton guestOption = null;
   public static JRadioButton broadcastOption = null;
   public static JRadioButton lobby1 = null;
   public static JRadioButton lobby2 = null;
   public static JRadioButton lobby3 = null;
   public static JButton sendButton = null;
   

    MessageClient(){
        try {
            localAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MessageClient client = new MessageClient();
        String ipString = JOptionPane.showInputDialog("Enter Server IP Adress:");
        client.setServerAddress(ipString);
        String username = JOptionPane.showInputDialog("Enter your username");
        //String username = JOptionPane.showInputDialog("Username already taken, please enter a new unique one");
        client.setUserName(username, client.serverAddress);
        //Run the chat program GUI
        initGUI();
        /*
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("mess1");
         client.sendMessage("Hello World");
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("mess2");
        client.sendMessage("Im Cool");
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("mess3");
        client.sendMessage("Hello Joe");
        */
       
    }
    
     public void setUserName(String userName, String serverHost){
         serverSocket = new JoinSocket(localAddress.getHostAddress(), serverHost, JOIN_PORT, userName);
     }
    
     public void sendMessage(String message){
         serverSocket.sendMessage(message);
     }
     
     //for messaging specific user
     public void messageTo(String message /*add receiver ID here*/){
         serverSocket.messageTo(message);
     }
     
     //this will send to all
     public void broadcast(String message){
         serverSocket.broadcast(message);
     }
     
     public void disconnect(){
         serverSocket.disconnect();
     }
    
    public void setServerAddress(String ipAddress){
        serverAddress = ipAddress;
    }
    
    public static void initGUI(){
        ActionAdapter keyListener = null;
        //set up lobby pane
        JPanel lobbyPane = initLobby();
        //set up chat pane
        JPanel chatPane = new JPanel(new BorderLayout());
        chatText = new JTextArea(15, 30);
        chatText.setLineWrap(true);
        chatText.setEditable(true);
        JScrollPane textPane = new JScrollPane(chatText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //Im not sure if this is how you do it
        keyListener = new ActionAdapter() {
            public void actionPerformed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    //TODO: run a check, see what radio button is currently selected
                    //broadcast(chatText.getSelectedText());
                }
            }
        };
        chatBar = new JTextField();
        chatBar.setEnabled(true);
        //chatBar.setActionCommand("message");
        chatBar.addActionListener(keyListener);
        chatPane.add(chatBar, BorderLayout.SOUTH);
        chatPane.add(textPane, BorderLayout.CENTER);
        chatPane.setPreferredSize(new Dimension(200,200));
        //Set up main Pane/where panels are
        JPanel mainPane = new JPanel(new BorderLayout());
        //mainPane.add(statusBar, BorderLayout.SOUTH);
        mainPane.add(lobbyPane, BorderLayout.EAST);
        mainPane.add(chatPane, BorderLayout.CENTER);
        
        //set up main frame
        mainFrame = new JFrame("Chatroom");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(mainPane);
        mainFrame.setSize(mainFrame.getPreferredSize());
        mainFrame.setLocation(200,200);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    
    public static JPanel initLobby(){
        JPanel pane = null;
        JPanel optionsPane = new JPanel(new GridLayout(1, 1));
        ButtonGroup bg = new ButtonGroup();
      /*hostOption = new JRadioButton("Host", false); //not sure if this is needed
      hostOption.setMnemonic(KeyEvent.VK_H);*/
      //below are radio buttons, where if you select one option, you will message to them
      //e.g ticking broadcast will send to all, lobby1 will mesgae 'lobby1' etc
      broadcastOption = new JRadioButton("Broadcast", true);
      lobby1 = new JRadioButton("User2", true);    //change these from hardcoded to respective lobbier's name
      lobby2 = new JRadioButton("User3", true);    //change these from hardcoded to respective lobbier's name
      lobby3 = new JRadioButton("User4", true);
      //add buttons to button group
      bg.add(broadcastOption);
      bg.add(lobby1);
      bg.add(lobby2);
      bg.add(lobby3);
      pane = new JPanel(new GridLayout(4, 1));
      pane.add(broadcastOption);
      pane.add(lobby1);
      pane.add(lobby2);
      pane.add(lobby3);
      optionsPane.add(pane);

      return optionsPane;
    }
        
  /*private void enterPress(java.awt.event.KeyEvent evt, String message){
      if(evt.getKeyCode()==KeyEvent.VK_ENTER){
          broadcast(message);
      }
  }*/
    static class ActionAdapter implements ActionListener {

        public void actionPerformed(ActionEvent e) {
        }
    }

    @Override
    public void run() {
        try{
            PrintWriter messageOutput = new PrintWriter(sendMessageSocket.getOutputStream(),true);
            messageOutput.println("Joing" ); 
            
            
        } catch (SocketException ex) {
           Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
            Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
