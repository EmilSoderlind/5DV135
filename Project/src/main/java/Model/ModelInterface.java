package Model;
import Utils.Status;
public interface ModelInterface {
     void gameLoop();
     void pause();
     void resume();
     Status getStatus();
}
