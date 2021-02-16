module cometd.client {
    requires info.picocli;
    requires javafx.controls;
    requires kotlin.stdlib;
    requires okhttp3;
    requires org.cometd.client.http.okhttp;
    requires org.cometd.client.websocket.okhttp;
    requires org.eclipse.jetty.util.ajax;
    requires org.slf4j;

    exports cometd.client.command;
}
