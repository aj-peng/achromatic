package main;

import entity.Entity;
import entity.NPC_Blue;
import object.OBJ_Dice;
import object.OBJ_Door;
import object.SuperObject;

public class Setter {
    GamePanel gp;

    public Setter(GamePanel gp) {
        this.gp = gp;
    }

    public void setNPC() {
        addNPC(0,12,2,new NPC_Blue(gp));
    }

    public void setObject() {
        addObject(0,2,2,new OBJ_Door(gp));
        addObject(1,4,4,new OBJ_Dice(gp));
    }

    private void addNPC(int index, int col, int row, Entity npc) {
        if (gp.npcs[index] != null) return;
        npc.worldX = gp.tileSize * col;
        npc.worldY = gp.tileSize * row;
        gp.npcs[index] = npc;
    }

    private void addObject(int index, int col, int row, SuperObject object) {
        if (gp.objs[index] != null) return;
        object.worldX = gp.tileSize * col;
        object.worldY = gp.tileSize * row;
        gp.objs[index] = object;
    }
}
