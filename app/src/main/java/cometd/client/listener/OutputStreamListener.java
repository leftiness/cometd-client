package cometd.client.listener;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;

public class OutputStreamListener implements ClientSessionChannel.MessageListener {
  private ObjectMapper mapper = new ObjectMapper()
      .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
  private OutputStream out = System.out;
  private String separator = System.lineSeparator();

  @Override
  public void onMessage(ClientSessionChannel channel, Message message) {
    try {
      this.mapper.writeValue(this.out, message);
      this.out.write(this.separator.getBytes());
    } catch (IOException e) {
      channel.getSession().disconnect();
    }
  }

  public void setObjectMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public void setOut(OutputStream out) {
    this.out = out;
  }

  public void setSeparator(String separator) {
    this.separator = separator;
  }
}
