package net.sympower.kmtronic.tester;

import net.sympower.kmtronic.tester.port.DeviceStatus;
import net.sympower.kmtronic.tester.port.JSerialPortFactory;
import net.sympower.kmtronic.tester.port.KMTronic;
import net.sympower.kmtronic.tester.port.opener.PortConfig;

import java.io.IOException;

public class KMTronicTester {

  final JSerialPortFactory portFactory;
  final PortConfig portConfig;
  final String portName;

  public KMTronicTester(JSerialPortFactory portFactory, PortConfig portConfig, String portName) {
    this.portFactory = portFactory;
    this.portConfig = portConfig;
    this.portName = portName;
  }

  DeviceStatus testReadStatus() throws IOException {
    try (KMTronic kmtronic = new KMTronic(portFactory.open(portName, portConfig))) {
      return kmtronic.getRelayStatus(1);
    }
  }

}
