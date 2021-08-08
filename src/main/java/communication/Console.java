package communication;

public class Console {

  private static Console console = new Console();

  private Console() {}

  public static Console getInstance() {
    return console;
  }

  public void println(byte[] data) {
    for(byte partData : data) {
      int toInt = Byte.toUnsignedInt(partData);
      String toStr = Integer.toBinaryString(toInt);
      toStr = String.format("%8s", toStr).replace(' ', '0');
      System.out.println(toStr);
    }
  }
}
