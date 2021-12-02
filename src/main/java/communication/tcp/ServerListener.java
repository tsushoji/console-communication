package communication.tcp;

public class ServerListener implements OnDoneServerListener {

  @Override
  public void onDone(int code, byte[] body) {
    StringBuilder result = new StringBuilder();
    for(byte data : body) {
      result.append(String.valueOf(data));
    }

    switch(code) {
      case 0:
        result.append("0");
        break;
      case 1:
        result.append("1");
        break;
      default:
        break;
    }
    body = result.toString().getBytes();
  }

  @Override
  public void onFailed() {
    System.out.println("クライアントへのレスポンス失敗");
  }

}
