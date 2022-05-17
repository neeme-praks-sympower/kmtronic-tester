package net.sympower.kmtronic.tester.port;

public class DeviceStatus {
  public final int deviceId;
  public final boolean[] isRelayOn;

  public DeviceStatus(int deviceId, boolean[] isRelayOn) {
    this.deviceId = deviceId;
    this.isRelayOn = isRelayOn;
  }

  public String toString() {
    return "DeviceStatus{" +
        "deviceId=" + deviceId + ", " +
        "status=" + makeRelayStatusString(isRelayOn) + '}';
  }

  private String makeRelayStatusString(boolean[] isRelayOn) {
    StringBuilder sb = new StringBuilder(8);
    for (boolean b : isRelayOn) {
      sb.append(b ? '1' : '0');
    }
    return sb.toString();
  }

}
