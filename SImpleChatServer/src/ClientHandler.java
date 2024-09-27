import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.*;

public class ClientHandler implements Runnable{

    private SimpleChatServer server;
    private BufferedReader reader;
    private Socket socket;
    private PrintWriter writer=null;

    public ClientHandler(Socket clientSocket, SimpleChatServer server){
        try{
            this.server= server;
            socket=clientSocket;
            InputStreamReader readerStream=new InputStreamReader(socket.getInputStream());
            reader=new BufferedReader(readerStream);
            writer=new PrintWriter(socket.getOutputStream());
        }catch(Exception ex){ex.printStackTrace();}
    }

    public void run(){
        String message;
        try{
            while((message=reader.readLine())!=null){
                System.out.println(socket.getLocalPort()+": "+message);
                server.tellEveryone(message);
            }
            Thread.sleep(1000);
        }catch(Exception ex){  System.out.println( "ошибка "+ex.getMessage() );
        }finally {
            this.close();
        }
    }

public  void sendMsg(String message){
    try{
        writer.println(message);
        writer.flush();
    }catch(Exception ex){ex.printStackTrace();}
}

    public void close(){

        try{reader.close();
        }catch(Exception ex){System.out.println(" "+ex.getMessage());}

        writer.close();
        server.removeClient(this);
    }
}