package communication.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

  private Socket socket;
  private InputStream reader;
  private OutputStream writer;

  public void connect(String ip, int port) {
    try {
      // クライアント側ソケット作成
      socket = new Socket(ip, port);
      writer = socket.getOutputStream();
      reader = socket.getInputStream();
    }catch(Exception e) {
      e.printStackTrace();
    }
  }

  public byte[] request(byte[] code, int bodySize) {
    byte[] body = new byte[bodySize];
    try {
      System.out.println("サーバーへ送信開始");
      // サーバーへ送信
      writer.write(code);
      System.out.println("サーバーへ送信終了");
      // サーバーからのデータ受信
      reader.read(body);
    }catch(Exception e) {
      e.printStackTrace();
    }
    return body;
  }

  public void stop() {
    try {
      if(reader != null) {
        reader.close();
        reader = null;
      }

      if(writer != null) {
        writer.close();
        writer = null;
      }

      if(socket != null) {
        socket.close();
        socket = null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
