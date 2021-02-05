
package snake.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener{
    
    private Image apple;
    private Image dot;
    private Image head;
    
    private final int DOT_SIZE = 10;  // total dots will be 300*300/10*10 = 90000/100 = 900
    private final int ALL_DOTS = 900;
    private final int RANDOM = 29;
    
    private int apple_x;
    private int apple_y;
    
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    
    
    private int dots;
    
    private Timer timer; 
    
    private JButton b1, b2;
    
    Board(){
        
        
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(new TAdapter());
        
        setBackground(Color.black);
        setPreferredSize(new Dimension(300, 300));
        
        setFocusable(true);
        
        loadImages();
        initGame();
    }
    
    public void loadImages(){
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("snake/game/icons/apple.png"));
        apple = i1.getImage();
        
        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("snake/game/icons/dot.png"));
        dot = i2.getImage();
        
        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("snake/game/icons/head.png"));
        head = i3.getImage();
        
    }
    
    public void initGame(){
        
        dots = 3;
        
        for(int i=0; i<dots; i++){
            x[i] = 50 - i*DOT_SIZE;   // 1st dot -> 50 2nd dot -> 50-10= 40 and 3rd -> 30
            y[i] = 50;
        }
        
        locateApple();
        
        timer = new Timer(140, this);
        timer.start();
        
    }
    
    public void locateApple(){
        int r = (int)(Math.random() * RANDOM);
        apple_x = (r*DOT_SIZE);
        
        r = (int)(Math.random() * RANDOM);
        apple_y = (r*DOT_SIZE);
        
    }
    
    public void checkApple(){
        
        if((x[0] == apple_x) && (y[0] == apple_y)){
            dots++;
            locateApple();
        }
        
    }
    
    public void checkCollision(){
        
        for(int i = dots; i>0; i--){
            if((i>4) && (x[0] == x[i]) && (y[0]==y[i])){
                inGame = false;
            }
        }
        
        if(y[0]>=300 || y[0]<0){
            inGame = false;
        }
        if(x[0]>=300 || x[0]<0){
            inGame = false;
        }
        
        if(!inGame){
            timer.stop();
        }
        
    }
    
    public void move(){
        
        for(int i = dots; i>0; i--){
            x[i] = x[i-1];  //as it will move where it the 
            y[i] = y[i-1];  //dot ahead of it was
        }
        
        if(leftDirection){
            x[0] -= DOT_SIZE;  //as dot will move left from where it was by its size
        }
        if(rightDirection){
            x[0] += DOT_SIZE;
        }
        if(upDirection){
            y[0] -= DOT_SIZE;
        }
        if(downDirection){
            y[0] += DOT_SIZE;
        }
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g){
        if(inGame){
            g.drawImage(apple, apple_x, apple_y, this);
            
            for(int i=0; i<dots; i++){
                if(i==0){
                    g.drawImage(head, x[i], y[i], this);
                }else{
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            
            Toolkit.getDefaultToolkit().sync();
        }else{
            gameOver(g);
        }
    }
    
    public void gameOver(Graphics g){
        String msg = "GAME OVER";
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);
        
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (300 - metrics.stringWidth(msg))/2, 300/2);
        
        b1 = new JButton("Replay");
        b1.setBackground(Color.black);
        b1.setForeground(Color.white);
        b1.setBounds(60, 180, 80, 30);
        b1.addActionListener(this);
        add(b1);
        
        b2 = new JButton("Quit");
        b2.setBackground(Color.black);
        b2.setForeground(Color.white);
        b2.setBounds(160, 180, 80, 30);
        b2.addActionListener(this);
        add(b2);
        
        JLabel l1 = new JLabel("SCORE : " + Integer.toString(dots-3));
        l1.setForeground(Color.yellow);
        l1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l1.setBounds(120, 50, 150, 30);
        add(l1);
        
        //System.out.print(dots);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        if(ae.getSource()==b1){
            new Snake().setVisible(true);
            this.setVisible(false);
        }else if(ae.getSource()==b2){
            System.exit(0);
        } 
        
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        
        repaint();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private class TAdapter extends KeyAdapter{
        
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            
            if(key == KeyEvent.VK_LEFT && (!rightDirection)){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && (!leftDirection)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_UP && (!downDirection)){
                leftDirection = false;
                upDirection = true;
                rightDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && (!upDirection)){
                leftDirection = false;
                rightDirection = false;
                downDirection = true;
            }
            
        }
        
    }
    
}
