package net.sympower.kmtronic.tester.port.opener;

import static net.sympower.kmtronic.tester.port.opener.PropertyReader.FALSE;
import static net.sympower.kmtronic.tester.port.opener.PropertyReader.TRUE;

public class Rs485ModeParameters {

  private final boolean useRS485Mode;
  private final boolean rs485RtsActiveHigh;
  private final boolean enableTermination;
  private final boolean rxDuringTx;
  private final int delayBeforeSendMicroseconds;
  private final int delayAfterSendMicroseconds;

  public Rs485ModeParameters() {
    this(true, true, false, false, 1000, 1000);
  }

  public Rs485ModeParameters(PropertyReader props) {
    this(props.getBooleanProperty("useRS485Mode", TRUE),
         props.getBooleanProperty("rs485RtsActiveHigh", TRUE),
         props.getBooleanProperty("enableTermination", FALSE),
         props.getBooleanProperty("rxDuringTx", FALSE),
         props.getIntegerProperty("delayBeforeSendMicroseconds", "1000"),
         props.getIntegerProperty("delayAfterSendMicroseconds", "1000"));
  }

  public Rs485ModeParameters(
      boolean useRS485Mode,
      boolean rs485RtsActiveHigh,
      boolean enableTermination,
      boolean rxDuringTx,
      int delayBeforeSendMicroseconds,
      int delayAfterSendMicroseconds
  ) {
    this.useRS485Mode = useRS485Mode;
    this.rs485RtsActiveHigh = rs485RtsActiveHigh;
    this.enableTermination = enableTermination;
    this.rxDuringTx = rxDuringTx;
    this.delayBeforeSendMicroseconds = delayBeforeSendMicroseconds;
    this.delayAfterSendMicroseconds = delayAfterSendMicroseconds;
  }

  public boolean isUseRS485Mode() {
    return useRS485Mode;
  }

  public boolean isRs485RtsActiveHigh() {
    return rs485RtsActiveHigh;
  }

  public boolean isEnableTermination() {
    return enableTermination;
  }

  public boolean isRxDuringTx() {
    return rxDuringTx;
  }

  public int getDelayBeforeSendMicroseconds() {
    return delayBeforeSendMicroseconds;
  }

  public int getDelayAfterSendMicroseconds() {
    return delayAfterSendMicroseconds;
  }

  public String toString() {
    return "useRS485Mode=" + useRS485Mode +
        ", rs485RtsActiveHigh=" + rs485RtsActiveHigh +
        ", enableTermination=" + enableTermination +
        ", rxDuringTx=" + rxDuringTx +
        ", delayBeforeSendMicroseconds=" + delayBeforeSendMicroseconds +
        ", delayAfterSendMicroseconds=" + delayAfterSendMicroseconds;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Rs485ModeParameters that = (Rs485ModeParameters) o;

    if (useRS485Mode != that.useRS485Mode) {
      return false;
    }
    if (rs485RtsActiveHigh != that.rs485RtsActiveHigh) {
      return false;
    }
    if (enableTermination != that.enableTermination) {
      return false;
    }
    if (rxDuringTx != that.rxDuringTx) {
      return false;
    }
    if (delayBeforeSendMicroseconds != that.delayBeforeSendMicroseconds) {
      return false;
    }
    return delayAfterSendMicroseconds == that.delayAfterSendMicroseconds;
  }

  public int hashCode() {
    int result = (useRS485Mode ? 1 : 0);
    result = 31 * result + (rs485RtsActiveHigh ? 1 : 0);
    result = 31 * result + (enableTermination ? 1 : 0);
    result = 31 * result + (rxDuringTx ? 1 : 0);
    result = 31 * result + delayBeforeSendMicroseconds;
    result = 31 * result + delayAfterSendMicroseconds;
    return result;
  }

}
