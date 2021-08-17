package communication.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import communication.Console;

public class UDPComMulticast {

  private static final long SCHEDULED_SEND_SLEEP_TIME = 15000;
  private static final int MAX_RECEIVED_DATA_SIZE = 1024;
  private static final UDPComMulticast INSTANCE = new UDPComMulticast();
  private static int receiveCount = 1;
  private static int sendCount = 1;
  MulticastSocket socket;
  InetAddress group;
  private boolean isReceiveTaskRunning;
  private boolean isSendTaskRunning;

  private UDPComMulticast() {}

  public static UDPComMulticast getInstance() {
    return INSTANCE;
  }

  public boolean coonect(String MultiAddres,
                           int sendPort,
                           int receivePort,
                           byte[] sendData,
                           int receivedDataSize) {
    try {
      if(socket == null) {
        socket = new MulticastSocket(receivePort);
        group = InetAddress.getByName(MultiAddres);
        socket.joinGroup(group);
        socket.setLoopbackMode(false);
      }
      System.out.println("非同期受信タスク開始");
      isReceiveTaskRunning = true;
      new Thread(new Runnable() {
        public void run() {
          startReceiveTask(receivedDataSize);
        }
      }).start();
      System.out.println("非同期送信タスク開始");
      isSendTaskRunning = true;
      new Thread(new Runnable() {
        public void run() {
          startSendTask(sendData, sendPort);
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
    while (isReceiveTaskRunning) {
      receive(receivedData);
    }
  }

  private void startSendTask(byte[] sendData, int port) {
    while (isSendTaskRunning) {
      try {
        send(sendData, port);
        Thread.sleep(SCHEDULED_SEND_SLEEP_TIME);
      } catch (InterruptedException e) {}
    }
  }

  public void discoonect() {
    stopReceiveTask();

    stopSendTask();
  }

  private void stopReceiveTask() {
    while (isReceiveTaskRunning) {
      isReceiveTaskRunning = false;
    }
    System.out.println("非同期受信タスク停止");
  }

  private void stopSendTask() {
    while (isSendTaskRunning) {
      isSendTaskRunning = false;
    }
    System.out.println("非同期送信タスク停止");
  }

  private boolean receive(byte[] data) {
    try {
      DatagramPacket packet = new DatagramPacket(data, data.length);
      socket.receive(packet);
      int packetSize = packet.getLength();
      if(packetSize > 0 && packetSize <= MAX_RECEIVED_DATA_SIZE) {
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

  public boolean send(byte[] sendData, int port) {
    try {
      DatagramPacket packet = new DatagramPacket(sendData,
                                                  sendData.length,
                                                  group,
                                                  port);
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
