package net.sympower.kmtronic.tester;

import net.sympower.kmtronic.tester.port.DeviceStatus;
import net.sympower.kmtronic.tester.port.JSerialPortFactory;
import net.sympower.kmtronic.tester.port.PortLister;
import net.sympower.kmtronic.tester.port.opener.PortConfig;
import net.sympower.kmtronic.tester.port.opener.PortOpener;
import net.sympower.kmtronic.tester.port.opener.PropertyReader;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KMTronicTestRunnerMain {

  public static final PrintStream OUT = System.out; // NOSONAR
  private static final String[] PORT_OPENER_CLASS_SUFFIXES_TO_TRY = { "Ver1_3", "Ver2_9" };
  public static final Pattern TEXT_COMMAND_PATTERN = Pattern.compile("text\\s+(\\S+)\\s+(\\d+)\\s+(.+)");

  public static void main(String[] args) throws Exception {
    String propertiesFileName = args.length > 0 ? args[0] : "port.properties";
    Scanner input = new Scanner(System.in);
    OUT.println("Plain runner! Type: \n" +
                           " * 'list' to list serial ports,\n" +
                           " * 'status' to get KMTronic status,\n" +
                           " * 'text <port name> <response timeout in millis> <text>' to send text to a port\n" +
                           " * 'exit' to get out\n");
    OUT.print("> ");
    PortOpener portOpener = makePortOpener();
    Properties properties = new Properties();
    try (FileInputStream fis = new FileInputStream(propertiesFileName)) {
      properties.load(fis);
    }
    PropertyReader propertyReader = new PropertyReader(properties);
    PortConfig portConfig = new PortConfig(propertyReader);
    while (input.hasNextLine()) {
      String line = input.nextLine();
      Matcher textCommandMatcher = TEXT_COMMAND_PATTERN.matcher(line);
      if (line.equalsIgnoreCase("exit")) {
        break;
      }
      else if (line.equalsIgnoreCase("list")) {
        OUT.println("Available ports:");
        OUT.println(PortLister.listAvailablePorts());
      }
      else if (line.equalsIgnoreCase("status")) {
        try {
          KMTronicTester reader =
              new KMTronicTester(new JSerialPortFactory(portOpener), portConfig, propertyReader.getProperty("portName"));
          DeviceStatus deviceStatus = reader.testReadStatus();
          OUT.println("Got response: " + deviceStatus);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      else if (textCommandMatcher.matches()) {
        String portName = textCommandMatcher.group(1);
        String timeout = textCommandMatcher.group(2);
        String text = textCommandMatcher.group(3);
        try {
          TextSender sender = new TextSender(new JSerialPortFactory(portOpener), portConfig, portName);
          String response = sender.sendText(text + "\r\n", Long.parseLong(timeout));
          OUT.println("Got response: " + response);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      else if (!line.isEmpty()) {
        OUT.println("Unknown command: " + line);
      }
      OUT.print("> ");
    }
  }

  private static PortOpener makePortOpener() {
    for (String classNameSuffix : PORT_OPENER_CLASS_SUFFIXES_TO_TRY) {
      try {
        return (PortOpener) Class.forName(PortOpener.class.getName() + classNameSuffix).newInstance();
      }
      catch (ClassNotFoundException e) {
        // Ignore, this is expected
      }
      catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
    throw new IllegalStateException("No port opener implementation found!");
  }

}
