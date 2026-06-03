package entity;

import main.GamePanel;
import main.Sprite;
import main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity implements Sprite {
    Utility utility = new Utility();
    GamePanel gp;

    public int worldX, worldY, speed;
    public boolean idle = false;

    public int spriteNum = 1;
    public int spriteCounter = 0;
    public final int interval = 8;

    public enum Direction { NONE, UP, DOWN, LEFT, RIGHT }
    public Direction direction;
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;

    public Rectangle hitbox = new Rectangle(0,0,48,48);
    public int hitboxDefaultX, hitboxDefaultY;
    public boolean collision = false;

    // public int actionLock = 0;
    public int dialogueIndex = 0;
    String[] dialogues = new String[4];

    // ENTITY STATUS
    public int maxHealth;
    public int health;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void update() {
        adjustAction();

        collision = false;
        gp.collision.checkTile(this);
        gp.collision.checkObject(this, false);
        gp.collision.checkPlayer(this);

        if (!collision && gp.gameState == GamePanel.GameState.PLAY) {
            idle = false;
            switch (direction) {
                case Direction.UP -> worldY -= speed;
                case Direction.DOWN -> worldY += speed;
                case Direction.LEFT -> worldX -= speed;
                case Direction.RIGHT -> worldX += speed;
            }

            spriteCounter++;
            if (spriteCounter > interval) {
                spriteNum = (spriteNum >= 4) ? 1 : spriteNum + 1;
                spriteCounter = 0;
            }
        }
        else if (!idle) {
            idle = true;
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            BufferedImage image = getImage();
            g2.drawImage(image, screenX, screenY, null);
            if (gp.debug) {
                g2.setColor(Color.RED);
                g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
            }
        }
    }

    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            gp.gameState = GamePanel.GameState.PLAY;
            dialogueIndex = 0;
            return;
        }

        gp.ui.setDialogue(dialogues[dialogueIndex]);
        dialogueIndex++;
        idle = true;

        switch (gp.player.direction) {
            case Direction.UP -> direction = Direction.DOWN;
            case Direction.DOWN -> direction = Direction.UP;
            case Direction.LEFT -> direction = Direction.RIGHT;
            case Direction.RIGHT -> direction = Direction.LEFT;
        }
    }

    public void adjustAction() {}

    public void adjustHealth(int amount) {
        gp.player.health = Math.clamp(gp.player.health + amount, 0, gp.player.maxHealth);
    }

    public int getSpriteOrder() {
        return worldY;
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        BufferedImage image;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = utility.scaleImage(image, width, height);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  image;
    }

    public BufferedImage getImage() {
        return switch (direction) {
            case Direction.UP -> getFrame(up1, up2, up3);
            case Direction.DOWN -> getFrame(down1, down2, down3);
            case Direction.LEFT -> getFrame(left1, left2, left3);
            case Direction.RIGHT -> getFrame(right1, right2, right3);
            default -> null;
        };
    }

    private BufferedImage getFrame(BufferedImage frame1, BufferedImage frame2, BufferedImage frame3) {
        if (idle || collision) {
            return switch (direction) {
                case Direction.UP, Direction.DOWN -> frame2;
                default -> frame1;
            };
        }
        return switch (spriteNum) {
            case 1 -> frame1;
            case 3 -> frame3;
            default -> frame2;
        };
    }
}
