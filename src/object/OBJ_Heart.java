package object;

import main.GamePanel;

public class OBJ_Heart extends SuperObject{
    public OBJ_Heart(GamePanel gp) {
        super(gp);
        name = "Heart";
        collision = false;

        setImage("heart",gp.tileSize / 2);
    }
}
