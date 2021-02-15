package cometd.client.listener;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.Message;

public class HeartbeatListener {

  private boolean connected = false;

  public void onConnect(ClientSessionChannel channel, Message message) {
    if (!channel.getSession().isConnected()) {
      System.out.println("DEAD");
      return;
    }

    boolean wasConnected = this.connected;
    this.connected = message.isSuccessful();

    if (!wasConnected && this.connected) {
      System.out.println("ESTABLISHED");
      return;
    }

    if (wasConnected && !this.connected) {
      System.out.println("LOST");
      return;
    }

    System.out.println("ALIVE");
  }

  public void onDisconnect(ClientSessionChannel channel, Message message) {
    if (message.isSuccessful()) {
      this.connected = false;
    }
  }
}
