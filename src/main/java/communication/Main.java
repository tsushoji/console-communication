package communication;

import java.util.Scanner;

import communication.udp.UDPCom;

public class Main {

	public static void main(String[] args) {
		UDPComInputStr("localhost", 10005, 1024);
	}

	private static void UDPComInputStr(String IPAddres, int port, int receivedDataSize) {
		UDPCom com = new UDPCom();
		com.coonect(port, receivedDataSize);
		int inputStrCount = 1;
		try(Scanner scan = new Scanner(System.in);){
			while(scan.hasNext()) {
				String inputStr = scan.nextLine();
				System.out.println(inputStrCount++ + "-コンソール送信文字列:" + inputStr);
				com.send(IPAddres, port, inputStr.getBytes("UTF-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
