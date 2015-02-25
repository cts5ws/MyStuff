package Snake;
import java.awt.event.*;//allows the timer to work
import java.awt.*;//draw on the applet

import javax.swing.Timer;//allows existance of the timer
import java.awt.Graphics;
import java.applet.*;

public class Snake extends Applet implements KeyListener, ActionListener
{
	
	private Font fnt20P = new Font("Helvetica", Font.PLAIN, 20);
	private Font fnt36P = new Font("Helvetica", Font.PLAIN, 36);
	private Font fnt60P = new Font("Helvetica", Font.PLAIN, 48);
	
	int WIDTH = 900;
	int HEIGHT = 600;
	
	private Graphics buffG;
	private Image offscreen;
	
	Point[] snake = new Point[500];
	Point[] storage = new Point[500];
	
	Point food = new Point(100, HEIGHT/2);
	int foodDiameter = 10;
	
	int snakeCount = 1;
	int dudeWidth = 10;
	int dudeSpeed = 5;
	
	boolean left = true;
	boolean right = false;
	boolean up = false;
	boolean down = false;
	
	boolean done = false;
	boolean winner = false;
	
	public Button b_PlayAgain;
	
	int count = 0;
	public Timer timer;
	
//**************************************************************************************************************************	
	
	public void update (Graphics g)
	{
		offscreen = createImage(WIDTH,HEIGHT);
		buffG = offscreen.getGraphics();
		buffG.setColor(getBackground());
		buffG.fillRect(0, 0, WIDTH, HEIGHT);
		paint(buffG);
		g.drawImage(offscreen, 0, 0, this);
	}
	
	public void init()
	{
		setSize(WIDTH, HEIGHT);//sets size of the frame
		setLayout(null);//turn off Layout manager
		addKeyListener(this);
		setBackground(Color.black);
		setFocusable(true);
		
		snake[0] = new Point(WIDTH/2,HEIGHT/2);
		storage[0] = new Point(0,0);
		
		for (int i = 1; i<500; i++)
		{
			snake[i] = new Point(-10,-10);
			storage[i] = new Point(-10,-10);
		}	
		
		b_PlayAgain = new Button("PLAY AGAIN"); //construct the button
		b_PlayAgain.setSize(150,50); //set size of button
		b_PlayAgain.setLocation(WIDTH/2 - 85, HEIGHT/2 + 20);//set location
		b_PlayAgain.setFont(fnt20P); //sets font of the button text
		b_PlayAgain.setVisible(false);//button visible
		b_PlayAgain.setBackground(Color.white);//set background color
		b_PlayAgain.setForeground(Color.black);//set text color
		b_PlayAgain.addActionListener(this);//makes button respond to clicks
		add(b_PlayAgain);//adds button to applet

		timer = new Timer(20, new MyTimer());//set timer
		timer.start();//timer star
	}
	
	public void paint(Graphics g)
	{
		//paints snake
		g.setColor(Color.red);	
		for (int i = 0; i < snakeCount; i++) g.fillRect(snake[i].x + dudeWidth/2, snake[i].y + dudeWidth/2, dudeWidth, dudeWidth);
		g.setColor(Color.yellow);
		g.drawOval(food.x + foodDiameter/2, food.y + foodDiameter/2, foodDiameter, foodDiameter);
		g.setColor(Color.white);
		
		//words
		if (done)
		{
			g.setFont(fnt60P);
			g.drawString("GAME OVER", WIDTH/2 - 150, HEIGHT/2);
		}	
		
		if (winner)
		{
			g.setFont(fnt60P);
			g.drawString("YOU WIN", WIDTH/2 - 80, HEIGHT/2);
		}
		
		g.setFont(fnt20P);
		g.drawString("Created by Cole Schafer", WIDTH/2 - 150, HEIGHT - 20);
		g.setFont(fnt36P);
		g.drawString("Score: " + snakeCount, WIDTH - 180, 40);
	}
	
	public void keyPressed (KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_W && down == false)
		{
			left = false;
			right = false;
			up = true;
			down = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_A && right == false)
		{
			left = true;
			right = false;
	        up = false;
			down = false;
		}	
		
