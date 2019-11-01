package Model;

import Model.Units.Drawable;
import java.util.ArrayList;

public interface Observer {
    void update(ArrayList<Drawable> drawablesList,int credits,int points);
    String getNameForHighScore(int finalPoints);
}