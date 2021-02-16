package cometd.client.listener;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;

public class HandshakeSubscribeListener implements ClientSessionChannel.MessageListener {

  private String channel;
  private ClientSessionChannel.MessageListener listener;

  @Override
  public void onMessage(ClientSessionChannel channel, Message message) {
    channel.getSession().getChannel(this.channel).subscribe(this.listener);
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public void setListener(ClientSessionChannel.MessageListener listener) {
    this.listener = listener;
  }
}
