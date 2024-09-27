import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleChatServer {
    ArrayList<ClientHandler> clientOutputStream;

    public static void main(String[] args) {
        new SimpleChatServer().go();
    }

    public void go(){
        clientOutputStream=new ArrayList<ClientHandler>();
        ServerSocket serverSocket=null;
        Socket clientSocket=null;

        try{
            serverSocket= new ServerSocket(5000);
          while(true){
                clientSocket=serverSocket.accept();

                ClientHandler client=new ClientHandler(clientSocket,this);
                clientOutputStream.add(client);

                Thread thread = new Thread(client);
                thread.start();

                System.out.println("got a connection");
          }
        }catch(Exception ex){ex.printStackTrace();}
        finally {
           try{
               clientSocket.close();
               serverSocket.close();
           }catch(Exception ex){
               System.out.println(ex.getMessage());}
        }
    }

    public void tellEveryone(String message){
        Iterator it= clientOutputStream.iterator();
        for(ClientHandler client:clientOutputStream){
            client.sendMsg(message);
        }
    }

    public void removeClient(ClientHandler client){
        clientOutputStream.remove(client);
    }
}
