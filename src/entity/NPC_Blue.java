package entity;

import main.GamePanel;

import java.awt.*;

public class NPC_Blue extends Entity {
    public NPC_Blue(GamePanel gp) {
        super(gp);

        idle = true;
        direction = Direction.DOWN;

        hitbox = new Rectangle(12,16,24,32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        setImages();
        setDialogue();
    }

    public void update() {
        if (direction != Direction.DOWN && gp.gameState == GamePanel.GameState.PLAY) {
            direction = Direction.DOWN;
        }
    }

    public void speak() {
        super.speak();
    }

    private void setImages() {
        up1 = setup("/npc/blue_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/blue_up_2", gp.tileSize, gp.tileSize);
        up3 = setup("/npc/blue_up_3", gp.tileSize, gp.tileSize);

        down1 = setup("/npc/blue_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/blue_down_2", gp.tileSize, gp.tileSize);
        down3 = setup("/npc/blue_down_3", gp.tileSize, gp.tileSize);

        left1 = setup("/npc/blue_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/blue_left_2", gp.tileSize, gp.tileSize);
        left3 = setup("/npc/blue_left_3", gp.tileSize, gp.tileSize);

        right1 = setup("/npc/blue_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/blue_right_2", gp.tileSize, gp.tileSize);
        right3 = setup("/npc/blue_right_3", gp.tileSize, gp.tileSize);
    }

    private void setDialogue() {
        dialogues[0] = "I have nothing but my sorrow, and I want nothing more.";
        dialogues[1] = "Oh sorrow, I have ended, you see, by respecting you, \nbecause I am certain you will never leave me.";
        dialogues[2] = "Ah, I realize it! Your beauty lies in the force of your being.";
    }
}
