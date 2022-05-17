package net.sympower.kmtronic.tester.port.opener;

import static net.sympower.kmtronic.tester.port.opener.PropertyReader.FALSE;

public class PortConfig {

  public static final String PARITY_NONE = "none";
  public static final String PARITY_ODD = "odd";
  public static final String PARITY_EVEN = "even";

  public final int baudRate;
  public final int numDataBits;
  public final String parity;
  public final int numStopBits;

  public final Rs485ModeParameters rs485ModeParameters;

  public PortConfig(int baudRate, int numDataBits, String parity, int numStopBits) {
    this(baudRate, numDataBits, parity, numStopBits, null);
  }

  public PortConfig(PropertyReader props) {
    this(props.getIntegerProperty("baudRate", "9600"),
         props.getIntegerProperty("numDataBits", "8"),
         props.getProperty("parity", "none"),
         props.getIntegerProperty("numStopBits", "1"),
         Boolean.TRUE.equals(props.getBooleanProperty("useRs485ModeParameters", FALSE)) ?
           new Rs485ModeParameters(props) :
           null
    );
  }

  public PortConfig(
      int baudRate,
      int numDataBits,
      String parity,
      int numStopBits,
      Rs485ModeParameters rs485ModeParameters
  ) {
    this.baudRate = baudRate;
    this.numDataBits = numDataBits;
    this.parity = parity;
    this.numStopBits = numStopBits;
    this.rs485ModeParameters = rs485ModeParameters;
  }

  public String toString() {
    return "PortConfig{" +
      "baudRate=" + baudRate + ", " +
      "numDataBits=" + numDataBits + ", " +
      "parity=" + parity + ", " +
      "numStopBits=" + numStopBits + ", " +
      "rs485ModeParameters=" + rs485ModeParameters + '}';
  }

  public boolean equalsExceptBaudRate(PortConfig that) {
    if (numDataBits != that.numDataBits)
      return false;
    if (numStopBits != that.numStopBits)
      return false;
    return parity != null ? parity.equals(that.parity) : that.parity == null;
  }

  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    PortConfig that = (PortConfig) o;
    return equalsExceptBaudRate(that) && baudRate == that.baudRate;
  }

  public int hashCode() {
    int result = baudRate;
    result = 31 * result + numDataBits;
    result = 31 * result + (parity != null ? parity.hashCode() : 0);
    result = 31 * result + numStopBits;
    return result;
  }
}
