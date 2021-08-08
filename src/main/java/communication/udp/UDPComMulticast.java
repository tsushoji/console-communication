package communication.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import communication.Console;

public class UDPComMulticast {

  private static int receiveCount = 1;
  private static int sendCount = 1;
  MulticastSocket socket;
  InetAddress group;

  public boolean coonect(String MultiAddres,
                           int port,
                           byte[] sendData,
                           int receivedDataSize) {
    try {
      socket = new MulticastSocket(port);
      group = InetAddress.getByName(MultiAddres);
      socket.joinGroup(group);
      socket.setLoopbackMode(false);
      System.out.println("非同期受信タスク開始");
      new Thread(new Runnable() {
        public void run() {
          startReceiveTask(receivedDataSize);
        }
      }).start();
      new Thread(new Runnable() {
        public void run() {
          startSendTask(sendData, port);
        }
      }).start();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private void startReceiveTask(int dataSize) {
    byte[] receivedData = new byte[dataSize];
    while (true) {
      receive(receivedData);
    }
  }

  private void startSendTask(byte[] sendData, int port) {
    while (true) {
      send(sendData, port);
    }
  }

  private boolean receive(byte[] receivedData) {
    try {
      
      DatagramPacket packet = new DatagramPacket(receivedData, receivedData.length);
      socket.receive(packet);
      System.out.println(
          receiveCount++ +
          "-UDP通信受信文字列:" +
          new String(Arrays.copyOf(packet.getData(), packet.getLength()), "UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean send(byte[] sendData, int port) {
    try {
      DatagramPacket packet = new DatagramPacket(sendData,
                                                  sendData.length,
                                                  group,
                                                  port);
      socket.send(packet);
      System.out.println(sendCount++ + "-UDP通信送信文字列:");
      Console.getInstance().println(sendData);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
