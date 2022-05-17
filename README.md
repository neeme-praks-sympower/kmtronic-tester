# KMTronic tester

A little utility to test RS-485 serial communication with KMTronic 8-port relay to troubleshoot issues with the latest jSerialComm library.

It builds two versions of the utility, one with jSerialComm 1.3 and other with jSerialComm 2.9. The older version works on the specific hardware but the newer version does not work.

## Building

```shell
./gradlew clean app:ver_1_3DistTar app:ver_2_9DistTar
```
This will produce two TAR files in `app/build/distributions/` folder, one for each version of the utility.

## Usage

### Command line arguments

Arguments:
1. Properties file for loading configuration (sample file included). By default `port.properties` in the current folder.

### Running

Run the utility and you will enter a simple command line shell with a few commands. 