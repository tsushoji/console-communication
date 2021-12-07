package communication.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server {

  private ServerSocket sSocket;
  private Socket socket;
  private InputStream reader;
  private OutputStream writer;
  private boolean running;

  public void connect(int port) {
    try {
      sSocket = new ServerSocket(port);
      // クライアントからの接続要求を待機
      socket = sSocket.accept();
      reader = socket.getInputStream();
      writer = socket.getOutputStream();
      running = true;
      System.out.println("クライアントとの接続完了");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void respond(OnDoneServerListener onDoneServerListener, int codeDataSize, int bodySize) {
    try {
      System.out.println("クライアントからの入力開始");
      while (running && !socket.isClosed()) {
        int size = reader.available();
        if (size <= codeDataSize) {
          // クライアントからのデータ受信
          byte[] codeData = new byte[codeDataSize];
          reader.read(codeData, 0, codeDataSize);
          // 終了 !!!!:555819297(ASKII)
          if (ByteBuffer.wrap(codeData).getInt() == 555819297) {
            stop();
            System.out.println("クライアントからの入力終了");
          } else {
            // 成功コールバック
            byte[] body = onDoneServerListener.onDone(codeData, bodySize);
            writer.write(body);
          }
        }

        Thread.sleep(100);
      }
    } catch (Exception e) {
      // 失敗コールバック
      onDoneServerListener.onFailed();
      e.printStackTrace();
    }
  }

  public void stop() {
    try {
      if (running) {
        running = false;

        Thread.sleep(2000);
      }

      if (reader != null) {
        reader.close();
        reader = null;
      }

      if (writer != null) {
        writer.close();
        writer = null;
      }

      if (socket != null) {
        socket.close();
        socket = null;
      }

      if (sSocket != null) {
        sSocket.close();
        sSocket = null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public boolean isConnected() {
    if (socket != null) {
      return socket.isConnected();
    }
    return false;
  }
}
