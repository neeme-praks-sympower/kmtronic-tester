package net.sympower.kmtronic.tester.port.opener;

import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused") // Used via reflection
public class PortOpenerVer2_9 implements PortOpener { // NOSONAR

  private final Logger log = LoggerFactory.getLogger(getClass());

  public SerialPort configurePort(SerialPort port, PortConfig config) {
    port.setBaudRate(config.baudRate);
    port.setNumDataBits(config.numDataBits);
    if (config.numStopBits == 1) {
      port.setNumStopBits(SerialPort.ONE_STOP_BIT);
    }
    else if (config.numStopBits == 2) {
      port.setNumStopBits(SerialPort.TWO_STOP_BITS);
    }
    switch (config.parity) {
      case PortConfig.PARITY_EVEN:
        port.setParity(SerialPort.EVEN_PARITY);
        break;
      case PortConfig.PARITY_ODD:
        port.setParity(SerialPort.ODD_PARITY);
        break;
      case PortConfig.PARITY_NONE:
        port.setParity(SerialPort.NO_PARITY);
        break;
      default:
        throw new IllegalArgumentException("Unrecognized parity value: " + config.parity);
    }
    port.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
    Rs485ModeParameters rs485ModeParameters = config.rs485ModeParameters;
    if (rs485ModeParameters != null) {
            port.setRs485ModeParameters(
                rs485ModeParameters.isUseRS485Mode(),
                rs485ModeParameters.isRs485RtsActiveHigh(),
                rs485ModeParameters.isEnableTermination(),
                rs485ModeParameters.isRxDuringTx(),
                rs485ModeParameters.getDelayBeforeSendMicroseconds(),
                rs485ModeParameters.getDelayAfterSendMicroseconds()
            );
    }
    return port;
  }

  public SerialPort openPort(SerialPort port, PortConfig config) {
    if (!port.isOpen()) {
      log.debug("Opening serial port {} with settings {}", port.getSystemPortName(), config);
      boolean wasSuccess = port.openPort();
      if (!wasSuccess) {
        throw new IllegalStateException(String.format(
            "Could not open serial port %s with settings %s! Error code %s, last error location %s",
            port.getSystemPortName(),
            config,
            port.getLastErrorCode(),
            port.getLastErrorLocation()
        ));
      }
      else {
        log.info("Successfully opened serial port {} with settings {}", port.getSystemPortName(), config);
      }
    }
    return port;
  }

}
