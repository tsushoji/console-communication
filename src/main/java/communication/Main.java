package communication;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import communication.tcp.Server;
import communication.tcp.ServerListener;
import communication.udp.UDPCom;
import communication.udp.UDPComMulticast;

public class Main {

  private static Scanner scan;

  public static void main(String[] args) {
    ResourceBundle rb = ResourceBundle.getBundle("communication");

    scan = new Scanner(System.in);
    boolean isLopp = false;
    Server server = null;
    try {
      do {
        menuDisplay();

        String inputMode = scan.nextLine();
        if(!inputMode.isEmpty()) {
          System.out.println("-モード:" + inputMode);
          switch(inputMode) {
          case "0":
            UDPComInputStr(rb.getString("udp.send.addres"),
                             Integer.parseInt(rb.getString("udp.send.port")),
                             Integer.parseInt(rb.getString("udp.receive.port")),
                             1024);
            isLopp = true;
            break;

          case "1":
            UDPComMulticastInputStr(rb.getString("udp.multicast.addres"),
                                        Integer.parseInt(rb.getString("udp.multicast.send.port")),
                                        Integer.parseInt(rb.getString("udp.multicast.receive.port")),
                                        1024);
            isLopp = true;
            break;

          case "2c":
            server = new Server();
            server.coonect(rb.getString("tcp.server.addres"),
                           Integer.parseInt(rb.getString("tcp.server.port")));
            server.respond(new ServerListener(), 4, 256);
            isLopp = true;
            break;

          case "2s":
            if(server != null && server.isConnected()) {
              server.stop();
            }
            isLopp = true;
            break;

          case "e":
            System.out.println("終了いたします");
            isLopp = false;
            break;

          default:
            System.out.println("通信タイプ対象外");
            System.out.println("再度、操作したいモードを選んでください:");
            isLopp = true;
            break;
          }
        }
      }while(isLopp);
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      scan.close();
    }
  }

  private static void menuDisplay() {
    Map<String, String> operationMenuMap = new LinkedHashMap<>();
    operationMenuMap.put("0", "UDP通信ユニキャスト");
    operationMenuMap.put("1", "UDP通信マルチキャスト定時送信あり");
    operationMenuMap.put("e", "終了");

    System.out.println("操作したいモードを選んでください:");
    for (Map.Entry<String, String> entry : operationMenuMap.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }

  private static void UDPComInputStr(String IPAddres,
                                        int sendPort,
                                        int receivedPort,
                                        int receivedDataSize) {
    UDPCom.getInstance().coonect(receivedPort, receivedDataSize);
    int inputStrCount = 1;
    try {
      while (scan.hasNext()) {
        String inputStr = scan.nextLine();
        if(inputStr.equals("#q")) {
          break;
        }
        System.out.println(inputStrCount++ + "-UDP通信送信文字列:" + inputStr);
        UDPCom.getInstance().send(IPAddres, sendPort, inputStr.getBytes("UTF-8"));
        if(inputStr.matches("[+-]?\\d*(\\.\\d+)?")) {
          UDPCom.getInstance().send(IPAddres,
              sendPort,
              ByteBuffer.wrap(new byte[4]).putInt(Integer.parseInt(inputStr)).array());
        }
      }
      UDPCom.getInstance().discoonect();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void UDPComMulticastInputStr(String MulticastAddres,
                                                 int sendPort,
                                                 int receivePort,
                                                 int receivedDataSize) {
    try {
      String inputStr = scan.nextLine();
      System.out.println("-定周期UDP通信送信文字列:" + inputStr);
      byte[] sendData = inputStr.getBytes("UTF-8");
      if(inputStr.matches("[+-]?\\d*(\\.\\d+)?")) {
        sendData = ByteBuffer.wrap(new byte[4]).putInt(Integer.parseInt(inputStr)).array();
      }
      UDPComMulticast.getInstance().coonect(MulticastAddres, sendPort, receivePort, sendData, receivedDataSize);
      int inputStrCount = 1;
      while (scan.hasNext()) {
        inputStr = scan.nextLine();
        if(inputStr.equals("#q")) {
          break;
        }
        System.out.println(inputStrCount++ + "-UDP通信送信文字列:" + inputStr);
        UDPComMulticast.getInstance().send(inputStr.getBytes("UTF-8"), sendPort);
        if(inputStr.matches("[+-]?\\d*(\\.\\d+)?")) {
          UDPComMulticast.getInstance().send(
              ByteBuffer.wrap(new byte[4]).putInt(Integer.parseInt(inputStr)).array(),
              sendPort);
        }
      }
      UDPComMulticast.getInstance().discoonect();
    } catch(Exception e) {
      e.printStackTrace();
    }

  }
}
