package net.sympower.kmtronic.tester.port;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class KMTronic implements AutoCloseable {

  private final Logger log = LoggerFactory.getLogger(getClass());

  public static final int DEVICE_NUMBER_BASE = 0xA0;
  public static final byte START_MARKER_BYTE = (byte) 0xFF;
  public static final int STATUS_MESSAGE_HEADER_SIZE = 2;
  public static final int NUMBER_OF_RELAYS_IN_MESSAGE = 8;

  private final OutputStream os;
  private final InputStream is;

  public KMTronic(JSerialPort port) {
    this.os = port.openOutputStream();
    this.is = port.openInputStream();
  }

  public DeviceStatus getRelayStatus(int deviceId) throws IOException {
    os.write(new byte[] { START_MARKER_BYTE, (byte) (DEVICE_NUMBER_BASE + deviceId), (byte) 0x00 });
    os.flush();
    final int expectedResponseSize = STATUS_MESSAGE_HEADER_SIZE + NUMBER_OF_RELAYS_IN_MESSAGE;
    ByteBuffer bb = ByteBuffer.allocate(expectedResponseSize);
    int bytesRead = 0;
    while (bytesRead < expectedResponseSize) {
      int b = is.read();
      if (b > -1) {
        bb.put((byte) b);
        bytesRead++;
      }
    }
    byte[] response = bb.array();
    if (response.length == expectedResponseSize) {
      return handleBytes(response, 0, expectedResponseSize);
    }
    else {
      throw new IllegalStateException(String.format(
          "Got %s bytes while expected %s bytes",
          response.length,
          expectedResponseSize
      ));
    }
  }

  public DeviceStatus handleBytes(byte[] buffer, int startIdx, int numRead) {
    DeviceStatus status = parseStatus(buffer, startIdx, numRead);
    if (status != null) {
      log.debug("Got device status: {}", status);
    }
    return status;
  }

  DeviceStatus parseStatus(byte[] buffer, int startIdx, int numRead) {
    int minNumberOfBytes = STATUS_MESSAGE_HEADER_SIZE + 1;
    for (int start = startIdx; start <= numRead - minNumberOfBytes; start++) {
      int deviceNumberCandidate = makeInt(buffer[start + 1]);
      if (buffer[start] == START_MARKER_BYTE && deviceNumberCandidate > DEVICE_NUMBER_BASE) {
        int deviceNumber = deviceNumberCandidate - DEVICE_NUMBER_BASE;
        boolean[] isRelayOn = new boolean[NUMBER_OF_RELAYS_IN_MESSAGE];
        int firstIndex = start + minNumberOfBytes - 1;
        for (int statusIdx = firstIndex; statusIdx < numRead && statusIdx < firstIndex + NUMBER_OF_RELAYS_IN_MESSAGE; statusIdx++) {
          isRelayOn[statusIdx-firstIndex] = (buffer[statusIdx] == 0x00);
        }
        return new DeviceStatus(deviceNumber, isRelayOn);
      }
    }
    return null;
  }

  // workaround for signed bytes
  private static int makeInt(byte b) {
    return b < 0 ? b + 256 : b;
  }

  public void close() {
    try {
      if (this.os != null) {
        this.os.close();
      }
    }
    catch (IOException e) {
      log.debug("Error while closing output stream", e);
    }
    try {
      if (this.is != null) {
        this.is.close();
      }
    }
    catch (IOException e) {
      log.debug("Error while closing input stream", e);
    }
  }

}
