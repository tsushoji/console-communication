package communication.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server {

  private ServerSocket sSocket;
  private Socket socket;
  private InputStream reader;
  private OutputStream writer;
  private boolean running;

  public void coonect(String ip, int port) {
    try {
      sSocket = new ServerSocket();
      sSocket.bind(new InetSocketAddress(ip, port));
      // クライアントからの接続要求を待機
      socket = sSocket.accept();
      reader = socket.getInputStream();
      writer = socket.getOutputStream();
      running = true;
      System.out.println("クライアントとの接続完了");
    }catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void respond(OnDoneServerListener onDoneServerListener, int codeDataSize, int bodySize) {
    try {
      System.out.println("クライアントからの入力開始");
      while(socket.isClosed()) {
        if(!running) {
          break;
        }
        int size = reader.available();
        if(size <= codeDataSize) {
          // クライアントからのデータ受信
          byte[] codeData = new byte[codeDataSize];
          reader.read(codeData, 0, codeDataSize);
          // 成功コールバック
          byte[] body = new byte[bodySize];
          onDoneServerListener.onDone(ByteBuffer.wrap(codeData).getInt(), body);
          writer.write(body);
        }

        Thread.sleep(100);
      }
    }catch(Exception e) {
      // 失敗コールバック
      onDoneServerListener.onFailed();
      e.printStackTrace();
    }
  }

  public void stop() {
    if(running) {
      running = false;
    }

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

      if(sSocket != null) {
        sSocket.close();
        sSocket = null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isConnected() {
    if(socket != null) {
      return socket.isConnected();
    }
    return false;
  }
}
