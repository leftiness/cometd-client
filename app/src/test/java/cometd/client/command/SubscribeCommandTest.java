package cometd.client.command;

import org.cometd.bayeux.Channel;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient.State;
import org.cometd.client.BayeuxClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import cometd.client.service.BayeuxClientFactory;

@ExtendWith(MockitoExtension.class)
public class SubscribeCommandTest {

  @Mock
  private BayeuxClientFactory clientFactory;

  @Mock
  private BayeuxClient client;

  @Mock
  private ClientSessionChannel channel;

  @Test
  public void callShouldReturnTimeoutWhenClientNeverConnects() {
    Mockito.doReturn(client).when(clientFactory).create("server");
    Mockito.doReturn(channel).when(client).getChannel(Channel.META_HANDSHAKE);
    Mockito.doReturn(false).when(client).waitFor(1000, State.CONNECTED);

    SubscribeCommand command = new SubscribeCommand();
    command.setTimeout(1000);
    command.setServer("server");
    command.setBayeuxClientFactory(clientFactory);

    Integer expected = ExitCode.ERROR_SUBSCRIBE_TIMEOUT;
    Integer actual = command.call();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void callShouldReturnDisconnectedWhenClientLosesConnection() {
    Mockito.doReturn(client).when(clientFactory).create("server");
    Mockito.doReturn(channel).when(client).getChannel(Channel.META_HANDSHAKE);
    Mockito.doReturn(true).when(client).waitFor(1000, State.CONNECTED);
    Mockito
        .doReturn(false)
        .when(client)
        .waitFor(Long.MAX_VALUE, State.DISCONNECTED);

    SubscribeCommand command = new SubscribeCommand();
    command.setTimeout(1000);
    command.setServer("server");
    command.setBayeuxClientFactory(clientFactory);

    Integer expected = ExitCode.ERROR_SUBSCRIBE_DISCONNECTED;
    Integer actual = command.call();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void callShouldReturnOkWhenClientLivesForever() {
    Mockito.doReturn(client).when(clientFactory).create("server");
    Mockito.doReturn(channel).when(client).getChannel(Channel.META_HANDSHAKE);
    Mockito.doReturn(true).when(client).waitFor(1000, State.CONNECTED);
    Mockito
        .doReturn(true)
        .when(client)
        .waitFor(Long.MAX_VALUE, State.DISCONNECTED);

    SubscribeCommand command = new SubscribeCommand();
    command.setTimeout(1000);
    command.setServer("server");
    command.setBayeuxClientFactory(clientFactory);

    Integer expected = ExitCode.OK;
    Integer actual = command.call();
    Assertions.assertEquals(expected, actual);
  }
}
