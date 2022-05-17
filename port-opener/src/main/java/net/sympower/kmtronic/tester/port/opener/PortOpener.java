package net.sympower.kmtronic.tester.port.opener;

import com.fazecast.jSerialComm.SerialPort;

public interface PortOpener {

  SerialPort configurePort(SerialPort port, PortConfig config);

  SerialPort openPort(SerialPort port, PortConfig config);

}
