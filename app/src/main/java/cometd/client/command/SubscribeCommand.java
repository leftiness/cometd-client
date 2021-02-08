package cometd.client.command;

import kotlin.DeprecationLevel;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Callable;

import okhttp3.OkHttpClient;
import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.http.okhttp.OkHttpClientTransport;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.websocket.okhttp.OkHttpWebSocketTransport;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "subscribe", aliases = { "s" })
public class SubscribeCommand implements Callable<Integer> {

  private String server;
  private String channel;

  @Override
  public Integer call() {

    OkHttpClient ok = new OkHttpClient();
    ClientTransport ws = new OkHttpWebSocketTransport(null, ok);
    ClientTransport http = new OkHttpClientTransport(null, ok);
    BayeuxClient bayeux = new BayeuxClient(this.server, ws, http);
    bayeux.handshake();
    // bayeux.getChannel(Channel.META_HANDSHAKE).addListener(new InitializerListener(bayeux));
    // bayeux.getChannel(Channel.META_CONNECT).addListener(new ConnectionListener(bayeux));
    bayeux.handshake();
    boolean success = bayeux.waitFor(1000, BayeuxClient.State.CONNECTED);
    if (!success) {
        System.err.printf("Could not handshake with server at %s%n", this.server);
        return 1;
    }
    Map<String, Object> data = new HashMap<>();
    data.put("user", "brandon");
    data.put("chat", "test");
    bayeux.getChannel("/chat/demo").publish(data);

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
}
