package communication.tcp;

import java.nio.ByteBuffer;
import java.util.Arrays;

// TCP-S-➁
public class ServerListener implements OnDoneServerListener {

  // TCP-S-➂
  @Override
  public byte[] onDone(byte[] code, int bodyLength) {

    StringBuilder result = new StringBuilder();

    switch(ByteBuffer.wrap(code).getInt()) {
      case 0:
        result.append("0");
        break;
      // ASKII:1
      case 825307441:
        result.append("1");
        break;
      default:
        break;
    }
    byte[] temp = result.toString().getBytes();
    int codeLength = code.length;
    byte[] body = Arrays.copyOf(code, codeLength + temp.length);
    for(int i = 0; i < temp.length; i++ ) {
      body[codeLength + i] = temp[i];
    }
    return body;
  }

  // TCP-S-④
  @Override
  public void onFailed() {
    System.out.println("クライアントへのレスポンス失敗");
  }

}
