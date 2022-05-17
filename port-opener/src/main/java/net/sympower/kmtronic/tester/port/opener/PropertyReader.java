package net.sympower.kmtronic.tester.port.opener;

import java.util.Properties;

public class PropertyReader {

  public static final String TRUE = "true";
  public static final String FALSE = "false";

  private final Properties properties;

  public PropertyReader(Properties properties) {
    this.properties = properties;
  }

  public String getProperty(String name) {
    return getProperty(name, null);
  }

  public String getProperty(String name, String defaultValue) {
    String value = properties.getProperty(name, System.getProperty(name, defaultValue));
    if (value != null) {
      value = value.trim();
      return value.length() > 0 ? value : null;
    }
    return null;
  }

  public Boolean getBooleanProperty(String name, String defaultValue) {
    return Boolean.parseBoolean(getProperty(name, defaultValue));
  }

  public Integer getIntegerProperty(String name, String defaultValue) {
    return Integer.parseInt(getProperty(name, defaultValue));
  }

}
