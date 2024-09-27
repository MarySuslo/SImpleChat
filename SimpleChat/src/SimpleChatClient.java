import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.html.HTML;
import java.awt.*;
import java.awt.event.*;

public class SimpleChatClient {
    SimpleChatClient clientChat;
    JTextArea incoming;
    JTextField outgoing;

    ClientSocket client=new ClientSocket();
    private BufferedReader reader=null;
    private PrintWriter writer=null;

    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    int x = screenSize.width;
    int y=screenSize.height ;

    public static void main(String[] args) {
        SimpleChatClient clientChat = new SimpleChatClient();
        clientChat.login();
    }

   public void go(){
        JFrame frame= new JFrame("Общий чат");
        JPanel panel = new JPanel();
        incoming=new JTextArea(25,40);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        incoming.setText("-- Вы вошли в чат! --\n");

        JScrollPane qSroller=new JScrollPane(incoming);
        qSroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qSroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing=new JTextField(20);
        JButton sendButton=new JButton("Отправить");

        sendButton.addActionListener(new SendButtonListener());
        outgoing.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                    sendMessage();
            }
        });

        panel.add(qSroller);
        panel.add(outgoing);
        panel.add(sendButton);

       setUpNetworking();

       Thread readThread = new Thread(new IncomingReader());
       readThread.start();

       panel.setBackground(new Color(24, 59, 54));
       frame.getContentPane().add(BorderLayout.CENTER,panel);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       frame.setSize(500,550);
       frame.setLocation((x-frame.getWidth())/2,(y-frame.getHeight())/2);
       frame.addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        writer.println(client.getName()+" покинул(а) чат");
        writer.close();
        }
    });
       frame.setVisible(true);
       outgoing.requestFocus();
   }

   private void setUpNetworking(){
        try{
            client.setSocket(new Socket("127.0.0.1",5000)) ;
            InputStreamReader streamReader= new InputStreamReader(client.getSocket().getInputStream());
            reader=new BufferedReader(streamReader);
            writer=new PrintWriter(client.getSocket().getOutputStream());

            System.out.println("networking established");
            }catch(IOException ex){ex.printStackTrace();
            System.out.println( ex.getMessage());
            }
   }

public void sendMessage(){
    try{
        writer.println(client.getName()+": "+outgoing.getText());
        writer.flush();
    }catch(Exception ex){ex.printStackTrace();}

        outgoing.setText("");
        outgoing.requestFocus();
}

   public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
        sendMessage();
        }
   }

   public class IncomingReader implements Runnable{
     public void run(){
         writer.println(client.getName()+" в чате!");
         writer.flush();

         String message;

         try{
             while((message=reader.readLine())!=null){
              System.out.println("(порт: "+client.getSocket().getLocalPort()+") "+message);

              incoming.append(" "+message+"\n");

             }
         }catch(Exception ex){
             System.out.println( "ошибка "+ex.getMessage() );
         }finally {
             try{
                 reader.close();
             }catch(IOException ex){
                 System.out.println(ex.getMessage());
             }
             client.close();
         }
       }
    }

    public void login(){
        JFrame frame=new JFrame("Авторизация");
        JPanel panel= new JPanel();
        JLabel label=new JLabel();
        JTextField fieldName=new JTextField(20);
        JButton button=new JButton("Сохранить");
        label.setText("Ваше имя:");

        label.setForeground(Color.white);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.setName(fieldName.getText());
                go();
                frame.dispose();
            }
        });
        panel.add(label);
        panel.add(fieldName);
        panel.add(button);
        panel.setBackground(new Color(24, 59, 54));
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.setSize(400,200);
        frame.setLocation((x-frame.getWidth())/2,(y-frame.getHeight())/2);
        frame.setVisible(true);
    }


}
