package communication.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

import communication.Console;

public class UDPCom {

  private static final int MAX_RECEIVED_DATA_SIZE = 1024;
  private static final UDPCom INSTANCE = new UDPCom();
  private static int receiveCount = 1;
  private static int sendCount = 1;
  private DatagramSocket socket;
  private boolean isReceiveTaskRunning;

  private UDPCom() {}

  public static UDPCom getInstance() {
    return INSTANCE;
  }

  public void coonect(int port, int dataSize) {
    System.out.println("非同期受信タスク開始");
    isReceiveTaskRunning = true;
    new Thread(new Runnable() {
      public void run() {
        startReceiveTask(port, dataSize);
      }
    }).start();
  }

  private void startReceiveTask(int port, int dataSize) {
    while (isReceiveTaskRunning) {
      receive(port, dataSize);
    }
  }

  public void discoonect() {
    stopReceiveTask();
  }

  private void stopReceiveTask() {
    while (isReceiveTaskRunning) {
      isReceiveTaskRunning = false;
    }
    System.out.println("非同期受信タスク停止");
  }

  private boolean receive(int port, int dataSize) {
    try {
      if(socket == null) {
        socket = new DatagramSocket(port);
      }
      byte[] data = new byte[dataSize];
      DatagramPacket packet = new DatagramPacket(data, data.length);
      socket.receive(packet);
      int packetSize = packet.getLength();
      if(packetSize > 0 && packetSize <= MAX_RECEIVED_DATA_SIZE ) {
        byte[] receivedData = packet.getData();
        System.out.println(
            receiveCount +
            "-UDP通信受信文字列:" +
            new String(Arrays.copyOf(receivedData, packetSize), "UTF-8"));
        System.out.println(receiveCount++ + "-UDP通信受信バイナリーデータ:");
        Console.getInstance().println(receivedData);
      }

    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean send(String IPAddres, int port, byte[] sendData) {
    try(DatagramSocket socket = new DatagramSocket()) {
      DatagramPacket packet = new DatagramPacket(sendData,
                                                  sendData.length,
                                                  new InetSocketAddress(IPAddres, port));
      socket.send(packet);
      System.out.println(sendCount++ + "-UDP通信送信バイナリーデータ:");
      Console.getInstance().println(sendData);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
