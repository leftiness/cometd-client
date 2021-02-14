package cometd.client.listener;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;

public class SubscribeListener implements ClientSessionChannel.MessageListener {

  private String[] channels;
  private ClientSessionChannel.MessageListener listener;

  @Override
  public void onMessage(ClientSessionChannel channel, Message message) {
    ClientSession session = channel.getSession();
    for (String each : this.channels) {
      session.getChannel(each).subscribe(this.listener);
    }
  }

  public void setChannels(String[] channels) {
    this.channels = channels;
  }

  public void setListener(ClientSessionChannel.MessageListener listener) {
    this.listener = listener;
  }
}
