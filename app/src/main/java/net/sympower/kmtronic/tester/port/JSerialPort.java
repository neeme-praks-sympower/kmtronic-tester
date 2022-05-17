package net.sympower.kmtronic.tester.port;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

public class JSerialPort implements AutoCloseable {

  private final SerialPort port;

  public JSerialPort(SerialPort port) {
    this.port = port;
  }

  public InputStream openInputStream() {
    return port.getInputStream();
  }

  public OutputStream openOutputStream() {
    return port.getOutputStream();
  }

  public void setBaudRate(int baudRate) {
    port.setBaudRate(baudRate);
  }

  public void close() {
    if (port != null) {
      port.closePort();
    }
  }

}
