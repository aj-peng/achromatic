package object;

import main.GamePanel;

public class OBJ_Dice extends SuperObject {
    public OBJ_Dice(GamePanel gp) {
        super(gp);
        name = "Dice";
        collision = false;

        setImage("dice",gp.tileSize / 2 + 4);
        setHitbox(gp.tileSize / 2);
    }
}
