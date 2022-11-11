import java.net.*;
import java.io.*;
class Server{
    ServerSocket server;
    //client socket
    Socket socket;
    //to read input data
    BufferedReader br;
    PrintWriter out;
    //Constructor
    public Server(){
        try{
        server = new ServerSocket(7777);
        System.out.println("server is ready to accept connection");
        System.out.println("waiting...");
        socket = server.accept();
            //the input from the socket is read by using buffered reader
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //write data to the socket at client
        out=new PrintWriter(socket.getOutputStream());

        startReading();
        startWriting();
    }catch(Exception e){
        e.printStackTrace();
    }

    }

    public void startReading(){
        //thread to read data from client
        Runnable r1 =()->{
            System.out.println("the reading has started...");
            try{

            while(true){
            String msg = br.readLine();
            if(msg.equals("exit")){
                System.out.println("the client reader has terminated the connection");
                socket.close();
                break;
            }
            System.out.println("Client: "+msg +"\n");
        }


            }catch(Exception e){
               // e.printStackTrace();
               System.out.println("connection is closed");
            }
        };
        new Thread(r1).start();      
    }
    


    public void startWriting(){
        //thread to take data from server and write to client
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
   
    public static void main(String[]args){
        System.out.println("the server is starting");
        new Server();
    }
}