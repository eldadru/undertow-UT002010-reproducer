import java.nio.ByteBuffer;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

public class ReproducerWebSocketEndpoint extends Endpoint {

  @Override
  public void onClose(Session session, CloseReason closeReason) {
    super.onClose(session, closeReason);
  }

  @Override
  public void onError(Session session, Throwable thr) {
    super.onError(session, thr);
  }

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    session.setMaxIdleTimeout(0); // disable max idle timeout
    //session.addMessageHandler(new EchoMessageHandler(session));
    session.addMessageHandler(new Whole<ByteBuffer>() {
      @Override
      public void onMessage(ByteBuffer message) {
      }
    });

    ChannelHealthChecker channelHealthChecker = new ChannelHealthChecker(session);
    Thread healthCheckThread = new Thread(channelHealthChecker);
    healthCheckThread.start();

  }
}