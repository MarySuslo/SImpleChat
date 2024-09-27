import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.time.LocalTime;

public class ClientSocket {
    private Socket socket;
    private String name;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void close(){
           try{
                socket.close();
            }catch(IOException ex){
                System.out.println(ex.getMessage());
           }
    }
}
