package cometd.client.service;

import okhttp3.OkHttpClient;
import org.cometd.client.BayeuxClient;
import org.cometd.client.http.okhttp.OkHttpClientTransport;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.websocket.okhttp.OkHttpWebSocketTransport;

public class BayeuxClientFactory {
  public BayeuxClient create(String server) {
    OkHttpClient ok = new OkHttpClient();
    ClientTransport ws = new OkHttpWebSocketTransport(null, ok);
    ClientTransport http = new OkHttpClientTransport(null, ok);
    BayeuxClient bayeux = new BayeuxClient(server, ws, http);

    return bayeux;
  }
}
