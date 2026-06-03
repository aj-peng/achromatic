package main;

import object.OBJ_Heart;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class UserInterface {
    GamePanel gp;
    Graphics2D g2;

    private final Font maruMonica;
    private final Font textFont, boldFont, titleFont, subOneFont, subTwoFont;

    public int commandNum = 0;
    private String dialogue = "";

    ArrayList<Message> messages = new ArrayList<>();
    BufferedImage heart;

    private static class Message {
        int ticks = 180;
        final String text;
        public Message(String text) {
            this.text = text;
        }
    }

    public UserInterface(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf"));
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // HUD
        SuperObject heart = new OBJ_Heart(gp);
        this.heart = heart.image;

        // FONT
        textFont = maruMonica.deriveFont(24F);
        boldFont = maruMonica.deriveFont(Font.BOLD, 24F);
        titleFont = maruMonica.deriveFont(Font.BOLD, 64F);
        subOneFont = maruMonica.deriveFont(Font.BOLD, 48F);
        subTwoFont = maruMonica.deriveFont(Font.BOLD, 32F);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        switch (gp.gameState) {
            case TITLE -> drawTitleMenu();
            case PLAY -> {
                drawMessage();
                drawPlayerHealth();
            }
            case PAUSE -> {
                drawPauseMenu();
                drawPlayerHealth();
            }
            case DIALOGUE -> {
                drawDialogueMenu();
                drawPlayerHealth();
            }
        }
    }

    public void drawDelta(long delta) {
        int x = gp.tileSize / 4;
        int y = gp.screenHeight - x;

        g2.setFont(textFont);
        g2.setColor(Color.WHITE);
        g2.drawString("Draw: " + delta, x, y);
    }

    public void setDialogue(String text) {
        dialogue = text;
    }

    public void addMessage(String text) {
        messages.add(new Message(text));
    }

    private void removeMessage(int index) {
        messages.remove(index);
    }

    private void drawMessage() {
        int messageX = gp.tileSize / 4;
        int messageY = gp.tileSize * 5;

        g2.setFont(textFont);
        g2.setColor(Color.white);

        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = messages.get(i);
            if (message != null) {
                if (--message.ticks < 1) {
                    removeMessage(i);
                    continue;
                }

                g2.drawString(message.text, messageX, messageY);
                messageY += gp.tileSize / 2;
            }
        }
    }

    private void drawTitleMenu() {
        g2.setColor(Color.YELLOW);
        g2.setFont(titleFont);

        String text = "ACHROMATIC";
        int x = getCenterTextX(text);
        int y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        g2.setColor(Color.white);
        g2.setFont(subOneFont);

        String[] entries = {"PLAY", "NEW GAME", "EXIT"};
        for(int i = 0; i < entries.length; i++) {
            text = entries[i];
            x = getCenterTextX(text);
            y += gp.tileSize * 3/2;
            g2.drawString(text, x, y);
            if (commandNum == i) {
                g2.drawString(">", x-gp.tileSize, y);
            }
        }
    }

    private void drawPauseMenu() {
        String text = "PAUSE";
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(subOneFont);
        g2.setColor(Color.YELLOW);

        int x = getCenterTextX(text);
        int y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        g2.setColor(Color.white);
        g2.setFont(subTwoFont);

        String[] entries = {"RESUME", "EXIT"};
        for(int i = 0; i < entries.length; i++) {
            text = entries[i];
            x = getCenterTextX(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == i) {
                g2.drawString(">", x-gp.tileSize, y);
            }
        }
    }

    private void drawDialogueMenu() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 2;
        int width = gp.screenWidth - 2*x;
        int height = gp.tileSize * 3;
        drawSubWindow(x, y, width, height);

        int padding = 8;
        x += gp.tileSize / 4 + padding;
        y += gp.tileSize / 2 + padding;

        g2.setFont(textFont);
        for (String line : dialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += gp.tileSize / 2 + padding;
        }
    }

    private void drawPlayerHealth() {
        int x = gp.tileSize / 4;
        int y = gp.tileSize / 4;
        g2.drawImage(heart, x, y, null);

        g2.setColor(Color.WHITE);
        g2.setFont(boldFont);
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(gp.player.health + "/" + gp.player.maxHealth,
                x + (gp.tileSize * 2 / 3),
                y + (heart.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent()
        );
    }

    private void drawSubWindow(int x, int y, int width, int height) {
        int arc = 25;
        int stroke = 4;

        Color alpha = new Color(0,0,0,210);
        g2.setColor(alpha);
        g2.fillRoundRect(x, y, width, height, arc, arc);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(stroke));
        g2.drawRoundRect(x + stroke, y + stroke,width - 2*stroke, height - 2*stroke, arc, arc);
    }

    private int getCenterTextX(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (gp.screenWidth - length) / 2;
    }
}
