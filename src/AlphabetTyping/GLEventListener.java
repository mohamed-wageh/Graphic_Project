package AlphabetTyping;

import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GLEventListener extends Listener{
    int screenWidth =800;
    int screenHeight = 800;
    boolean isGameVisible;
    char currentChar;
    int x,y;
    long startTime,elapsedTime;
    int score=0,missingChar=0;

    String textureNames[] = {
            "a.png","b.png","c.png","d.png","e.png","f.png","g.png","h.png", "i.png",
            "j.png","k.png","l.png","m.png","n.png","o.png","p.png","q.png", "r.png",
            "s.png","t.png","u.png","v.png","w.png","x.png","y.png","z.png", "0.png",
            "1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png","9.png",
            "Back.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];
    GL gl;

    public void init(GLAutoDrawable gld) {
        gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch( IOException e ) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        startGame();

    }

    public void startGame(){
        isGameVisible = true;
        currentChar = (char) ('a'+Math.random()*26);
        x= (int) (Math.random()*(screenWidth-100));
        y= (int) (Math.random()*(screenHeight-100));

        startTime = System.nanoTime();
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        DrawBackground();

        if (isGameVisible && missingChar<10) {
            int index = currentChar - 'a';
            DrawSprite(x, y, index, 1);

            elapsedTime = System.nanoTime() - startTime;
            long elapsedSeconds = elapsedTime / 1_000_000_000L;
            long timeLeft = 6_000_000_000L-elapsedTime;
            if (elapsedSeconds >= 5) {
                startGame();
                missingChar++;
                startTime = System.nanoTime();
            }
            drawScore();
            displayScore(gl);
            drawWrong();
            displayMissing(gl);
            drawTime();
            displayTime(gl,timeLeft);
        }else {
            isGameVisible = false;
            drawGameOver();
        }

    }

    public void DrawSprite(int x, int y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(screenWidth/2.0) - 0.9, y/(screenHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length-1]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void displayScore(GL gl){
        int offsetX=200;
        String scoreString = String.valueOf(score);
        for (int i = 0; i < scoreString.length(); i++){
            char digit = scoreString.charAt(i);
            int digitIndex = digit - '0'+26;
            DrawSprite(offsetX,screenHeight-68,digitIndex,0.5f);
            offsetX+=50;
        }
    }

    public void displayMissing(GL gl){
        int offsetX=450;
        String scoreString = String.valueOf(missingChar);
        for (int i = 0; i < scoreString.length(); i++){
            char digit = scoreString.charAt(i);
            int digitIndex = digit - '0'+26;
            DrawSprite(offsetX,screenHeight-68,digitIndex,0.5f);
            offsetX+=50;
        }
    }

    public void displayTime(GL gl,long timeLeft){
        int offsetX=700;
        int seconds = (int) (timeLeft /1_000_000_000L);
        String secondsString = String.valueOf(seconds);
        char digit = secondsString.charAt(0);
        int digitIndex = digit - '0'+26;
        DrawSprite(offsetX,screenHeight-68,digitIndex,0.5f);

    }

    public void drawYouWin() {
        int startX = 120; // Starting x-coordinate
        int y = (screenHeight / 2) + 50; // Starting y-coordinate
        float scale = 1.0f; // Scale of the sprites

        // Drawing the letters "YOU WIN"
        DrawSprite(startX, y, 25, scale);      // 'Y'
        DrawSprite(startX + 100, y, 15, scale); // 'O'
        DrawSprite(startX + 200, y, 21, scale); // 'U'
        DrawSprite(startX + 350, y, 23, scale); // 'W'
        DrawSprite(startX + 450, y, 9, scale);  // 'I'
        DrawSprite(startX + 550, y, 14, scale); // 'N'
    }

    public void drawGameOver() {
        int startX = 30; // Starting x-coordinate
        int y = (screenHeight / 2) + 50; // Starting y-coordinate
        float scale = 1.0f; // Scale of the sprites

        // Drawing the letters "GAME OVER"
        DrawSprite(startX+20, y, 6, scale);     // 'G'
        DrawSprite(startX + 100, y, 0, scale); // 'A'
        DrawSprite(startX + 180, y, 12, scale); // 'M'
        DrawSprite(startX +260, y, 4, scale);  // 'E'
        DrawSprite(startX + 420, y, 14, scale); // 'O'
        DrawSprite(startX + 500, y, 21, scale); // 'V'
        DrawSprite(startX + 580, y, 4, scale);  // 'E'
        DrawSprite(startX + 660, y, 17, scale); // 'R'
    }
    public void drawScore() {
        int startX = 0;
        int y = (screenHeight) - 70;
        float scale = 0.4f;
        DrawSprite(startX, y,  18, scale);
        DrawSprite(startX + 35, y,  2, scale);
        DrawSprite(startX + 70, y,  14, scale);
        DrawSprite(startX + 105, y,  17, scale);
        DrawSprite(startX + 140, y,  4, scale);
    }

    public void drawWrong() {
        int startX = 250;
        int y = (screenHeight) - 70;
        float scale = 0.4f;
        DrawSprite(startX, y,  22, scale);
        DrawSprite(startX + 35, y,  17, scale);
        DrawSprite(startX + 70, y,  14, scale);
        DrawSprite(startX + 105, y,  13, scale);
        DrawSprite(startX + 140, y,  6, scale);
    }

    public void drawTime() {
        int startX = 530;
        int y = (screenHeight) - 70;
        float scale = 0.4f;
        DrawSprite(startX, y,  19, scale);
        DrawSprite(startX + 35, y,  8, scale);
        DrawSprite(startX + 70, y,  12, scale);
        DrawSprite(startX + 105, y,  4, scale);

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }
    @Override
    public void keyTyped(KeyEvent e) {
        char typedChar = e.getKeyChar();

        if (Character.isUpperCase(typedChar)) {
            typedChar = Character.toLowerCase(typedChar);
        }

        if (typedChar == currentChar) {
            score++;
            startGame();
        }else {
            missingChar++;
        }
        if (missingChar>=10){
            isGameVisible = false;
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
