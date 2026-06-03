package object;

import main.GamePanel;

public class OBJ_Door extends SuperObject{
    public OBJ_Door(GamePanel gp) {
        super(gp);
        name = "Door";
        collision = true;

        setImage("door",gp.tileSize);
        setHitbox(gp.tileSize);
    }
}
