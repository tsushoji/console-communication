package communication;

import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.Scanner;

import communication.udp.UDPCom;

public class Main {

  public static void main(String[] args) {
    ResourceBundle rb = ResourceBundle.getBundle("communication");
    UDPComInputStr(rb.getString("udp.send.addres"),
                     Integer.parseInt(rb.getString("udp.send.port")),
                     Integer.parseInt(rb.getString("udp.receive.port")),
                     1024);
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
}
