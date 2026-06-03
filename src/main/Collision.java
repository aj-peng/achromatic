package main;

import entity.Entity;

public class Collision {
    GamePanel gp;

    public Collision(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.hitbox.x;
        int entityRightWorldX = entity.worldX + entity.hitbox.x + entity.hitbox.width;
        int entityTopWorldY = entity.worldY + entity.hitbox.y;
        int entityBottomWorldY = entity.worldY + entity.hitbox.y + entity.hitbox.height;
        int tileNum1, tileNum2;

        switch (entity.direction) {
            case Entity.Direction.UP -> {
                entityTopWorldY = entityTopWorldY - entity.speed;
                tileNum1 = gp.tileManager.getTileNum(entityLeftWorldX, entityTopWorldY);
                tileNum2 = gp.tileManager.getTileNum(entityRightWorldX, entityTopWorldY);
                if (getTileCollisions(tileNum1, tileNum2)) entity.collision = true;
            }
            case Entity.Direction.DOWN -> {
                entityBottomWorldY = entityBottomWorldY + entity.speed;
                tileNum1 = gp.tileManager.getTileNum(entityLeftWorldX, entityBottomWorldY);
                tileNum2 = gp.tileManager.getTileNum(entityRightWorldX, entityBottomWorldY);
                if (getTileCollisions(tileNum1, tileNum2)) entity.collision = true;
            }
            case Entity.Direction.LEFT -> {
                entityLeftWorldX = entityLeftWorldX - entity.speed;
                tileNum1 = gp.tileManager.getTileNum(entityLeftWorldX, entityTopWorldY);
                tileNum2 = gp.tileManager.getTileNum(entityLeftWorldX, entityBottomWorldY);
                if (getTileCollisions(tileNum1, tileNum2)) entity.collision = true;
            }
            case Entity.Direction.RIGHT -> {
                entityRightWorldX = entityRightWorldX + entity.speed;
                tileNum1 = gp.tileManager.getTileNum(entityRightWorldX, entityTopWorldY);
                tileNum2 = gp.tileManager.getTileNum(entityRightWorldX, entityBottomWorldY);
                if (getTileCollisions(tileNum1, tileNum2)) entity.collision = true;
            }
        }
    }

    public void checkPlayer(Entity entity) {
        convertWorldHitbox(entity);
        convertWorldHitbox(gp.player);
        updateWorldHitbox(entity);

        if (entity.hitbox.intersects(gp.player.hitbox)) {
            entity.collision = true;
        }

        resetHitbox(entity);
        resetHitbox(gp.player);
    }

    public int checkEntity(Entity entity, Entity[] targets) {
        int index = 999;
        convertWorldHitbox(entity);
        updateWorldHitbox(entity);

        for (int i = 0; i < targets.length; i++) {
            if (targets[i] != null) {
                convertWorldHitbox(targets[i]);
                if (entity.hitbox.intersects(targets[i].hitbox)) {
                    index = i;
                }
                resetHitbox(targets[i]);
            }
        }

        resetHitbox(entity);
        return index;
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        convertWorldHitbox(entity);
        updateWorldHitbox(entity);

        for (int i = 0; i < gp.objs.length; i++) {
            if (gp.objs[i] != null) {
                gp.objs[i].hitbox.x = gp.objs[i].worldX + gp.objs[i].hitbox.x;
                gp.objs[i].hitbox.y = gp.objs[i].worldY + gp.objs[i].hitbox.y;
                if (entity.hitbox.intersects(gp.objs[i].hitbox)) {
                    if (gp.objs[i].collision) {
                        entity.collision = true;
                    }
                    if (player) {
                        index = i;
                    }
                }
                gp.objs[i].hitbox.x = gp.objs[i].hitboxDefaultX;
                gp.objs[i].hitbox.y = gp.objs[i].hitboxDefaultY;
            }
        }

        resetHitbox(entity);
        return index;
    }

    private void resetHitbox(Entity entity) {
        entity.hitbox.x = entity.hitboxDefaultX;
        entity.hitbox.y = entity.hitboxDefaultY;
    }

    private void updateWorldHitbox(Entity entity) {
        switch (entity.direction) {
            case Entity.Direction.UP -> entity.hitbox.y -= entity.speed;
            case Entity.Direction.DOWN -> entity.hitbox.y += entity.speed;
            case Entity.Direction.LEFT -> entity.hitbox.x -= entity.speed;
            case Entity.Direction.RIGHT -> entity.hitbox.x += entity.speed;
        }
    }

    private void convertWorldHitbox(Entity entity) {
        entity.hitbox.x = entity.worldX + entity.hitbox.x;
        entity.hitbox.y = entity.worldY + entity.hitbox.y;
    }

    private boolean getTileCollisions (int tileNum1, int tileNum2) {
        return gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision;
    }
}
