package cometd.client.listener;

import java.io.PrintStream;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;

public class PrintStreamListener implements ClientSessionChannel.MessageListener {

  private PrintStream out = System.out;

  @Override
  public void onMessage(ClientSessionChannel channel, Message message) {
    this.out.println(message.getData());

    if (this.out.checkError()) {
      channel.getSession().disconnect();
    }
  }

  public void setOut(PrintStream out) {
    this.out = out;
  }
}
