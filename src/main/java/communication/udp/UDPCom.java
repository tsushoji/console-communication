package communication.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class UDPCom {

  private static int receiveCount = 1;
  private static int sendCount = 1;

  public void coonect(int port, int dataSize) {
    System.out.println("非同期受信タスク開始");
    new Thread(new Runnable() {
      public void run() {
        startReceiveTask(port, dataSize);
      }
    }).start();
  }

  private void startReceiveTask(int port, int dataSize) {
    while (true) {
      receive(port, dataSize);
    }
  }

  private boolean receive(int port, int dataSize) {
    try (DatagramSocket socket = new DatagramSocket(port)) {
      byte[] receivedData = new byte[dataSize];
      DatagramPacket packet = new DatagramPacket(receivedData, receivedData.length);
      socket.receive(packet);
      System.out.println(
          receiveCount++ + "-UDP通信受信文字列:" + new String(Arrays.copyOf(packet.getData(), packet.getLength()), "UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean send(String IPAddres, int port, byte[] sendData) {
    try (DatagramSocket socket = new DatagramSocket()) {
      DatagramPacket packet = new DatagramPacket(sendData, sendData.length, new InetSocketAddress(IPAddres, port));
      socket.send(packet);
      System.out.println(sendCount++ + "-UDP通信送信文字列:" + sendData);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
