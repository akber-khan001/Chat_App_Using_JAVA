import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading =new JLabel("Client Area");
    private JTextArea messageArea =new JTextArea();
    private JTextField messageInput =new JTextField();
    private Font font =new Font("Roboto",Font.PLAIN,30);
    //constructor
    public Client(){
        try{
            System.out.println("sending request to server");
            socket =new Socket("127.0.0.1",7777);
            System.out.println("connnection established");
            br =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =new PrintWriter(socket.getOutputStream());
                createGUI();
                handleEvents();
            startReading();
            //startWriting();

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("key realesed: "+e.getKeyCode());
                if(e.getKeyCode()==10){
                    //System.out.println("you have pressed enter");
                    String contentToSend = messageInput.getText();
                    messageArea.append(contentToSend);
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }

        });
    }
    private void createGUI(){
        //GUI code
     this.setTitle("Client Messenger[END]"); 
     this.setSize(600,700);  
     this.setLocationRelativeTo(null);    
     this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     //code for components
     heading.setFont(font);
     messageArea.setFont(font);
     messageInput.setFont(font);
     heading.setIcon(new ImageIcon("chatLogo2.png"));
     heading.setHorizontalAlignment(SwingConstants.CENTER);
     heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
     //we cant edit the mssg area
        messageArea.setEditable(false);


        //setting layout of frame
     this.setLayout(new BorderLayout());

     //adding components to frame
     this.add(heading,BorderLayout.NORTH);
     JScrollPane jScrollPane= new JScrollPane(messageArea);
     this.add(jScrollPane,BorderLayout.CENTER);
     this.add(messageInput,BorderLayout.SOUTH);

     this.setVisible(true);
    }
    public void startReading(){
        //thread to read data from server
        Runnable r1 =()->{
            System.out.println("the reading has started...");
            try{
            while(true){
                
            String msg = br.readLine();
            if(msg.equals("exit")){
                System.out.println("the server reader has terminated the connection");
                JOptionPane.showMessageDialog(this,"server terminated the chat");
                messageInput.setEnabled(false);
                socket.close();
                break;
            }
           // System.out.println("server: "+msg);   
           messageArea.append("Server: " + msg + "\n");
        }
    }catch(Exception e){
       // e.printStackTrace();
       System.out.println("connection is closed");
    }
            
    };
    new Thread(r1).start();
}
public void startWriting(){
    //thread to take data from client and write to server
    Runnable r2=()->{
        System.out.println("writer started...");
        try{
        while(!socket.isClosed()){
           
                //taking input from the keyboard
                BufferedReader br1 =new BufferedReader(new InputStreamReader(System.in));
                String content =br1.readLine();
                out.println(content);
                //if data isnt going then we forcefully flush the data
                out.flush();
                if(content.equals("exit")){
                    socket.close();
                    break;
                }
           
        }
        System.out.println("connection is closed");
    }catch(Exception e){
        e.printStackTrace();
    }

    };
    new Thread(r2).start();
}
    public static void main(String[] args) {
        System.out.println("this is client...");
        new Client();
        
    }
    
}
