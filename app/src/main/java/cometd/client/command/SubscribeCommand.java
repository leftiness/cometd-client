package cometd.client.command;

import java.io.PrintStream;
import java.util.concurrent.Callable;

import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import cometd.client.service.BayeuxClientFactory;

@Command(name = "subscribe", aliases = { "s" })
public class SubscribeCommand implements Callable<Integer> {

  private String server;
  private String channel;
  private long timeout;

  private BayeuxClientFactory clientFactory = new BayeuxClientFactory();
  private PrintStream out = System.out;

  @Override
  public Integer call() {
    BayeuxClient client = this.clientFactory.create(this.server);

    client
      .getChannel(Channel.META_HANDSHAKE)
      .addListener((ClientSessionChannel.MessageListener) this::onHandshake);
    client.handshake();

    if (!client.waitFor(this.timeout, BayeuxClient.State.CONNECTED)) {
      return -1;
    }

    client.waitFor(600000, BayeuxClient.State.DISCONNECTED);
    return 0;
  }

  @Parameters(index = "0", arity = "1")
  public void setServer(String server) {
    this.server = server;
  }

  @Parameters(index = "1", arity = "1")
  public void setChannel(String channel) {
    this.channel = channel;
  }

  @Option(names = { "-t", "--timeout" }, arity = "1", defaultValue = "1000")
  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  public void setBayeuxClientFactory(BayeuxClientFactory clientFactory) {
    this.clientFactory = clientFactory;
  }

  public void setOut(PrintStream out) {
    this.out = out;
  }

  private void onHandshake(ClientSessionChannel channel, Message message) {
    channel.getSession().getChannel(this.channel).subscribe(this::onMessage);
  }

  private void onMessage(ClientSessionChannel channel, Message message) {
    this.out.println(message.getData());
  }
}
