import java.nio.ByteBuffer;
import javax.websocket.Session;

public class ChannelHealthChecker implements Runnable {

  private Session session;

  public ChannelHealthChecker(Session session) {
    this.session = session;
  }

  @Override
  public void run() {
    while (true) {
      try {
        if (!session.isOpen()) {
          return;
        }
        session.getAsyncRemote().sendPing(ByteBuffer.wrap("reproducer".getBytes()));
        Thread.sleep(3);
      } catch (Exception e) {
        return;
      }
    }
  }
}