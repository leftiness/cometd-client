package cometd.client.command;

import java.util.concurrent.Callable;

import org.cometd.bayeux.Channel;
import org.cometd.client.BayeuxClient.State;
import org.cometd.client.BayeuxClient;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import cometd.client.listener.HandshakeSubscribeListener;
import cometd.client.listener.OutputStreamListener;
import cometd.client.service.BayeuxClientFactory;

@Command(
  name = "subscribe",
  aliases = { "s" },
  mixinStandardHelpOptions = true
)
public class SubscribeCommand implements Callable<Integer> {

  private String server;
  private String channel;
  private long timeout;

  private BayeuxClientFactory clientFactory = new BayeuxClientFactory();

  @Override
  public Integer call() {
    OutputStreamListener out = new OutputStreamListener();

    HandshakeSubscribeListener subscribe = new HandshakeSubscribeListener();
    subscribe.setChannel(this.channel);
    subscribe.setListener(out);

    BayeuxClient client = this.clientFactory.create(this.server);
    client.getChannel(Channel.META_HANDSHAKE).addListener(subscribe);
    client.handshake();

    if (!client.waitFor(this.timeout, State.CONNECTED)) {
      return ExitCode.ERROR_SUBSCRIBE_TIMEOUT;
    }

    if (!client.waitFor(Long.MAX_VALUE, State.DISCONNECTED)) {
      return ExitCode.ERROR_SUBSCRIBE_DISCONNECTED;
    }

    return ExitCode.OK;
  }

  @Parameters(
    description = "Choose the CometD server",
    index = "0",
    arity = "1"
  )
  public void setServer(String server) {
    this.server = server;
  }

  @Parameters(
    description = "Choose the CometD channel",
    index = "1",
    arity = "1"
  )
  public void setChannel(String channel) {
    this.channel = channel;
  }

  @Option(
    names = { "-t", "--timeout" },
    description = "Choose how many milliseconds to wait for the connection (Default: ${DEFAULT-VALUE})",
    arity = "1",
    defaultValue = "60000"
  )
  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  public void setBayeuxClientFactory(BayeuxClientFactory clientFactory) {
    this.clientFactory = clientFactory;
  }
}
