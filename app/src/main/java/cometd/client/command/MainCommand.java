package cometd.client.command;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import picocli.CommandLine.Command;

@Command(name = "cometd", subcommands = { SubscribeCommand.class })
public class MainCommand extends Application implements Runnable {
  @Override
  public void run() {
    launch();
  }

  @Override
  public void start(Stage stage) {
    Label l = new Label("hello world");
    Scene scene = new Scene(new StackPane(l), 640, 480);
    stage.setScene(scene);
    stage.show();
  }
}
