package net.sympower.kmtronic.tester.port;

import com.fazecast.jSerialComm.SerialPort;
import net.sympower.kmtronic.tester.port.opener.PortConfig;
import net.sympower.kmtronic.tester.port.opener.PortOpener;

public class JSerialPortFactory {

  private final PortOpener portOpener;

  public JSerialPortFactory(PortOpener portOpener) {
    this.portOpener = portOpener;
  }

  public JSerialPort open(String portName, PortConfig portConfig) {
    return new JSerialPort(portOpener.openPort(portOpener.configurePort(findPort(portName), portConfig), portConfig));
  }

  private static SerialPort findPort(String portName) {
    return findPort(portName, SerialPort.getCommPorts());
  }

  static SerialPort findPort(String portName, SerialPort[] ports) {
    StringBuilder availablePorts = new StringBuilder(127);
    for (int i = 0; i < ports.length; i++) {
      if (ports[i].getSystemPortName().equals(portName)) {
        return ports[i];
      }
      availablePorts.append(ports[i].getSystemPortName());
      if (i < ports.length-1) {
        availablePorts.append(", ");
      }
    }
    throw new IllegalArgumentException(String.format(
        "Invalid port name specified: %s, available: %s",
        portName,
        availablePorts
    ));
  }

}
