package communication.tcp;

public interface OnDoneServerListener {

  byte[] onDone(byte[] code, int bodyLength);
  void onFailed();
}
