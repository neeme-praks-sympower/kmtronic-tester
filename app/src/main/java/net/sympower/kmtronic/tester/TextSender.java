package net.sympower.kmtronic.tester;

import net.sympower.kmtronic.tester.port.JSerialPort;
import net.sympower.kmtronic.tester.port.JSerialPortFactory;
import net.sympower.kmtronic.tester.port.opener.PortConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TextSender {

  private final JSerialPortFactory portFactory;
  private final PortConfig portConfig;
  private final String portName;

  public TextSender(JSerialPortFactory portFactory, PortConfig portConfig, String portName) {
    this.portFactory = portFactory;
    this.portConfig = portConfig;
    this.portName = portName;
  }

  String sendText(String text, long timeoutMs) throws IOException {
    try (
        JSerialPort port = portFactory.open(portName, portConfig);
        OutputStream os = port.openOutputStream();
        InputStream is = port.openInputStream()
    ) {
      os.write(text.getBytes(StandardCharsets.UTF_8));
      byte[] responseBytes = nonBlockingBusyWaitRead(is, timeoutMs);
      return new String(responseBytes, StandardCharsets.UTF_8);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return e.toString();
    }
  }

  public byte[] nonBlockingBusyWaitRead(InputStream is, long timeoutMs) throws IOException, InterruptedException {
    final long start = System.currentTimeMillis();
    ByteBuffer bb = ByteBuffer.allocate(1024);
    while (System.currentTimeMillis() < start + timeoutMs) {
      if (is.available() > 0) {
        int b = is.read();
        if (b > -1) {
          bb.put((byte) b);
        }
        else {
          break;
        }
      }
      else {
        //noinspection BusyWait
        Thread.sleep(1);
      }
    }
    return bb.array();
  }

}