		if (e.getKeyCode() == KeyEvent.VK_S && up == false)
		{
			left = false;
			right = false;
			up = false;
			down = true;
		}	
		
		if (e.getKeyCode() == KeyEvent.VK_D && left == false)
		{
			left = false;
			right = true;
			up = false;
			down = false;
		}
		
		requestFocus();//return focus to applet
		repaint();//repaint the applet
	}
	
	public void keyTyped(KeyEvent e) {}//end keytypeds
	public void keyReleased(KeyEvent e)  {}//end keyreleased	
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == b_PlayAgain)
		{
			snakeCount = 1;
			count = 0;
			
			left = true;
			right = false;
			down = false;
			up = false;
			
			done = false;
			winner = false;
			
			snake[0].x = WIDTH/2;
			snake[0].y = HEIGHT/2;
			
			food.x = 100;
			food.y = HEIGHT/2;
			
			for (int i = 1; i < 500; i++)
			{
				snake[i].x = 0;
				snake[i].y = 0;
				storage[i].x = 0;
				storage[i].y = 0;
			}	
			
			
			timer.restart();
			b_PlayAgain.setVisible(false);//button visible
		}	
	}
	
	//Purpose: determine if the game is over
	public void gameOver()
	{
		//checks if character moves off top, bottom, left, right
		if (snake[0].x + dudeWidth/2 <= 0 || (snake[0].x + dudeWidth + dudeWidth/2) >= WIDTH || (snake[0].y + dudeWidth/2) <= 0 || (snake[0].y + dudeWidth + dudeWidth/2) >= HEIGHT ) 
		{
			timer.stop();
			done = true;
			b_PlayAgain.setVisible(true);//button visible
		}
		
		for (int i = 2; i <= snakeCount; i++)
		{
			if (Math.sqrt(Math.pow((snake[0].x + dudeWidth/2) - (snake[i].x + dudeWidth/2), 2) + Math.pow((snake[0].y + dudeWidth/2) - (snake[i].y + dudeWidth/2), 2)) <= (dudeWidth))
			{
				timer.stop();
				done = true;
				b_PlayAgain.setVisible(true);//button visible
			}	
		}	
	}
	
	//purpose: if you win the game
	public void youWin()
	{
		if (snakeCount == 499)
		{
			winner = true;
			timer.stop();
			b_PlayAgain.setVisible(true);//button visible
		}	
	}
	
	//purpose: grow the beast
	public void grow()
	{
		if (Math.sqrt(Math.pow((snake[0].x + dudeWidth/2) - (food.x + foodDiameter/2), 2) + Math.pow((snake[0].y + dudeWidth/2) - (food.y + foodDiameter/2), 2)) <= (dudeWidth/2 + foodDiameter/2))
		{
			snakeCount+=3;
			food.x = ranNum(10,890);
			food.y = ranNum(10,590);
		}
	}
	
	//Purpose: generates random numbers between two values
	//Parameters: min is minimum number you want and max is maximum number you want
	//Postcondition: returns the random number
 	public int ranNum (int min, int max)
	{
		int ranNum = (int)(min + max * Math.random());//random number generate
		return ranNum;
	}//end ranNum
	
	private class MyTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent p1)//method that responds to the timer
		{
			count++;
			
			if (right) snake[0].x += dudeSpeed;
			if (down) snake[0].y += dudeSpeed;
			if (left) snake[0].x += -dudeSpeed;
			if (up) snake[0].y += -dudeSpeed;
			
			for (int j = 0; j<500; j++)
			{
				storage[j].x = snake[j].x;
				storage[j].y = snake[j].y;
			}
			
			if (count%5 == 0)
			{	
				for (int i = 0; i <= snakeCount; i++)
				{
					{
						snake[i+1].x = storage[i].x;
						snake[i+1].y = storage[i].y;
					}
				}
			}
			
			grow();
			gameOver();
			youWin();

			repaint();
		}
	}
	

}
