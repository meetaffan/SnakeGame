import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.sql.Time;
import java.util.Random;

public class myPanel extends JPanel implements KeyListener, ActionListener {
    ImageIcon snaketitle = new ImageIcon(getClass().getResource("snaketitle.jpg"));
    ImageIcon rightmouth = new ImageIcon(getClass().getResource("rightmouth.png"));
    ImageIcon snakeimage = new ImageIcon(getClass().getResource("snakeimage.png"));
    ImageIcon leftmouth = new ImageIcon(getClass().getResource("leftmouth.png"));
    ImageIcon upmouth = new ImageIcon(getClass().getResource("upmouth.png"));
    ImageIcon downmouth = new ImageIcon(getClass().getResource("downmouth.png"));
    ImageIcon food = new ImageIcon(getClass().getResource("enemy.png"));

    boolean isUp = false;
    boolean isDown = false;
    boolean isRight = true;    // at the start of game isRight is true
    boolean isLeft = false;

    //for creating food
    int []xpos = {25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
    int []ypos = {75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625};

    Random random = new Random();
    int foodx=150;
    int foody=150;

    int[] snakex  = new int[750];    // max size of snake can be only till 750 due to contraint of window
    int[] snakey  = new int[750];
    int move=0;
    int lengthOfSnake=3;
    Timer time;
    boolean gameOver=false;
    int score =0;

    myPanel(){
        addKeyListener(this);     // created to provide functionality to the keyboard keys
        setFocusable(true);
        time = new Timer(100,this);  // snake speed
        time.start();
    }

    public void paint(Graphics g){     // paint function is used to draw something over panel
        super.paint(g);
        g.setColor(Color.white);        // g is acting like a pencil
        g.drawRect(24,10, 851,55);
       // g.drawRect(24,74, 851,576);
        snaketitle.paintIcon(this,g,24,11);     // dimension of image is provided by default
        g.setColor(Color.black);      // updated pencil color with black
        g.fillRect(24,74,851,576);   // dimensions was +1 for x,y,width,height
                                                       // can be filled used even by without drawing rectangle
        if(move==0){
            // #123
            snakex[0]=100;        // snake body image pixel is of 25px;
            snakex[1]=75;         // refer copy note
            snakex[2]=50;        // position of snake body while start of the game from x&y co-ordinates

            snakey[0]=100;        // since its on same horizontal at beginning of game
            snakey[1]=100;
            snakey[2]=100;
        }

        if(isRight) rightmouth.paintIcon(this,g,snakex[0],snakey[0]);    // here we painted the first part of body
        if(isDown)  downmouth.paintIcon(this,g,snakex[0],snakey[0]);
        if(isUp) upmouth.paintIcon(this,g,snakex[0],snakey[0]);
        if(isLeft)  leftmouth.paintIcon(this,g,snakex[0],snakey[0]);

        //Now to print the remaining 2 parts of snake, since its length is 3    #443
        //body part at required location
        for(int i=1; i<lengthOfSnake; i++){
            snakeimage.paintIcon(this,g,snakex[i],snakey[i]);   // painted the snake body for position defined before #123 & #443
        }
        food.paintIcon(this,g,foodx,foody);
        if(gameOver) {
            g.setColor(Color.white);
            g.setFont(new Font(Font.SERIF, Font.BOLD,30));
            g.drawString("Game Over",300,300);
            g.setFont(new Font(Font.SERIF,Font.PLAIN, 30));
            g.drawString("Press space key to restart",330,360);
        }
        g.setColor(Color.white);
        g.setFont(new Font("ITALIC", Font.PLAIN, 15));
        g.drawString("Score "+score,750,30);
        g.drawString("Length :"+lengthOfSnake,750,50);
        g.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && (gameOver)) {
            restart();
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT && (!isLeft))
        {
            isUp=false;
            isDown=false;
            isLeft=false;
            isRight=true;
            move++;
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT && (!isRight))
        {
            isUp=false;
            isDown=false;
            isLeft=true;
            isRight=false;
            move++;
        }
        if(e.getKeyCode()==KeyEvent.VK_UP && (!isDown))
        {
            isUp=true;
            isDown=false;
            isLeft=false;
            isRight=false;
            move++;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN && (!isUp))
        {
            isUp=false;
            isDown=true;
            isLeft=false;
            isRight=false;
            move++;
        }
    }

    private void restart() {
        gameOver=false;
        move=0;
        lengthOfSnake=3;
        isLeft=false;
        isRight=true;
        isUp=false;
        isDown=false;
        time.start();
        newFood();
        score=0;
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i = lengthOfSnake-1; i>0; i--){
            snakex[i]=snakex[i-1];  // activated when key is pressed. whatever at i-1 should be now equal to i
            snakey[i]=snakey[i-1];
        }
        if(isLeft) snakex[0]=snakex[0]-25;
        if(isRight) snakex[0]=snakex[0]+25;
        if(isUp) snakey[0]=snakey[0]-25;
        if(isDown) snakey[0]=snakey[0]+25;

        // if going outside of boundary, come back from opposite direction
        if(snakex[0]>850) snakex[0]=25;
        if(snakex[0]<25) snakex[0]=850;
        if(snakey[0]>625) snakey[0]=75;
        if(snakey[0]<75) snakey[0]=625;
        CollisionWithFood();
        CollisionWithBody();
        repaint();
    }

    private void CollisionWithBody() {
        for(int i=lengthOfSnake-1; i>0; i--){
            if(snakex[i]==snakex[0] && snakey[i]==snakey[0]){
                time.stop();
                gameOver=true;
            }
        }
    }

    //function created when snake eats food
    private void CollisionWithFood() {
        if(snakex[0]==foodx && snakey[0]==foody){
            newFood();
            lengthOfSnake++;
            score++;
            //below location will be increased by 1    // doubt
            snakex[lengthOfSnake-1]=snakex[lengthOfSnake-2];
            snakey[lengthOfSnake-1]=snakey[lengthOfSnake-2];
        }
    }

    private void newFood() {
        foodx=xpos[random.nextInt(xpos.length-1)];
        foody=ypos[random.nextInt(ypos.length-1)];
        for(int i=lengthOfSnake-1; i>=0; i--)
        {
            if(snakex[i]==foodx && snakey[i]==foody){
                newFood();
            }
        }
    }
}
