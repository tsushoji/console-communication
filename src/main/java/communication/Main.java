package communication;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import communication.udp.UDPCom;
import communication.udp.UDPComMulticast;

public class Main {

  public static void main(String[] args) {
    ResourceBundle rb = ResourceBundle.getBundle("communication");

    menuDisplay();

    try (Scanner scan = new Scanner(System.in);) {
      while (scan.hasNext()) {
        String inputMode = scan.nextLine();
        System.out.println("-モード:" + inputMode);
        switch(inputMode) {
        case "0":
          UDPComInputStr(rb.getString("udp.send.addres"),
                           Integer.parseInt(rb.getString("udp.send.port")),
                           Integer.parseInt(rb.getString("udp.receive.port")),
                           1024);
          break;

        case "1":
          UDPComMulticastInputStr(rb.getString("udp.multicast.addres"),
                                      Integer.parseInt(rb.getString("udp.multicast.send.port")),
                                      Integer.parseInt(rb.getString("udp.multicast.receive.port")),
                                      1024);
          break;

        case "e":
          System.out.println("終了いたします");
          throw new Exception();

        default:
          System.out.println("通信タイプ対象外");
          System.out.println("再度、操作したいモードを選んでください:");
          break;
        }
      }
    } catch (Exception e) {}
  }

  private static void menuDisplay() {
    Map<String, String> operationMenuMap = Map.of(
        "0", "UDP通信ユニキャスト",
        "1", "UDP通信マルチキャスト定時送信あり",
        "e", "終了");
    System.out.println("操作したいモードを選んでください:");
    for (Map.Entry<String, String> entry : operationMenuMap.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }

  private static void UDPComInputStr(String IPAddres,
                                        int sendPort,
                                        int receivedPort,
                                        int receivedDataSize) {
    UDPCom com = new UDPCom();
    com.coonect(receivedPort, receivedDataSize);
    int inputStrCount = 1;
    try (Scanner scan = new Scanner(System.in);) {
      while (scan.hasNext()) {
        String inputStr = scan.nextLine();
        System.out.println(inputStrCount++ + "-UDP通信送信文字列:" + inputStr);
        com.send(IPAddres, sendPort, inputStr.getBytes("UTF-8"));
        if(inputStr.matches("[+-]?\\d*(\\.\\d+)?")) {
          com.send(IPAddres,
              sendPort,
              ByteBuffer.wrap(new byte[4]).putInt(Integer.parseInt(inputStr)).array());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void UDPComMulticastInputStr(String MulticastAddres,
                                                 int sendPort,
                                                 int receivePort,
                                                 int receivedDataSize) {
    try (Scanner scan = new Scanner(System.in);){
      String inputStr = scan.nextLine();
      System.out.println("-定周期UDP通信送信文字列:" + inputStr);
      UDPComMulticast com = new UDPComMulticast();
      byte[] sendData = inputStr.getBytes("UTF-8");
      if(inputStr.matches("[+-]?\\d*(\\.\\d+)?")) {
        sendData = ByteBuffer.wrap(new byte[4]).putInt(Integer.parseInt(inputStr)).array();
      }
      com.coonect(MulticastAddres, sendPort, receivePort, sendData, receivedDataSize);
    } catch(Exception e) {
      e.printStackTrace();
    }

  }
}
