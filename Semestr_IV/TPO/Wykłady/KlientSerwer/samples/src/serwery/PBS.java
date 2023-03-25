package serwery;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import javax.swing.JOptionPane;

public class PBS {

  private PhoneDirectory pd = null;
  private ServerSocketChannel ssc = null;
  private Selector selector = null;
  volatile boolean serverIsRunning = true;

  public PBS(PhoneDirectory pd, String host, int port ) {
    this.pd = pd;
    try {

      ssc = ServerSocketChannel.open();


      ssc.configureBlocking(false);


      ssc.socket().bind(new InetSocketAddress(host, port));


      selector = Selector.open();

      ssc.register(selector,SelectionKey.OP_ACCEPT);

    } catch(Exception exc) {
        exc.printStackTrace();
        System.exit(1);
    }
    System.out.println("Server started and ready for handling requests");
    new Thread(new Runnable() {
                 public void run() {
                    JOptionPane.showMessageDialog(null, "Press Ok to stop server");
                    serverIsRunning = false;
                 }
    }).start();   
    serviceConnections();
  }

  private void serviceConnections() {
    long count = 0;  
    while(serverIsRunning) {
      try {

        selector.select();
        count++;             // czy select() naprawde blokuje?
        System.out.println("Iteracja " + count);

        Set keys = selector.selectedKeys();

        Iterator iter = keys.iterator();
        while(iter.hasNext()) {

          SelectionKey key = (SelectionKey) iter.next(); 
          iter.remove();                                 
                                                         

          if (key.isAcceptable()) { 

            SocketChannel cc = ssc.accept();


            cc.configureBlocking(false);

            cc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            continue;
          }

          if (key.isReadable()) { 
            SocketChannel cc = (SocketChannel) key.channel();
            serviceRequest(cc);
            continue;
          }
        }
      } catch(Exception exc) {
          exc.printStackTrace();
          continue;
      }
    }
    System.out.println("liczba wykonan petli " + count);
  }

  private static Pattern reqPatt = Pattern.compile(" +", 3);

  private static String msg[] = { "Ok", "Invalid request", "Not found",
                                  "Couldn't add - entry already exists",
                                  "Couldn't replace non-existing entry",
                                  };


  private static Charset charset  = Charset.forName("ISO-8859-2");
  private static final int BSIZE = 1024;


  private ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);


  private StringBuffer reqString = new StringBuffer();


  private void serviceRequest(SocketChannel sc) {
    if (!sc.isOpen()) return; 


    reqString.setLength(0);
    bbuf.clear();
    try {
      readLoop:                    
      while (true) {               
        int n = sc.read(bbuf);     
        if (n > 0) {
          bbuf.flip();
          CharBuffer cbuf = charset.decode(bbuf);
          while(cbuf.hasRemaining()) {
            char c = cbuf.get();
            if (c == '\r' || c == '\n') break readLoop;
            reqString.append(c);
          }
        }
      }

    String[] req = reqPatt.split(reqString, 3);
      String cmd = req[0];

      if (cmd.equals("bye")) {             
          writeResp(sc, 0, null);          
          sc.close();                      
          sc.socket().close();
      }
      else if (cmd.equals("get")) {
        if (req.length != 2) writeResp(sc, 1, null);
        else {
          String phNum = (String) pd.getPhoneNumber(req[1]);
          if (phNum == null) writeResp(sc, 2, null);
          else writeResp(sc, 0, phNum);
        }
      }
      else if (cmd.equals("add"))  {
        if (req.length != 3) writeResp(sc, 1, null);
        else {
          boolean added = pd.addPhoneNumber(req[1], req[2]);
          if (added) writeResp(sc, 0, null);
          else writeResp(sc, 3, null);
        }
      }
      else if (cmd.equals("replace"))  {
        if (req.length != 3) writeResp(sc, 1, null);
        else {
          boolean replaced = pd.replacePhoneNumber(req[1], req[2]);
          if (replaced) writeResp(sc, 0, null);
          else writeResp(sc, 4, null);
        }
      }
      else writeResp(sc, 1, null);         

    } catch (Exception exc) {              
        exc.printStackTrace();
        try { sc.close();
              sc.socket().close();
        } catch (Exception e) {}
    }
  }

  private StringBuffer remsg = new StringBuffer(); 

  private void writeResp(SocketChannel sc, int rc, String addMsg)
                         throws IOException {
    remsg.setLength(0);
    remsg.append(rc);
    remsg.append(' ');
    remsg.append(msg[rc]);
    remsg.append('\n');
    if (addMsg != null) {
      remsg.append(addMsg);
      remsg.append('\n');
    }
    ByteBuffer buf = charset.encode(CharBuffer.wrap(remsg));
    sc.write(buf);
  }

  public static void main(String[] args) {
    try {
      String phdFileName = args[0];
      String host = args[1];
      int port = Integer.parseInt(args[2]);

      PhoneDirectory pd = new PhoneDirectory(phdFileName);
      new PBS(pd, host, port);
    } catch(Exception exc) {
        exc.printStackTrace();
        System.exit(1);
    }
  }

}
