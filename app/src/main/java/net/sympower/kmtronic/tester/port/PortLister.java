package net.sympower.kmtronic.tester.port;

import com.fazecast.jSerialComm.SerialPort;

public class PortLister {

  private PortLister() {
  }

  public static String listAvailablePorts() {
    return portsToHumanReadableListString(SerialPort.getCommPorts());
  }

  static String portsToHumanReadableListString(SerialPort[] ports) {
    StringBuilder portsList = new StringBuilder(127);
    for (SerialPort port : ports) {
      portsList.append("  ").append(port.getSystemPortName()).append(System.lineSeparator());
    }
    return portsList.toString();
  }

}
