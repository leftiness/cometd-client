package cometd.client.command;

import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "subscribe", aliases = { "s" })
public class SubscribeCommand implements Callable<Integer> {

  private String channel;

  @Override
  public Integer call() {
    System.out.println(this.channel);
    return 0;
  }

  @Parameters(index = "0", arity = "1")
  public void setChannel(String channel) {
    this.channel = channel;
  }
}
