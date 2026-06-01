package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InputHandler implements KeyListener {
    GamePanel gp;

    public enum Direction { NONE, UP, DOWN, LEFT, RIGHT }

    private final List<Runnable> space = new ArrayList<>();
    private final List<Runnable> wasd = new ArrayList<>();
    private final Set<Integer> debounce = new HashSet<>();
    public Direction direction = Direction.NONE;
    public boolean moving = false;
    public boolean debug = false;

    public InputHandler(GamePanel gp) { this.gp = gp; }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (debounce.contains(code)) return;
        debounce.add(code);

        if (gp.gameState == gp.titleState) {
            titleState(code);
        }
        else if (gp.gameState == gp.playState) {
            playState(code);
        }
        else if (gp.gameState == gp.pauseState) {
            pauseState(code);
        }
        else if (gp.gameState == gp.dialogueState) {
            dialogueState(code);
        }
        if (code == KeyEvent.VK_BACK_QUOTE) {
            debug = !debug;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        debounce.remove(code);

        if ((code == KeyEvent.VK_W && direction == Direction.UP) ||
                (code == KeyEvent.VK_S && direction == Direction.DOWN) ||
                (code == KeyEvent.VK_A && direction == Direction.LEFT) ||
                (code == KeyEvent.VK_D && direction == Direction.RIGHT)) {
            direction = Direction.NONE;
            moving = false;
        }
    }

    public void titleState(int code) {
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum = Math.clamp(gp.ui.commandNum - 1, 0 , 2);
            gp.playSound(1);
        }
        else if (code == KeyEvent.VK_S) {
            gp.ui.commandNum = Math.clamp(gp.ui.commandNum + 1, 0 , 2);
            gp.playSound(1);
        }
        else if (code == KeyEvent.VK_SPACE) {
            if (gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.playMusic(0);
                gp.playSound(1);
            }
            else if (gp.ui.commandNum == 1) {
                System.out.println("NEW GAME");
            }
            else if (gp.ui.commandNum == 2) {
                System.exit(0);
            }
        }
    }

    public void playState(int code) {
        // Handle movement keys
        switch (code) {
            case KeyEvent.VK_W:
                direction = Direction.UP;
                moving = true;
                movement();
                break;
            case KeyEvent.VK_S:
                direction = Direction.DOWN;
                moving = true;
                movement();
                break;
            case KeyEvent.VK_A:
                direction = Direction.LEFT;
                moving = true;
                movement();
                break;
            case KeyEvent.VK_D:
                direction = Direction.RIGHT;
                moving = true;
                movement();
                break;
            case KeyEvent.VK_SPACE:
                interact();
                break;
            case KeyEvent.VK_ESCAPE:
                gp.ui.commandNum = 0;
                gp.gameState = gp.pauseState;
                break;
        }
    }

    public void pauseState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        else if (code == KeyEvent.VK_W) {
            gp.ui.commandNum = Math.clamp(gp.ui.commandNum - 1, 0 , 1);
            gp.playSound(1);
        }
        else if (code == KeyEvent.VK_S) {
            gp.ui.commandNum = Math.clamp(gp.ui.commandNum + 1, 0 , 1);
            gp.playSound(1);
        }
        else if (code == KeyEvent.VK_SPACE) {
            if (gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
            }
            else if (gp.ui.commandNum == 1) {
                System.exit(0);
            }
        }
    }

    public void dialogueState(int code) {
        if (code == KeyEvent.VK_SPACE) {
            gp.gameState = gp.playState;
        }
    }

    public void connectInteract(Runnable listener) {
        space.add(listener);
    }

    public void connectMovement(Runnable listener) {
        wasd.add(listener);
    }

    private void interact() {
        for (Runnable listener : space) {
            listener.run();
        }
    }

    private void movement() {
        if (moving) {
            for (Runnable listener : wasd) {
                listener.run();
            }
        }
    }
}
