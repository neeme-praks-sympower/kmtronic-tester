package net.sympower.kmtronic.tester;

import net.sympower.kmtronic.tester.port.DeviceStatus;
import net.sympower.kmtronic.tester.port.JSerialPortFactory;
import net.sympower.kmtronic.tester.port.opener.PortConfig;
import net.sympower.kmtronic.tester.port.PortLister;
import net.sympower.kmtronic.tester.port.opener.PortOpener;
import net.sympower.kmtronic.tester.port.opener.PropertyReader;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Scanner;

public class KMTronicTestRunnerMain {

  public static final PrintStream OUT = System.out; // NOSONAR

  public static void main(String[] args) {
    String propertiesFileName = args.length > 0 ? args[0] : "port.properties";
    String libraryVersion = args.length > 1 ? args[1] : "1.3";
    Scanner input = new Scanner(System.in);
    OUT.println("Plain runner! Type " +
                           "'list' to list serial ports, " +
                           "'status' to get KMTronic status or " +
                           "'exit' to get out.");
    OUT.print("> ");
    while (input.hasNextLine()) {
      String line = input.nextLine();
      if (line.equalsIgnoreCase("exit")) {
        break;
      }
      else if (line.equalsIgnoreCase("list")) {
        OUT.println("Available ports:");
        OUT.println(PortLister.listAvailablePorts());
      }
      else if (line.equalsIgnoreCase("status")) {
        try {
          Properties properties = new Properties();
          try (FileInputStream fis = new FileInputStream(propertiesFileName)) {
            properties.load(fis);
            PropertyReader propertyReader = new PropertyReader(properties);
            PortConfig portConfig = new PortConfig(propertyReader);
            PortOpener portOpener = makePortOpener(libraryVersion);
            KMTronicTester reader =
                new KMTronicTester(new JSerialPortFactory(portOpener), portConfig, propertyReader.getProperty("portName"));
            DeviceStatus deviceStatus = reader.testReadStatus();
            OUT.println("Got response: " + deviceStatus);
          }
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

  private static PortOpener makePortOpener(String libraryVersion) {
    String classNameSuffix;
    if (libraryVersion.startsWith("1")) {
      classNameSuffix = "Ver1_3";
    }
    else {
      classNameSuffix = "Ver2_9";
    }
    try {
      return (PortOpener) Class.forName(PortOpener.class.getName() + classNameSuffix).newInstance();
    }
    catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}
