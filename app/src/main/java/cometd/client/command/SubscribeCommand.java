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

import cometd.client.listener.HeartbeatListener;
import cometd.client.listener.PrintStreamListener;
import cometd.client.listener.SubscribeListener;
import cometd.client.service.BayeuxClientFactory;

@Command(name = "subscribe", aliases = { "s" })
public class SubscribeCommand implements Callable<Integer> {

  private String server;
  private String[] channels;
  private long timeout;

  private BayeuxClientFactory clientFactory = new BayeuxClientFactory();

  @Override
  public Integer call() {
    PrintStreamListener print = new PrintStreamListener();
    print.setOut(System.out);

    SubscribeListener subscribe = new SubscribeListener();
    subscribe.setChannels(this.channels);
    subscribe.setListener(print);

    HeartbeatListener heartbeat = new HeartbeatListener();
    ClientSessionChannel.MessageListener onConnect = heartbeat::onConnect;
    ClientSessionChannel.MessageListener onDisconnect = heartbeat::onDisconnect;

    BayeuxClient client = this.clientFactory.create(this.server);
    client.getChannel(Channel.META_HANDSHAKE).addListener(subscribe);
    client.getChannel(Channel.META_CONNECT).addListener(onConnect);
    client.getChannel(Channel.META_DISCONNECT).addListener(onDisconnect);
    client.handshake();

    if (!client.waitFor(this.timeout, BayeuxClient.State.CONNECTED)) {
      return -1;
    }

    if (!client.waitFor(Long.MAX_VALUE, BayeuxClient.State.DISCONNECTED)) {
      return -2;
    }

    return 0;
  }

  @Parameters(index = "0", arity = "1")
  public void setServer(String server) {
    this.server = server;
  }

  @Parameters(index = "1", arity = "1..*")
  public void setChannels(String[] channels) {
    this.channels = channels;
  }

  @Option(names = { "-t", "--timeout" }, arity = "1", defaultValue = Long.MAX_VALUE + "")
  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  public void setBayeuxClientFactory(BayeuxClientFactory clientFactory) {
    this.clientFactory = clientFactory;
  }
}
