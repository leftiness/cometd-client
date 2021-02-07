package cometd.client;

import picocli.CommandLine;

import cometd.client.command.MainCommand;

public class App {
  public static void main(String[] args) {
    System.exit(new CommandLine(new MainCommand()).execute(args));
  }
}
