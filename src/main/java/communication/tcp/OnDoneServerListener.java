package communication.tcp;

public interface OnDoneServerListener {

  void onDone(int code, byte[] body);
  void onFailed();
}
