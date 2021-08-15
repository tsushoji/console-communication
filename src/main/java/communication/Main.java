package communication;

import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.Scanner;

import communication.udp.UDPCom;
import communication.udp.UDPComMulticast;

public class Main {

  public static void main(String[] args) {
    ResourceBundle rb = ResourceBundle.getBundle("communication");

    String comType = "1";
    switch(comType) {
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
      default:
        System.out.println("通信タイプ対象外");
        break;
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
        System.out.println(inputStrCount++ + "-コンソール送信文字列:" + inputStr);
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
      System.out.println("-定周期で送信する文字列:" + inputStr);
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
