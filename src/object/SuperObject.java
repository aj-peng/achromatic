package object;

import main.GamePanel;
import main.Sprite;
import main.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SuperObject implements Sprite {
    Utility utility = new Utility();
    GamePanel gp;

    public String name;
    public int worldX, worldY;
    public boolean collision = false;

    public BufferedImage image;
    public int imageOffsetX, imageOffsetY;

    public Rectangle hitbox;
    public int hitboxDefaultX, hitboxDefaultY;

    public SuperObject(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (gp.player.visible(worldX, worldY)) {
            g2.drawImage(image, screenX + imageOffsetX, screenY + imageOffsetY, null);
            if (gp.debug) {
                g2.setColor(Color.RED);
                g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
            }
        }
    }

    public void setImage(String imageName, int dimension) {
        try {
            image = utility.scaleImage(
                    ImageIO.read(Objects.requireNonNull(getClass().getResource("/object/" + imageName + ".png"))),
                    dimension, dimension);
            imageOffsetX = (gp.tileSize - image.getWidth()) / 2;
            imageOffsetY = (gp.tileSize - image.getHeight()) / 2;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setHitbox(int dimension) {
        hitboxDefaultX = (gp.tileSize - dimension) / 2;
        hitboxDefaultY = (gp.tileSize - dimension) / 2;
        hitbox = new Rectangle(hitboxDefaultX, hitboxDefaultY, dimension, dimension);
    }

    public int getSpriteOrder() {
        return worldY;
    }
}
