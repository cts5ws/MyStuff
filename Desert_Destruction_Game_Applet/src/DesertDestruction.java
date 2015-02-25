// Cole Schafer

import java.awt.event.*;//allows the timer to work
import java.awt.*;//draw on the applet
import javax.swing.Timer;//allows existance of the timer
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.applet.*;
import java.applet.AudioClip;


public class DesertDestruction extends Applet implements KeyListener, MouseListener, MouseMotionListener, ActionListener
{	
	
	private static final long serialVersionUID = 1L;
	//VARIABLES
	//**************************************************************************************************
	
	//Fonts
	private Font fnt36P = new Font("Papyrus", Font.PLAIN, 36);
	private Font fnt16P = new Font("Helvetica", Font.PLAIN, 16);
	private Font fnt20P = new Font("Helvetica", Font.PLAIN, 20);
	private Font fnt24P = new Font("Papyrus", Font.PLAIN, 24);
	private Font fnt60P = new Font("Papyrus", Font.PLAIN, 48);
	
	//Colors
	private Color black = new Color(0,0,0);
	private Color white = new Color(255,255,255);
	private Color brown = new Color(205, 133, 63);

	//Bullet Variables
	int numBull = 0, maxBull = 10;//numBull is current number of bullets and maxBull
	double[] changeX = new double[maxBull];//array that keeps track of change in X values for the bullets
	double[] changeY = new double[maxBull];//array that keeps track of the change in Y values for the bullets
	double[] angle = new double[maxBull];//array that keeps track of angle to shoot the bullet when mouse is clicked
	boolean[] bullExist = new boolean[maxBull];//array that keeps track of whether each bullet currently exists
	Bullet[] bullArr = new Bullet[maxBull];//array that keeps track of bullet objects
	
	//Mine Variables
	int numMine = 0, maxMine = 10;//numMine keeps track of mines and maxMine is the maximum number of mines that can be in existance at once
	boolean[] mineExist = new boolean[maxMine];//array of booleans that keeps track of existance of mines
	Mine[] mineArr = new Mine[maxMine];//array that keeps track of boolean object
	
	//Projectile Variables
	int numProAlive;//number of projectiles
	Projectile[] proArr = new Projectile[44];//array of projectiles
	
	//Number of Projectiles per level
	int levelOnePro = 8;
	int levelTwoPro = 12;
	int levelThreePro = 16;
	int levelFourPro = 20;
	int levelFivePro = 24;
	int levelSixPro = 28;
	int levelSevenPro = 32;
	int levelEightPro = 36;
	int levelNinePro = 40;
	int levelTenPro = 44;
	
	//Number of Bullets/Ammo Per Level
	int levelOneAmmo = 10;
	int levelTwoAmmo = 20;
	int levelThreeAmmo = 30;
	int levelFourAmmo = 40;
	int levelFiveAmmo = 50;
	int levelSixAmmo = 50;
	int levelSevenAmmo = 50;
	int levelEightAmmo = 50;
	int levelNineAmmo = 50;
	int levelTenAmmo = 50;
	
	//Number of Mines Per Level
	int levelOneMine = 10;
	int levelTwoMine = 10;
	int levelThreeMine = 10;
	int levelFourMine = 10;
	int levelFiveMine = 10;
	int levelSixMine = 10;
	int levelSevenMine = 10;
	int levelEightMine = 10;
	int levelNineMine = 10;
	int levelTenMine = 10;
	
	int currentAmmo = levelOneAmmo;//keeps track of current number of ammo in possesion
	int currentMine = levelOneMine;//keeps track of number of mine in possesion
	
	int currentLevel = 0;//keeps track of the current level
	
	//time variables
	int tenSec = 0;
	int sec = 0;
	int min = 0;
	
	//graphic buffer and repaint
	private Graphics buffG;
	private Image offscreen;
	
	public static int WIDTH;//applet size
	public static int HEIGHT;
	
	public Point p1 = new Point (-100,-100);//point defined for character
	int charDiameter = 20;//radius of character
	final int UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;//directions for character
	int jump = 20;//pixel jump for character
	
	boolean stillAlive = true;//true if character is alive, false if character is not
	
	int f48Char = 32;//estimate at how many pixels each character in 60fnt is in width
	
	//Buttons
	public Button b_PlayAgain;
	public Button b_LevelTwo;
	public Button b_LevelThree;
	public Button b_LevelFour;
	public Button b_LevelFive;
	public Button b_LevelSix;
	public Button b_LevelSeven;
	public Button b_LevelEight;
	public Button b_LevelNine;
	public Button b_LevelTen;
	public Button b_Pause;
	public Button b_Play;
	public Button b_Start;
	public Button b_Close;
	
	//ImageIcon variables
	ImageIcon up;
	ImageIcon down;
	ImageIcon left;
	ImageIcon right;
	ImageIcon roid;
	ImageIcon mine;
	ImageIcon bullet;
	ImageIcon explosion;
	ImageIcon bourne;
	
	
	//keeps track of which picture to display for character on screen
	boolean moveUp = false;
	boolean moveDown = true;
	boolean moveLeft = false;
	boolean moveRight = false;
	
	//if true, explosion will occur on either character, bullet, or mine
	boolean charBoom = false;
	boolean bulletBoom = false;
	boolean mineBoom = false;
	
	//locations for bullet explosion and mine explosion
	public Point bullExLoc = new Point(-100,100);
	public Point mineExLoc = new Point(-100,100);
	
	//used as increment to determine how long explosion animation appears on screen
	int charExCount = 0;
	int bullExCount = 0;
	int mineExCount = 0;
	
	int score = 0;//keeps track of score
	int hitProjBull = 100;
	int hitProjMine = 50;
	int remainingAmmo = 20;
	int scoreLoss = 1000;
	
	boolean offScreen = false;//false until character attempts to move offscreen
	
	boolean timerOn = false;//true if timer is running,
	
	AudioClip gunshot;
	AudioClip boom;
	
	private Timer timer;//establish timer
	int count = 0;//incriments as timer runs
	
	//JAVA METHODS
	//*************************************************************************************************************************
	
	//Purpose: updates the screen so no shutter
	public void update (Graphics g)
	{
		offscreen = createImage(WIDTH,HEIGHT);
		buffG = offscreen.getGraphics();
		buffG.setColor(getBackground());
		buffG.fillRect(0, 0, WIDTH, HEIGHT);
		paint(buffG);
		g.drawImage(offscreen, 0, 0, this);

	}//end update
	
	public void init()
	{	
		gunshot = getAudioClip(getCodeBase(), "gunshot.wav");
		boom = getAudioClip(getCodeBase(), "blast.wav");
		
		setLayout(new FlowLayout());
		
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));//sets cross hair as cursor
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//gets screen size of monitor
		WIDTH = screenSize.width;//sets width and height to monitor size
		HEIGHT = screenSize.height - 60;
		
		setSize(WIDTH, HEIGHT);//sets size of the frame
		
		setLayout(null);//turn off Layout manager
		setBackground(brown);//white background
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		
		b_PlayAgain = new Button("PLAY AGAIN"); //construct the button
		b_PlayAgain.setSize(170,110); //set size of button
		b_PlayAgain.setLocation(WIDTH/2 - 85, HEIGHT/2 + 20);//set location
		b_PlayAgain.setFont(fnt20P); //sets font of the button text
		b_PlayAgain.setVisible(false);//button visible
		b_PlayAgain.setBackground(white);//set background color
		b_PlayAgain.setForeground(black);//set text color
		b_PlayAgain.addActionListener(this);//makes button respond to clicks
		add(b_PlayAgain);//adds button to applet
		
		b_LevelTwo = new Button("START LEVEL TWO"); //construct the button
		b_LevelTwo.setSize(250,50); //set size of button
		b_LevelTwo.setLocation(WIDTH/2 - 125, HEIGHT/2 + 20);//set location
		b_LevelTwo.setFont(fnt20P); //sets font of the button text
		b_LevelTwo.setVisible(false);//button visible
		b_LevelTwo.setBackground(white);//set background color
		b_LevelTwo.setForeground(black);//set text color
		b_LevelTwo.addActionListener(this);//makes button respond to clicks
		add(b_LevelTwo);//adds button to applet
		
		b_LevelThree = new Button("START LEVEL THREE"); //construct the button
		b_LevelThree.setSize(250,50); //set size of button
		b_LevelThree.setLocation(WIDTH/2 - 125, HEIGHT/2 + 20);//set location
		b_LevelThree.setFont(fnt20P); //sets font of the button text
		b_LevelThree.setVisible(false);//button visible
		b_LevelThree.setBackground(white);//set background color
		b_LevelThree.setForeground(black);//set text color
		b_LevelThree.addActionListener(this);//makes button respond to clicks
		add(b_LevelThree);//adds button to applet
		
		b_LevelFour = new Button("START LEVEL FOUR"); //construct the button
		b_LevelFour.setSize(250 - 125,50); //set size of button
		b_LevelFour.setLocation(WIDTH/2, HEIGHT/2 + 20);//set location
		b_LevelFour.setFont(fnt20P); //sets font of the button text
		b_LevelFour.setVisible(false);//button visible
		b_LevelFour.setBackground(white);//set background color
		b_LevelFour.setForeground(black);//set text color
		b_LevelFour.addActionListener(this);//makes button respond to clicks
		add(b_LevelFour);//adds button to applet
		
		b_LevelFive = new Button("START LEVEL FIVE"); //construct the button
		b_LevelFive.setSize(250 - 125,50); //set size of button
		b_LevelFive.setLocation(WIDTH/2, HEIGHT/2 + 20);//set location
		b_LevelFive.setFont(fnt20P); //sets font of the button text
		b_LevelFive.setVisible(false);//button visible
		b_LevelFive.setBackground(white);//set background color
		b_LevelFive.setForeground(black);//set text color
		b_LevelFive.addActionListener(this);//makes button respond to clicks
		add(b_LevelFive);//adds button to applet
		
		b_LevelSix = new Button("START LEVEL SIX"); //construct the button
		b_LevelSix.setSize(250,50); //set size of button
		b_LevelSix.setLocation(WIDTH/2 - 125, HEIGHT/2 + 20);//set location
		b_LevelSix.setFont(fnt20P); //sets font of the button text
		b_LevelSix.setVisible(false);//button visible
		b_LevelSix.setBackground(white);//set background color
		b_LevelSix.setForeground(black);//set text color
		b_LevelSix.addActionListener(this);//makes button respond to clicks
		add(b_LevelSix);//adds button to applet
		
		b_LevelSeven = new Button("START LEVEL Seven"); //construct the button
		b_LevelSeven.setSize(250,50); //set size of button
		b_LevelSeven.setLocation(WIDTH/2 - 125, HEIGHT/2 + 20);//set location
		b_LevelSeven.setFont(fnt20P); //sets font of the button text
		b_LevelSeven.setVisible(false);//button visible
		b_LevelSeven.setBackground(white);//set background color
		b_LevelSeven.setForeground(black);//set text color
		b_LevelSeven.addActionListener(this);//makes button respond to clicks
		add(b_LevelSeven);//adds button to applet
		
		b_LevelEight = new Button("START LEVEL EIGHT"); //construct the button
		b_LevelEight.setSize(250,50); //set size of button
		b_LevelEight.setLocation(WIDTH/2 - 125, HEIGHT/2 + 20);//set location
		b_LevelEight.setFont(fnt20P); //sets font of the button text
		b_LevelEight.setVisible(false);//button visible
		b_LevelEight.setBackground(white);//set background color
		b_LevelEight.setForeground(black);//set text color
		b_LevelEight.addActionListener(this);//makes button respond to clicks
		add(b_LevelEight);//adds button to applet
		
		b_LevelNine = new Button("START LEVEL NINE"); //construct the button
		b_LevelNine.setSize(250,50); //set size of button
		b_LevelNine.setLocation(WIDTH/2 - 125, HEIGHT/2 + 20);//set location
		b_LevelNine.setFont(fnt20P); //sets font of the button text
		b_LevelNine.setVisible(false);//button visible
		b_LevelNine.setBackground(white);//set background color
		b_LevelNine.setForeground(black);//set text color
		b_LevelNine.addActionListener(this);//makes button respond to clicks
		add(b_LevelNine);//adds button to applet
		
		b_LevelTen = new Button("START LEVEL TEN"); //construct the button
		b_LevelTen.setSize(250,50); //set size of button
		b_LevelTen.setLocation(WIDTH/2 - 125, HEIGHT/2 + 20);//set location
		b_LevelTen.setFont(fnt20P); //sets font of the button text
		b_LevelTen.setVisible(false);//button visible
		b_LevelTen.setBackground(white);//set background color
		b_LevelTen.setForeground(black);//set text color
		b_LevelTen.addActionListener(this);//makes button respond to clicks
		add(b_LevelTen);//adds button to applet
		
		
		b_Start = new Button("BEGIN YOUR JOURNEY"); //construct the button
		b_Start.setSize(230,50); //set size of button
		b_Start.setLocation(WIDTH/2 - 150, HEIGHT/2 + 40);//set location
		b_Start.setFont(fnt20P); //sets font of the button text
		b_Start.setVisible(true);//button visible
		b_Start.setBackground(white);//set background color
		b_Start.setForeground(black);//set text color
		b_Start.addActionListener(this);//makes button respond to clicks
		add(b_Start);//adds button to applet
		
		b_Close = new Button("Close"); //construct the button
		b_Close.setSize(70,50); //set size of button
		b_Close.setLocation(WIDTH - 100, 20);//set location
		b_Close.setFont(fnt16P); //sets font of the button text
		b_Close.setVisible(false);//button visible
		b_Close.setBackground(white);//set background color
		b_Close.setForeground(black);//set text color
		b_Close.addActionListener(this);//makes button respond to clicks
		add(b_Close);//adds button to applet
		
		//Image Icons are defined
		right = new ImageIcon(getClass().getResource("right.png"));
		left = new ImageIcon(getClass().getResource("left.png"));
		down = new ImageIcon(getClass().getResource("down.png"));
		up = new ImageIcon(getClass().getResource("up.png"));
		roid = new ImageIcon(getClass().getResource("roid.png"));
		mine = new ImageIcon(getClass().getResource("mine.png"));
		bullet = new ImageIcon(getClass().getResource("bullet.png"));
		explosion = new ImageIcon(getClass().getResource("explosion.gif"));
		bourne = new ImageIcon(getClass().getResource("bourne.png"));
		
		setProjInit(44);//initializes prjectiles
		timer = new Timer(15, new MyTimer());//set timer
	}//end init
	
	//Purpose: paints everything on applet
	public void paint(Graphics g)
	{	
		super.paint(g);
		
		//draws background objects (rocks)
		g.setColor(Color.gray);
		int[] xPoints = { 200, 210, 210, 205, 195, 193 };
		int[] yPoints = { 25, 30, 40, 45, 42, 35 };
		int points = 6;
		g.fillPolygon(xPoints, yPoints, points);
				
		int[] xPoints2 = { 500, 510, 505, 505, 500, 498 };
		int[] yPoints2 = { 125, 130, 140, 142, 142, 135 };
		int points2 = 6;
		g.fillPolygon(xPoints2, yPoints2, points2);
				
		int[] xPoints3 = { 50, 70, 60, 50, 45, 43 };
		int[] yPoints3 = { 425, 425, 440, 450, 442, 435 };
		int points3 = 6;
		g.fillPolygon(xPoints3, yPoints3, points3);
		
		int[] xPoints4 = { 590, 600, 600, 595, 585, 583 };
		int[] yPoints4 = { 525, 530, 540, 545, 542, 535 };
		int points4 = 6;
		g.fillPolygon(xPoints4, yPoints4, points4);
				
		int[] xPoints5 = { 290, 300, 300, 295, 290, 283 };
		int[] yPoints5 = { 225, 230, 240, 247, 242, 235 };
		int points5 = 6;
		g.fillPolygon(xPoints5, yPoints5, points5);

		int[] xPoints6 = { 320, 325, 330, 325, 315, 313 };
		int[] yPoints6 = { 475, 480, 485, 495, 492, 485 };
		int points6 = 6;
		g.fillPolygon(xPoints6, yPoints6, points6);
				
		int[] xPoints7 = { 200, 210, 205, 205, 195, 193 };
		int[] yPoints7 = { 325, 330, 340, 347, 342, 335 };
		int points7 = 6;
		g.fillPolygon(xPoints7, yPoints7, points7);
				
		int[] xPoints8 = { 525, 535, 535, 530, 525, 523 };
		int[] yPoints8 = { 325, 330, 340, 345, 342, 335 };
		int points8 = 6;
		g.fillPolygon(xPoints8, yPoints8, points8);
				
		int[] xPoints9 = { 380, 390, 395, 385, 375, 373 };
		int[] yPoints9 = { 100, 105, 115, 123, 117, 110 };
		int points9 = 6;
		g.fillPolygon(xPoints9, yPoints9, points9);
				
		int[] xPoints10 = { 100, 110, 105, 105, 95, 90 };
		int[] yPoints10 = { 175, 180, 190, 195, 190, 185 };
		int points10 = 6;
		g.fillPolygon(xPoints10, yPoints10, points10);
				
		int[] xPoints11 = { 175, 185, 180, 180, 170, 168 };
		int[] yPoints11 = { 550, 555, 565, 565, 557, 560 };
		int points11 = 6;
		g.fillPolygon(xPoints11, yPoints11, points11);
				
		int[] xPoints12 = { 475, 485, 480, 480, 470, 468 };
		int[] yPoints12 = { 450, 455, 465, 465, 457, 460 };
		int points12 = 6;
		g.fillPolygon(xPoints12, yPoints12, points12);
				
		int[] xPoints13 = { 425, 435, 430, 430, 420, 418 };
		int[] yPoints13 = { 200, 205, 215, 215, 207, 210 };
		int points13 = 6;
		g.fillPolygon(xPoints13, yPoints13, points13);
				
		int[] xPoints14 = { 825, 835, 830, 830, 820, 818 };
		int[] yPoints14 = { 200, 205, 215, 215, 207, 210 };
		int points14 = 6;
		g.fillPolygon(xPoints14, yPoints14, points14);
				
		int[] xPoints15 = { 375, 385, 380, 380, 370, 368 };
		int[] yPoints15 = { 650, 655, 665, 665, 657, 660 };
		int points15 = 6;
		g.fillPolygon(xPoints15, yPoints15, points15);
				
		int[] xPoints16 = { 800, 810, 810, 805, 795, 793 };
		int[] yPoints16 = { 625, 630, 640, 645, 642, 635 };
		int points16 = 6;
		g.fillPolygon(xPoints16, yPoints16, points16);
				
		int[] xPoints17 = { 100, 110, 110, 105, 95, 93 };
		int[] yPoints17 = { 625, 630, 640, 645, 642, 635 };
		int points17 = 6;
		g.fillPolygon(xPoints17, yPoints17, points17);
				
		int[] xPoints18 = { 975, 985, 980, 980, 970, 968 };
		int[] yPoints18 = { 650, 655, 665, 665, 657, 660 };
		int points18 = 6;
		g.fillPolygon(xPoints18, yPoints18, points18);
				
		int[] xPoints19 = { 1300, 1310, 1305, 1305, 1300, 1298 };
		int[] yPoints19 = { 325, 330, 340, 342, 342, 335 };
		int points19 = 6;
		g.fillPolygon(xPoints19, yPoints19, points19);
				
		int[] xPoints20 = { 1300, 1310, 1310, 1305, 1295, 1293 };
		int[] yPoints20 = { 25, 30, 40, 45, 42, 35 };
		int points20 = 6;
		g.fillPolygon(xPoints20, yPoints20, points20);
				
		int[] xPoints21 = { 800, 810, 810, 805, 795, 793 };
		int[] yPoints21 = { 25, 30, 40, 45, 42, 35 };
		int points21 = 6;
		g.fillPolygon(xPoints21, yPoints21, points21);
				
		int[] xPoints22 = { 1100, 1110, 1105, 1105, 1100, 1098 };
		int[] yPoints22 = { 125, 130, 140, 142, 142, 135 };
		int points22 = 6;
		g.fillPolygon(xPoints22, yPoints22, points22);
				
		int[] xPoints23 = { 650, 670, 660, 650, 645, 643 };
		int[] yPoints23 = { 425, 425, 440, 450, 442, 435 };
		int points23 = 6;
		g.fillPolygon(xPoints23, yPoints23, points23);
				
		int[] xPoints24 = { 1190, 1200, 1200, 1195, 1185, 1183 };
		int[] yPoints24 = { 525, 530, 540, 545, 542, 535 };
		int points24 = 6;
		g.fillPolygon(xPoints24, yPoints24, points24);
				
		int[] xPoints25 = { 890, 900, 900, 895, 890, 883 };
		int[] yPoints25 = { 225, 230, 240, 247, 242, 235 };
		int points25 = 6;
		g.fillPolygon(xPoints25, yPoints25, points25);

		int[] xPoints26 = { 920, 925, 930, 925, 915, 913 };
		int[] yPoints26 = { 475, 480, 485, 495, 492, 485 };
		int points26 = 6;
		g.fillPolygon(xPoints26, yPoints26, points26);
				
		int[] xPoints27 = { 800, 810, 805, 805, 795, 793 };
		int[] yPoints27 = { 325, 330, 340, 347, 342, 335 };
		int points27 = 6;
		g.fillPolygon(xPoints27, yPoints27, points27);
				
		int[] xPoints28 = { 1125, 1135, 1135, 1130, 1125, 1123 };
		int[] yPoints28 = { 325, 330, 340, 345, 342, 335 };
		int points28 = 6;
		g.fillPolygon(xPoints28, yPoints28, points28);
				
		int[] xPoints29 = { 980, 990, 995, 985, 975, 973 };
		int[] yPoints29 = { 100, 105, 115, 123, 117, 110 };
		int points29 = 6;
		g.fillPolygon(xPoints29, yPoints29, points29);
				
		int[] xPoints30 = { 700, 710, 705, 705, 695, 690 };
		int[] yPoints30 = { 175, 180, 190, 195, 190, 185 };
		int points30 = 6;
		g.fillPolygon(xPoints30, yPoints30, points30);
		
		if (currentLevel == 0)
		{
				g.drawImage(bourne.getImage(), 550, 150, this);
			
				g.setColor(Color.black);
				g.setFont(fnt36P);
				g.drawString("Instructions:",20,150);
				g.setFont(fnt24P);
				g.drawString("Use the W A S D keys to maneuver",20,200);
				g.drawString("the hero away from the flying rocks",20,225);
				g.drawString("while using the mouse to shoot and",20,250);
				g.drawString("destroy them (use your bullets wisely",20,275);
				g.drawString("however because they are limited).",20,300);
				g.drawString("Pressing the space bar will also drop",20,325);
				g.drawString("a mine that will destroy any rock that",20,350);
				g.drawString("runs into it. If you are able to last 60",20,375);
				g.drawString("seconds or destroy all of the rocks then",20,400);
				g.drawString("you will move on to the next level.",20,425);
				g.drawString("You will recieve 1 point for every tenth",20,500);
				g.drawString("of a second you stay alive, 100 points for",20,525);
				g.drawString("every rock you destroy with a bullet and",20,550);
				g.drawString("50 with a mine and 20 points for every ",20,575);
				g.drawString("bullet and mine remaining after that level.",20,600);
				g.drawString("Goodluck!",150,625);
		}
		
		graphBullet(g);//graphs bullets
		graphMine(g);//graphs mines
		
		//graphs image of character according to his direction of movement
		if (moveUp == true) g.drawImage(up.getImage(), p1.x - 8, p1.y - 8, this);
		if (moveDown == true) g.drawImage(down.getImage(), p1.x - 10, p1.y - 8, this);
		if (moveLeft == true) g.drawImage(left.getImage(), p1.x - 8, p1.y - 8, this);
		if (moveRight == true) g.drawImage(right.getImage(), p1.x - 10, p1.y - 8, this);
		
		charExplosion(g);//creates explosion when character dies
		bulletExplosion(g);//creates explosion when bullet and projectile collide
		mineExplosion(g);//creates explosion when mine and projectile collide
		
		//paints the projectiles according to the level
		g.setColor(Color.black);
		if (currentLevel == 1) setProjPaint(levelOnePro, g);
		else if (currentLevel == 2) setProjPaint(levelTwoPro, g);
		else if (currentLevel == 3) setProjPaint(levelThreePro, g);
		else if (currentLevel == 4) setProjPaint(levelFourPro, g);
		else if (currentLevel == 5) setProjPaint(levelFivePro, g);
		else if (currentLevel == 6) setProjPaint(levelSixPro, g);
		else if (currentLevel == 7) setProjPaint(levelSevenPro, g);
		else if (currentLevel == 8) setProjPaint(levelEightPro, g);
		else if (currentLevel == 9) setProjPaint(levelNinePro, g);
		else if (currentLevel == 10) setProjPaint(levelTenPro, g);
		
		//Displays time
		g.setFont(fnt24P);
		if (min != 1) g.drawString("Time", WIDTH - 100, HEIGHT - 75);
		if (min != 1) g.drawString(min + ":" + sec + "." + tenSec, WIDTH - 100, HEIGHT - 50);
		
		g.setFont(fnt60P);
		
		//Displays text if character dies
		if (stillAlive == false && min != 1 && numProAlive !=0) g.drawString("YOU LOSE", WIDTH/2 - (5*f48Char), HEIGHT/2);//if game ends
		
		/*The following Strings are printed to the screen at the end of each level. The level can end
		 * either because time has reached 60 seconds or the player has destroyed all projectiles.
		 * Also displays the number of remaining mines and bullets and awards points for those.
		 */
		else if ((stillAlive == false && currentLevel == 2 && min == 1)||(currentLevel == 1 && numProAlive == 0 && stillAlive == false)) 
		{
				g.drawString("LEVEL ONE COMPLETE", WIDTH/2 - (9*f48Char),HEIGHT/2);
				g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 300, 600);
				g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 300, 650);
		}
		
		else if ((stillAlive == false && currentLevel == 3 && min == 1)||(currentLevel == 2 && numProAlive == 0))
		{
				g.drawString("LEVEL TWO COMPLETE", WIDTH/2 - (9*f48Char),HEIGHT/2);
				g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 150, 600);
				g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 150, 620);
		}
		
		else if ((stillAlive == false && currentLevel == 4 && min == 1)||(currentLevel == 3 && numProAlive == 0))
		{
			g.drawString("LEVEL THREE COMPLETE", WIDTH/2 - (9*f48Char),HEIGHT/2);
			g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 150, 600);
			g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 150, 620);
		}
		
		else if ((stillAlive == false && currentLevel == 5 && min == 1)||(currentLevel == 4 && numProAlive == 0))
		{
			g.drawString("LEVEL FOUR COMPLETE", WIDTH/2 - (10*f48Char),HEIGHT/2);
			g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 150, 600);
			g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 150, 620);
		}
		
		else if ((stillAlive == false && currentLevel == 6 && min == 1)||(currentLevel == 5 && numProAlive == 0))
			{
			g.drawString("LEVEL FIVE COMPLETE", WIDTH/2 - (10*f48Char),HEIGHT/2);
			g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 150, 600);
			g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 150, 620);
			}
		
		else if ((stillAlive == false && currentLevel == 7 && min == 1)||(currentLevel == 6 && numProAlive == 0)){
			{
				g.drawString("LEVEL SIX COMPLETE", WIDTH/2 - (9*f48Char),HEIGHT/2);
				g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 150, 600);
				g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 150, 620);
			}
		}
		
		else if ((stillAlive == false && currentLevel == 8 && min == 1)||(currentLevel == 7 && numProAlive == 0))
		{
			g.drawString("LEVEL SEVEN COMPLETE", WIDTH/2 - (11*f48Char),HEIGHT/2);
			g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 150, 600);
			g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 150, 620);
		}
		
		else if ((stillAlive == false && currentLevel == 9 && min == 1)||(currentLevel == 8 && numProAlive == 0))
			{
			g.drawString("LEVEL EIGHT COMPLETE", WIDTH/2 - (11*f48Char),HEIGHT/2);
			g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 150, 600);
			g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 150, 620);
			}
		
		else if ((stillAlive == false && currentLevel == 10 && min == 1)||(currentLevel == 9 && numProAlive == 0))
			{
				g.drawString("LEVEL NINE COMPLETE", WIDTH/2 - (10*f48Char),HEIGHT/2);
				g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 150, 600);
				g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 150, 620);
			}
		
		else if ((stillAlive == false && currentLevel == 11 && min == 1)||(currentLevel == 10 && numProAlive == 0))
			{
				g.drawString("YOU WIN", WIDTH/2 - (9*f48Char),HEIGHT/2);
				g.drawString(currentAmmo + "Bullets left =" + (currentAmmo*remainingAmmo) + " points", 150, 600);
				g.drawString(currentMine + "Mines left =" + (currentMine*remainingAmmo) + " points", 150, 700);
			}
		
		g.setFont(fnt24P);
		g.drawString("Current Level: " + currentLevel, 700, 30);//displays current level
		if (min != 1) g.drawString("Number of Projectiles: " + numProAlive, 70, 30);//displays number of projectiles
		g.drawString("Ammunition Remaining: " + currentAmmo, 350, 60);//displays ammunition remaining
		g.drawString("Mines Remaining:  " + currentMine, 350, 30);//displays mines remaining
		g.drawString("Score: " + score, 700, 60);//displays score
		g.drawLine(0,80, WIDTH, 80);
		g.setColor(Color.black);
	}//end paint

	//Purpose: respond to key events
	//purpose: move character as wasd keys are pressed
	public void keyPressed(KeyEvent e)
	{	
		if (stillAlive == true && timerOn == true)//if the character is still alive and the timer is on
		{	
				if (e.getKeyCode() == KeyEvent.VK_W)
				{
					p1.y-=jump;//moves character in the -y direction
					moveUp = true;//changes character icon to the upward facing character
					moveDown = false;//sets all others false
					moveLeft = false;
					moveRight = false;
				}
		
				else if (e.getKeyCode() == KeyEvent.VK_A)
				{
					p1.x-=jump;//moves character in -x direction
					moveUp = false;
					moveDown = false;
					moveLeft = true;//leftward facing character is set to true while all others are false
					moveRight = false;
				}
		
				else if (e.getKeyCode() == KeyEvent.VK_S)
				{
					p1.y+=jump;//moves character in the positive y direction
					moveUp = false;
					moveDown = true;//downward facing character is set to true while all others are set to false
					moveLeft = false;
					moveRight = false;
				}
		
				else if (e.getKeyCode() == KeyEvent.VK_D)
				{
					p1.x+=jump;//moves character in the positive x direction
					moveUp = false;
					moveDown = false;
					moveLeft = false;
					moveRight = true;//rightward facing character is set to true while all others are set to false
				}
				
				else if (e.getKeyCode() == KeyEvent.VK_SPACE && currentMine > 0)//if number of mines in possesion is greater than zero
				{
					mineArr[numMine] = new Mine(p1.x + charDiameter/4, p1.y + charDiameter/4);//drops mine at the location of the character
					mineExist[numMine] = true;//sets mine exist variable to true
					numMine++;//numMine incremented (used to keep track of the mines)
					currentMine--;//current number of mines in posession is decreased by one
				}
				
		}	
			//used to pause the game
			if (e.getKeyCode() == KeyEvent.VK_P && timerOn == true)//if timer is one
			{
				timer.stop();//stops timer
				timerOn = false;//timer boolean set to false
				b_Close.setVisible(true);//close game button is visible visible
			}
			
			//if p is pressed while the game is paused, the game will continue where it left off
			else if (e.getKeyCode() == KeyEvent.VK_P && timerOn == false)
			{
				timer.start();
				timerOn = true;
				b_Close.setVisible(false);//button visible
			}
			requestFocus();//return focus to applet
			repaint();//repaint the applet
	}//end keypressed
	
	public void keyTyped(KeyEvent e) {}//end keytypeds
	public void keyReleased(KeyEvent e)  {}//end keyreleased
	
	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	//Purpose: respond to mouse events
	public void mouseClicked(MouseEvent e) 
	{	
		if (stillAlive == true && timerOn == true)//checks if the character is still alive and the timer is on
		{
			if (currentAmmo > 0)//checks if the player has remaining ammunition
			{	
				changeX[numBull] = (e.getX()*1.0 - (1.0*p1.x + charDiameter/4));//records the change in x from the click point to the location of the character
				changeY[numBull] = (e.getY()*1.0 - (1.0*p1.y + charDiameter/4));//records change in y from click point to the character
				angle[numBull] = Math.atan2(changeY[numBull],changeX[numBull]);//defines an angle based on the change in x and change in y
				bullArr[numBull] = new Bullet(p1.x + charDiameter/2, p1.y + charDiameter/2, angle[numBull], 10 );//defines a new bullet object at the location of the character with the angle for bullet motion
				bullExist[numBull] = true;//bullet existance variable set to true
				
				gunshot.play();
				
				currentAmmo--;//current bullets in possesion decreases by one
				numBull++;//numBull increased (used to keep track of bullets) increment allows for definition of new bullet on next click
			}	
		}
	}

	//unimplemented methods
	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	//Purpose: respond to action
	public void actionPerformed(ActionEvent e) 
	{
		//all PlayAgain ActionEvents are the same. All that changes is the level
		if (e.getSource() == b_PlayAgain)
		{
			b_PlayAgain.setVisible(false);//button invisible
			if (currentLevel == 1)//checks to see that the current level is one
			{
				resetComplete(levelOnePro);//resets all projectiles
				resetAll(levelOnePro);//resets variables
				currentAmmo = levelOneAmmo;//rests ammunition
				currentMine = levelOneMine;//resets mines
				offScreen = false;//sets offscreen variable to false
				score-=scoreLoss;//decrements score by 500
			}
			else if (currentLevel == 2)
			{
				resetComplete(levelTwoPro);
				resetAll(levelTwoPro);
				currentAmmo = levelTwoAmmo;
				currentMine = levelTwoMine;
				offScreen = false;
				score-=scoreLoss;
			}
			else if (currentLevel == 3)
			{
				resetComplete(levelThreePro);
				resetAll(levelThreePro);
				currentAmmo = levelThreeAmmo;
				currentMine = levelThreeMine;
				offScreen = false;
				score-=scoreLoss;
			}
			else if (currentLevel == 4)
			{
				resetComplete(levelFourPro);
				resetAll(levelFourPro);
				currentAmmo = levelFourAmmo;
				currentMine = levelFourMine;
				offScreen = false;
				score-=scoreLoss;
			}
			else if (currentLevel == 5)
			{
				resetComplete(levelFivePro);
				resetAll(levelFivePro);
				currentAmmo = levelFiveAmmo;
				currentMine = levelFiveMine;
				offScreen = false;
				score-=scoreLoss;
			}
			else if (currentLevel == 6)
			{
				resetComplete(levelSixPro);
				resetAll(levelSixPro);
				currentAmmo = levelSixAmmo;
				currentMine = levelSixMine;
				offScreen = false;
				score-=scoreLoss;
			}
			else if (currentLevel == 7)
			{
				resetComplete(levelSevenPro);
				resetAll(levelSevenPro);
				currentAmmo = levelSevenAmmo;
				currentMine = levelSevenMine;
				offScreen = false;
				score-=scoreLoss;
			}
			else if (currentLevel == 8)
			{
				resetComplete(levelEightPro);
				resetAll(levelEightPro);
				currentAmmo = levelEightAmmo;
				currentMine = levelEightMine;
				offScreen = false;
				score-=scoreLoss;
			}
			else if (currentLevel == 9)
			{
				resetComplete(levelNinePro);
				resetAll(levelNinePro);
				currentAmmo = levelNineAmmo;
				currentMine = levelNineMine;
				offScreen = false;
				score-=scoreLoss;
			}
			else if (currentLevel == 10)
			{
				resetComplete(levelTenPro);
				resetAll(levelTenPro);
				currentAmmo = levelTenAmmo;
				currentMine = levelTenMine;
				offScreen = false;
				score-=scoreLoss;
			}
		}
		
		//All Level Action events do same thing, changes from one level to next when the level is beaten
		if (e.getSource() == b_LevelTwo)//starts level two
		{
			b_LevelTwo.setVisible(false);//sets button invisible
			resetComplete(levelTwoPro);//resets all projectiles
			resetAll(levelTwoPro);//resets all variables
			if (numProAlive == 0) currentLevel++;//increases the level by one
			
			score = score + (currentAmmo*remainingAmmo);//adds score for leftover ammo
			score = score + (currentMine*remainingAmmo);//adds score for leftover mines
			
			currentAmmo = levelTwoAmmo;//sets new ammo amount
			currentMine = levelTwoMine;//sets new mine amount
		}
		
		if (e.getSource() == b_LevelThree)
		{
			b_LevelThree.setVisible(false);
			resetComplete(levelThreePro);
			resetAll(levelThreePro);
			if (numProAlive == 0) currentLevel++;
			
			score = score + (currentAmmo*remainingAmmo);
			score = score + (currentMine*remainingAmmo);
			
			currentAmmo = levelThreeAmmo;
			currentMine = levelThreeMine;
		}
		
		if (e.getSource() == b_LevelFour)
		{
			b_LevelFour.setVisible(false);
			resetComplete(levelFourPro);
			resetAll(levelFourPro);
			if (numProAlive == 0) currentLevel++;
			
			score = score + (currentAmmo*remainingAmmo);
			score = score + (currentMine*remainingAmmo);
			
			currentAmmo = levelFourAmmo;
			currentMine = levelFourMine;
		}
		
		if (e.getSource() == b_LevelFive)
		{
			b_LevelFive.setVisible(false);
			resetComplete(levelFivePro);
			resetAll(levelFivePro);
			if (numProAlive == 0) currentLevel++;
			
			score = score + (currentAmmo*remainingAmmo);
			score = score + (currentMine*remainingAmmo);
			
			currentAmmo = levelFiveAmmo;
			currentMine = levelFiveMine;
		}
		
		if (e.getSource() == b_LevelSix)
		{
			b_LevelSix.setVisible(false);
			resetComplete(levelSixPro);
			resetAll(levelSixPro);
			if (numProAlive == 0) currentLevel++;
			
			score = score + (currentAmmo*remainingAmmo);
			score = score + (currentMine*remainingAmmo);
			
			currentAmmo = levelSixAmmo;
			currentMine = levelSixMine;
		}
		
		if (e.getSource() == b_LevelSeven)
		{
			b_LevelSeven.setVisible(false);
			resetComplete(levelSevenPro);
			resetAll(levelSevenPro);
			if (numProAlive == 0) currentLevel++;
			
			score = score + (currentAmmo*remainingAmmo);
			score = score + (currentMine*remainingAmmo);
			
			currentAmmo = levelSevenAmmo;
			currentMine = levelSevenMine;
		}
		
		if (e.getSource() == b_LevelEight)
		{
			b_LevelEight.setVisible(false);
			resetComplete(levelEightPro);
			resetAll(levelEightPro);
			if (numProAlive == 0) currentLevel++;
			
			score = score + (currentAmmo*remainingAmmo);
			score = score + (currentMine*remainingAmmo);
			
			currentAmmo = levelEightAmmo;
			currentMine = levelEightMine;
		}
		
		if (e.getSource() == b_LevelNine)
		{
			b_LevelNine.setVisible(false);
			resetComplete(levelNinePro);
			resetAll(levelNinePro);
			if (numProAlive == 0) currentLevel++;
			
			score = score + (currentAmmo*remainingAmmo);
			score = score + (currentMine*remainingAmmo);
			
			currentAmmo = levelNineAmmo;
			currentMine = levelNineMine;
		}
		
		if (e.getSource() == b_LevelTen)
		{
			b_LevelTen.setVisible(false);
			resetComplete(levelTenPro);
			resetAll(levelTenPro);
			if (numProAlive == 0) currentLevel++;
			
			score = score + (currentAmmo*remainingAmmo);
			score = score + (currentMine*remainingAmmo);
			
			currentAmmo = levelTenAmmo;
			currentMine = levelTenMine;
		}
		
		//Stars the game at the beginning
		if (e.getSource() == b_Start)
		{
			timer.start();//stars timer
			timerOn = true;//sets timer variable to true
			b_Start.setVisible(false);//button visible
			currentLevel = 1;//sets current level to equal one
			p1.x = WIDTH/2;//puts character in the middle of the screen
			p1.y = HEIGHT/2;
		}
		
		//closes the game
		if (e.getSource() == b_Close)
		{
			System.exit(0);
		}
	}

	//PROJECTILE METHODS*******************************************************************************
	
	/*Purpose: The following four methods are used to reset the projectiles after they leave the screen
	 * and repaints the on the opposite to move again
	 * Parameters: p is the projectile you wish to reset
	 */
	//PROJECTILE METHODS
	//****************************************************************************************************************************
	
	//resets and replaces projectile coming from top when they leave the screen
	public void resetU (Projectile p)
	{
		if (p.locY >= (HEIGHT + 25))//checks if the projectile has gone off the bottom of the screen
		{
			p.locX = ranNum(1,WIDTH);//resets it at a random X location
			p.locY = -25;//starts it above the top border
		}
	}//end resetu
	
	//resets and replaces projectiles coming from the bottom when they leave the screen
	public void resetD (Projectile p)
	{
		if (p.locY <= -25)//checks if the projectile has one off the top of the screen
		{
			p.locX = ranNum(1,WIDTH);//assines X location to random number
			p.locY = HEIGHT + 25;//resets Y location to the bottom of the screen
		}
	}//end reset d
	
	//resets and replaces projectiles coming from left when they leave the screen
	public void resetL (Projectile p)
	{
		if (p.locX >= (WIDTH +25))//checks if the projectile has gone off the right side of the scren 
		{
			p.locX = -25;//sets X location to the left side of the screen
			p.locY = ranNum(1,HEIGHT);//generates random Y value
		}
	}//end resetl
	
	//resets and replaces projectiles coming from left when they leave the screen
	public void resetR (Projectile p)
	{
		if (p.locX <= -25)//checks if the projectile has gone off the left side of the screen
		{
			p.locX = WIDTH +25;//sets X location to the right side of the screen
			p.locY = ranNum(1,HEIGHT);//generates random Y value
		}
	}//end reset r
	
	/*Purpose: The following 4 methods are used to reset the the projectiles regardless of location
	 * This is used in gameOver() or in level up methods
	 * Parameters: p is the projectile you wish to rest
	 */
	//The following methods are the exact same as the methods above except these are used to reset
	//projectiles after each level or when game is over. Resets regardles off projectile location
	
	//resets and replaces projectile coming from top regardless of location
	public void resetUComp (Projectile p)
		{
			p.locX = ranNum(1,WIDTH);
			p.locY = -25;
		}//end resetu
	
	//resets and replaces projectiles coming from the bottom regardless of location
	public void resetDComp (Projectile p)
		{
			p.locX = ranNum(1,WIDTH);
			p.locY = HEIGHT + 25;
		}//end reset d
		
	//resets and replaces projectiles coming from left regardless of location
	public void resetLComp (Projectile p)
		{
			p.locX = -25;
			p.locY = ranNum(1,HEIGHT);
		}//end resetl
		
	//resets and replaces projectiles coming from left regardless of location
	public void resetRComp (Projectile p)
		{
			p.locX = WIDTH +25;
			p.locY = ranNum(1,HEIGHT);
		}//end reset r
	
	//Purpose: Degines the projectile arrays, shoots an equal number from each side
	//Parameter: num is the number of projectiles you wish to be defined
	public void setProjInit(int num)
		{
			for (int i = 0; i < num; i++)
			{
				if (i % 4 == 0)//these will create projectiles that shoot from top 
				{
					proArr[i] = new Projectile(ranNum(1, WIDTH), -25, ranNum(10, 20), 1, 1, ranNum(1, 200), true);//defines projectiles
				}
				
				else if (i % 4 == 1)//these will create projectiles that shoot from bottom
				{
					proArr[i] = new Projectile(ranNum(1, WIDTH), HEIGHT + 40,ranNum(10, 20), 1, -1, ranNum(1, 200), true );
				}
				
				else if (i % 4 == 2)//these will create projectiles that shoot from left
				{
					proArr[i] = new Projectile(-25, ranNum(1, HEIGHT), ranNum(10, 20), 1, 1, ranNum(1, 200), true);
				}
				
				else if (i % 4 == 3)//these will shoot projectiles that shoot from left
				{
					proArr[i] = new Projectile(WIDTH + 40, ranNum(1, HEIGHT), ranNum(10, 20), -1, 1, ranNum(1, 200), true);
				}
			}//end for
		}//end setprojinit

	//Purpose: paint projectiles on the screen
	//Parameters: num is the number of bullets you are painting
	public void setProjPaint(int num, Graphics g)
		{
			for (int i = 0; i < num; i++)
			{
				//checks if the projectile is on, then draws the astroid image onto that location
				if (proArr[i].proOn == true) g.drawImage(roid.getImage(), proArr[i].locX, proArr[i].locY, proArr[i].diameter, proArr[i].diameter, this);
			}
		}//end set projpaint
	
	//Purpose: Move the projectiles
	//Parameters: num is the number of projectiles you wish to move
	public void setProjAction (int num)
		{
			for (int i = 0; i < num; i++)
			{
				if (proArr[i].proOn == true)//checks if projectile is on, or availible to move
				{
					if (i % 4 == 0 && count >= proArr[i].proCanGo) proArr[i].MoveY();//moves from top to bottom
					if (i % 4 == 1 && count >= proArr[i].proCanGo) proArr[i].MoveY();//moves from bottom to top
					if (i % 4 == 2 && count >= proArr[i].proCanGo) proArr[i].MoveX();//moves from left to right
					if (i % 4 == 3 && count >= proArr[i].proCanGo) proArr[i].MoveX();//moves from right to left
				}
			}
		}//end set proj action

	//purpose: resets projectiles to the other side during game when they leave the screen
	//Parameters: num is the number of projectiles you wish to reset
	public void resetAllPro (int num)
	{
		for (int i = 0; i < num; i++)
		{
			if (i % 4 == 0)//these will create projectiles that shoot from top
			{
				resetU(proArr[i]);//resets projectiles that move from top to bottom
			}
			
			else if (i % 4 == 1)//these will create projectiles that shoot from bottom
			{
				resetD(proArr[i]);//resets projectile shat move bottom to top
			}
			
			else if (i % 4 == 2)//these will create projectiles that shoot from left
			{
				resetL(proArr[i]);//resets projectile that move left to right
			}
			
			else if (i % 4 == 3)//these will shoot projectiles that shoot from left
			{
				resetR(proArr[i]);//resets projectile that move right to left
			}
		}//end for
	}//end reset all

	//Purpose: reassignes all projectile locations to off screen (used when game is over or when a level is won)\
	//Parameters: num is the number of projectiles you wish to reset
	public void resetComplete(int num)
	{
		for (int i = 0; i < num; i++)
		{
			if (i % 4 == 0)//these will create projectiles that shoot from top
			{
				resetUComp(proArr[i]);
			}
			
			else if (i % 4 == 1)//these will create projectiles that shoot from bottom
			{
				resetDComp(proArr[i]);
			}
			
			else if (i % 4 == 2)//these will create projectiles that shoot from left
			{
				resetLComp(proArr[i]);
			}
			
			else if (i % 4 == 3)//these will shoot projectiles that shoot from left
			{
				resetRComp(proArr[i]);
			}
		}//end for
	}

	//Purpose: stops movement if character is hit by projectile
	//Parameters: num is the number of projectiles to check against the location of the character
	public void gameOver(int num)
	{
			for (int i = 0; i < num; i++)
			{
				if (proArr[i].proOn == true)//checks if the projectile is true
				{	
					//checks for collision between the character and projectile using distance formula
					//also checks to see if the character has moved off screen
					if (Math.sqrt(Math.pow((p1.x + charDiameter/2) - (proArr[i].locX + proArr[i].diameter/2), 2) + Math.pow((p1.y + charDiameter/2) - (proArr[i].locY + proArr[i].diameter/2), 2)) <= ((charDiameter / 2)+(proArr[i].diameter / 2)) || offScreen == true)
					{
						boom.play();
						
						charBoom = true;//creates explosion at the location of the character
						stillAlive = false;
						b_PlayAgain.setVisible(true);//Play again button visible
						timer.stop();// timer is stopped
					}
				}
			}	
	}//end game over
	
	//Purpose: Count the number of projectiles that are being graphed on the screen
	public void countPro()
	{
		int numPro = 0;
		for (int j = 0; j <= 9; j++ )
		{
			if (currentLevel == (j+1))//checks for current level
			{
				for (int i = 0; i < (8 + 4*j); i++)//used because each level goes up by 4 projectiles
				{
					if (proArr[i].proOn == true)//checks to see if projectiles being checked are on
					{
						numPro++;//if they are on the number of projectiles in incremented by one
					}
				}//end inner for
			}
		}//end outer for
		numProAlive = numPro;//numProAlive is set equal to numPro after the method is run. 
	}
	
	//BULLET METHODS***********************************************************************************
	
	//BULLET METHODS
	//***********************************************************************************************************************************************************

	//Purpose: Graphs the bullet on screen
	public void graphBullet(Graphics g)
	{
		for (int i=0; i < maxBull; i++)
		{
			//if the bullet exists and it is on the visible screen then the bullet image is painted
			if (bullExist[i] == true && bullArr[i].locX >= 0 && bullArr[i].locY >= 0) g.drawImage(bullet.getImage(), (int)bullArr[i].locX, (int)bullArr[i].locY,bullArr[i].diameter,bullArr[i].diameter,this);
		}
	}

	//Purpose: move the bullet
	public void moveBullet()
	{
		for (int i=0; i < maxBull; i++)
		{
			//checks if bullet is true and bullet is on visible scrren
			if (bullExist[i] == true && bullArr[i].locX >= 0 && bullArr[i].locY >= 0)
			{	
				bullArr[i].MoveX();//if so then bullets are moved
				bullArr[i].MoveY();
			}
		}
	}

	//Purpose: reset bullets after they go off the screen
	public void resetBullet()
	{
		for (int j = 0; j < numBull; j++)
		{
			//checks to see that the bullet exists
			if (bullExist[j] == true)
			{	
				//if the bullet is off the visible screen it is set to false
				if (bullArr[j].locX > WIDTH || bullArr[j].locX < 0) bullExist[j] = false;
				if (bullArr[j].locY > HEIGHT || bullArr[j].locY < 0) bullExist[j] = false;
			}
		}
		if (numBull == maxBull) numBull = 0;//resets numBull to zero if it reaches maxBull (numBull is used to keep track of bullets when defined)
	}

	//Purpose: Checks to see if the bullet has collided with a projectile
	//Parameters: numPro is the number of projectiles you wish to check against
	public void bullCollision(int numPro)
	{
		for (int i = 0; i < maxBull; i++)//checks every bullet
		{
			if (bullExist[i] == true)//if bullet being checked exists
			{	
				for (int j = 0; j < numPro; j++)//checks every projectiles
				{	
					if (proArr[j].proOn == true)//if the projectile is on
					{
						//distance formula is used to check to see if the bullet collides with the projectiles
						if (Math.sqrt(Math.pow((bullArr[i].locX + bullArr[i].diameter/2) - (proArr[j].locX + proArr[j].diameter/2), 2) + Math.pow((bullArr[i].locY + bullArr[i].diameter/2) - (proArr[j].locY + proArr[j].diameter/2), 2)) <= ((bullArr[i].diameter / 2)+(proArr[j].diameter / 2)))
						{	
							//sets location for explosion and sets expolsion variable to true
							bullExLoc.x = proArr[j].locX;
							bullExLoc.y = proArr[j].locY;
							bulletBoom = true;
							
							boom.play();
						
							//sets projectile and bullet to false
							proArr[j].proOn = false;
							bullExist[i] = false;
							bullArr[i].locX = -10;//moves bullet location off screen
							bullArr[i].locY = -10;
							
							score+=hitProjBull;//adds 100 points to the score
						}	
					}
				}//end for
			}
		}//end for	
	}//end collision
	
	//MINE METHODS********************************************************************************************
	
	//MINE METHODS*******************************************************************************************************
	
	//Purpose: Graphs the mines on the screen
	public void graphMine(Graphics g)
	{
		for (int i=0; i < maxMine; i++)
		{
			//if mine exists mine image is painted on the screen
			if (mineExist[i] == true) g.drawImage(mine.getImage(), mineArr[i].locX, mineArr[i].locY, mineArr[i].diameter, mineArr[i].diameter, this);
		}
	}

	//Purpose: checks to see if mine collides with projectile (exact same as bullet collision methods)\
	//Parameters: number of projectiles you are comparing against
	public void mineCollision(int numPro)
	{
		for (int i = 0; i < maxMine; i++)//checks all mines
		{
			if (mineExist[i] == true)//checks to see that the mine being checked exists
			{	
				for (int j = 0; j < numPro; j++)//checks all projectiles
				{	
					if (proArr[j].proOn == true)//checks if projectile is on
					{
						//uses distance formula to check for collision between mine and projectile
						if (Math.sqrt(Math.pow((mineArr[i].locX + mineArr[i].diameter/2) - (proArr[j].locX + proArr[j].diameter/2), 2) + Math.pow((mineArr[i].locY + mineArr[i].diameter/2) - (proArr[j].locY + proArr[j].diameter/2), 2)) <= ((mineArr[i].diameter / 2)+(proArr[j].diameter / 2)))
						{
							//location for expolosion set and explosion boolean set to true
							mineExLoc.x = proArr[j].locX;
							mineExLoc.y = proArr[j].locY;
							mineBoom = true;
							
							boom.play();
						
							proArr[j].proOn = false;//projectile set false
							mineExist[i] = false;//mine set to false
							mineArr[i].locX = -10;//mine location moved off screen
							mineArr[i].locY = -10;
							
							score+=hitProjMine;//score incremented by 100
						}	
					}
				}//end for
			}
		}//end for
	}//end mineCollision
	
	public void resetMine()
	{
		if (numMine == maxMine) numMine = 0;
	}
	
	//OTHER METHODS***********************************************************************************************
	
	//OTHER METHODS
	//***********************************************************************************************************************************************************************
	
	//Purpose: generates random numbers between two values
	//Parameters: min is minimum number you want and max is maximum number you want
	//Postcondition: returns the random number
 	public int ranNum (int min, int max)
	{
		int ranNum = (int)(min + max * Math.random());//random number generate
		return ranNum;
	}//end ranNum

	//Purpose: keeps track of time on applet
 	//Values based off of timer
	public void time()
	{
		//count is variable that increments in the timer
		if (count%5 == 0) 
		{
			tenSec++;//adds one tenth of a second
			score++;//increments score by one
		}
		if (count%50 == 0) sec++;//adds seconds 
		if (sec == 60) min++;//adds minute when second reaches 60
		if (tenSec == 9) tenSec = 0;//resets tensec to zero when it reaches nine
		if (sec == 60) sec = 0;//resets seconds back to zero
	}//end time

	//Purpose: reset all variables and objects for the start of a new level
	//Parameters: number of projectiles in the level
	public void resetAll(int numPro)
	{
		//sets character to middle of the screen
		p1.x = WIDTH/2;
		p1.y = HEIGHT/2;
		
		stillAlive = true;//character is alive
		
		for (int i = 0; i < numPro; i++) proArr[i].proOn = true;//sets all projectiles to true
		for (int j = 0; j < maxBull; j++) bullExist[j] = false;//all bullets do not exist
		for (int k = 0; k < maxMine; k++) mineExist[k] = false;//all mines do not exist
		
		numBull = 0;
		
		//time variables set to zero
		count = 0;
		tenSec = 0;
		sec = 0;
		min = 0;
		
		//explosion counts for character, bullet, and mine set to zero
		charExCount = 0;
		bullExCount = 0;
		mineExCount = 0;
		
		timer.restart();//timer starts once again
	}//end resetAll
	
	//Purpose: creates explosion animation when character dies
	public void charExplosion(Graphics g)
	{
		if (charBoom == true) //checks if boom is supposed to happen
		{
			g.drawImage(explosion.getImage(), p1.x - 25, p1.y - 25, this);//draws explosion at the location of the character
			charExCount ++;//increments
			if (charExCount == 25) //once value reaches 20
			{
				charBoom = false;//explosions stop
				p1.x = -100;//character is moved offscreen
				p1.y = -100;
			}
		}
	}//end charexplosion
	
	//Purpose: creates explosion when bullet is destroyed
	//same as charExplosion method
	public void bulletExplosion(Graphics g)
	{
		if (bulletBoom == true)
		{	
			g.drawImage(explosion.getImage(), bullExLoc.x - 25, bullExLoc.y - 25, this);
			bullExCount ++;
			
			if (bullExCount == 30)
			{
				bullExCount = 0;
				bulletBoom = false;
			}
		}
	}
	
	//Purpose: creates explosion animation for mine
	//Same as charExplosion method
	public void mineExplosion(Graphics g)
	{
		if (mineBoom == true)
		{
			g.drawImage(explosion.getImage(), mineExLoc.x - 25, mineExLoc.y - 25, this);
			mineExCount ++;
			
			if (mineExCount == 30)
			{
				mineExCount = 0;
				mineBoom = false;
			}
		}
	}//end mineExplosion

	//Purpose: checks to see if character moves off screen
	public void checkOffScreen()
	{
		//checks if character moves off top, bottom, left, right
		if ((p1.x - charDiameter/2) <= 0 || (p1.x + charDiameter/2) >= WIDTH || (p1.y - charDiameter/2) <= 0 || (p1.y + charDiameter/2) >= HEIGHT ) 
			{
				stillAlive = false;//no longer alive
				offScreen = true;
			}
	}//end checkOffScreen
	
	
	//LEVEL METHODS **************************************************************************************************************************
	/*
	 * Each of the following methods does the exact same thing. It checks to see if time has reached 1 minute of if all projectiles
	 * have been destroyed. In both cases the level is complete. When the level is complete stillAlive is set to false to stop functions,
	 * the timer is stopped, and the "start next level button is displayed on screen"
	 * 
	 * Retyping that for every method would have been repetative
	 */
	
	public void levelOneComp()
	{
		if ((min == 1 && currentLevel == 2)||(numProAlive == 0 && currentLevel == 1))
		{
			stillAlive = false;
			timer.stop();
			b_LevelTwo.setVisible(true);//button visible
		}
	}
	
	public void levelTwoComp()
	{
		if ((min == 1 && currentLevel == 3)||(numProAlive == 0 && currentLevel == 2))
		{
			stillAlive = false;
			timer.stop();
			b_LevelThree.setVisible(true);//button visible
		}
	}
	
	public void levelThreeComp()
	{
		if ((min == 1 && currentLevel == 4)||(numProAlive == 0 && currentLevel == 3))
		{
			stillAlive = false;
			timer.stop();
			b_LevelFour.setVisible(true);//button visible
		}
	}
	
	public void levelFourComp()
	{
		if ((min == 1 && currentLevel == 5)||(numProAlive == 0 && currentLevel == 4))
		{
			stillAlive = false;
			timer.stop();
			b_LevelFive.setVisible(true);//button visible
		}
	}
	
	public void levelFiveComp()
	{
		if ((min == 1 && currentLevel == 6)||(numProAlive == 0 && currentLevel == 5))
		{
			stillAlive = false;
			timer.stop();
			b_LevelSix.setVisible(true);//button visible
		}
	}
	
	public void levelSixComp()
	{
		if ((min == 1 && currentLevel == 7)||(numProAlive == 0 && currentLevel == 6))
		{
			stillAlive = false;
			timer.stop();
			b_LevelSeven.setVisible(true);//button visible
		}
	}
	
	public void levelSevenComp()
	{
		if ((min == 1 && currentLevel == 8)||(numProAlive == 0 && currentLevel == 7))
		{
			stillAlive = false;
			timer.stop();
			b_LevelEight.setVisible(true);//button visible
		}
	}
	
	public void levelEightComp()
	{
		if ((min == 1 && currentLevel == 9)||(numProAlive == 0 && currentLevel == 8))
		{
			stillAlive = false;
			timer.stop();
			b_LevelNine.setVisible(true);//button visible
		}
	}
	
	public void levelNineComp()
	{
		if ((min == 1 && currentLevel == 10)||(numProAlive == 0 && currentLevel == 9))
		{
			stillAlive = false;
			timer.stop();
			b_LevelTen.setVisible(true);//button visible
		}
	}
	
	public void levelTenComp()
	{
		if ((min == 1 && currentLevel == 11)||(numProAlive == 0 && currentLevel == 10))
		{
			stillAlive = false;
			timer.stop();
			b_LevelTen.setVisible(true);//button visible
		}
	}
	
	//TIMER  ********************************************************************************************************************************

	//Purpose: timer for Actions
	private class MyTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent p1)//method that responds to the timer
		{
			if (stillAlive == true)//checks if character is stillAlive
			{
				count++;//increments count variable, used in the time method
				if (min == 1) currentLevel++;//changes level if 1 minute is reached
				
				//resets bullets and mine variables, keeps track of each variable
				resetBullet();
				resetMine();
				
				countPro();//counts all the projectiles currently alive
				
				//checks for collision between bullets and projectiles and mines with projectiles, level dependent
				//Also moves projectiles and resets them when they move off the screen
				if (currentLevel == 1) 
				{
					bullCollision(levelOnePro);
					mineCollision(levelOnePro);
					setProjAction(levelOnePro);
					resetAllPro(levelOnePro);
				}
				
				else if (currentLevel == 2) 
				{
					bullCollision(levelTwoPro);
					mineCollision(levelTwoPro);
					setProjAction(levelTwoPro);
					resetAllPro(levelTwoPro);
				}
				
				else if (currentLevel == 3) 
				{
					bullCollision(levelThreePro);
					mineCollision(levelThreePro);
					setProjAction(levelThreePro);
					resetAllPro(levelThreePro);
				}
				
				else if (currentLevel == 4) 
				{
					bullCollision(levelFourPro);
					mineCollision(levelFourPro);
					setProjAction(levelFourPro);
					resetAllPro(levelFourPro);
				}
				
				else if (currentLevel == 5) 
				{
					bullCollision(levelFivePro);
					mineCollision(levelFivePro);
					setProjAction(levelFivePro);
					resetAllPro(levelFivePro);
				}
				
				else if (currentLevel == 6) 
				{
					bullCollision(levelSixPro);
					mineCollision(levelSixPro);
					setProjAction(levelSixPro);
					resetAllPro(levelSixPro);
				}
				
				else if (currentLevel == 7) 
				{
					bullCollision(levelSevenPro);
					mineCollision(levelSevenPro);
					setProjAction(levelSevenPro);
					resetAllPro(levelSevenPro);
				}
				
				else if (currentLevel == 8) 
				{
					bullCollision(levelEightPro);
					mineCollision(levelEightPro);
					setProjAction(levelEightPro);
					resetAllPro(levelEightPro);
				}
				
				else if (currentLevel == 9) 
				{
					bullCollision(levelNinePro);
					mineCollision(levelNinePro);
					setProjAction(levelNinePro);
					resetAllPro(levelNinePro);
				}
				
				else if (currentLevel == 10) 
				{
					bullCollision(levelTenPro);
					mineCollision(levelTenPro);
					setProjAction(levelTenPro);
					resetAllPro(levelTenPro);
				}
				
					
				moveBullet();//moves bullets
				
				//checks to see if each level has ended
				levelOneComp();
				levelTwoComp();
				levelThreeComp();
				levelFourComp();
				levelFiveComp();
				levelSixComp();
				levelSevenComp();
				levelEightComp();
				levelNineComp();
				levelTenComp();
				
				time();//keeps track of time

				checkOffScreen();//checks to see that character is on the visible screen
				
				repaint();//repaints application
			}
			else;
			
			//checks to see if game is over depending on the level
			if (currentLevel == 1) gameOver(levelOnePro);
			else if (currentLevel == 2) gameOver(levelTwoPro);
			else if (currentLevel == 3) gameOver(levelThreePro);
			else if (currentLevel == 4) gameOver(levelFourPro);
			else if (currentLevel == 5) gameOver(levelFivePro);
			else if (currentLevel == 6) gameOver(levelSixPro);
			else if (currentLevel == 7) gameOver(levelSevenPro);
			else if (currentLevel == 8) gameOver(levelEightPro);
			else if (currentLevel == 9) gameOver(levelNinePro);
			else if (currentLevel == 10) gameOver(levelTenPro);
		}//end actionperformed
	}//end my timer

	//Purpose: creates object for the Projectile
	//Purpose: Object for the projectiles
	public class Projectile
	{
		public int locX;//x location
		public int locY;//y location
		public int diameter = 10;//diameter 
		public int projSpeedH;//horizontal speed (number of pixels jumped)
		public int projSpeedV;//vertical speed (number of pixels jumped)
		public int projDirH;//Horizontal direction
		public int projDirV;//vertical direction
		public int proCanGo;//time at which projectile can move
		public boolean proOn;//projectile on or not

		//Parameters: x is x location, y is y location, dia is diameter, dirH is horizontal direction
		//dirV is vertical direction, canGo is time at which projectile can move, and on is whether projectile is on or off
		public Projectile (int x, int y, int dia,int dirH, int dirV, int canGo, boolean on )
		{
			locX = x;
			locY = y;
			diameter = dia;
			projDirH = dirH;
			projDirV = dirV;
			projSpeedH = 5;//speed set to 5
			projSpeedV = 5;
			proCanGo = canGo;
			proOn = on;
		}
		
		//Purpose: moves projectiles in the x direction
		//Postcondition: returns x location for the projectile
		public int MoveX()
		{
			locX += projDirH * projSpeedH;//established movement of projectiles
			return locX;
		}
		
		//Purpose: moves projectiles in the y direction
		//Postcondition: returns y location for the projectile
		public int MoveY()
		{
			locY += projDirV * projSpeedV;//establishes movement of projectile
			return locY;
		}
	}//end Projectile

	//Purpose: creates object for the Bullet
	//Purpose: object for the bullet
	public class Bullet
	{
		public double locX;// x location
		public double locY;//y location
		public double angle;//angle to shoot bullet
		public int diameter;//diameter of bullet
		public double speed;//speed of bullet
		
		//Parameters: x is x location, y is y location, ang is angle to shoot and sp is speed
		public Bullet(int x, int y, double ang, double sp)
		{
			locX = x;
			locY = y;
			angle = ang;
			diameter = 15;//diameter set
			speed = sp;
		}
		
		//Purpose: Moves bullet in x direction
		//Postcondition: returns x location for bullet
		public double MoveX()
		{
			locX += Math.cos(angle) * speed;//established movement of bullet by referenced x component of angle
			return locX;
		}
		
		//Purpose: moves bullet in y direction
		//Postcondition: returns y location for bullet
		public double MoveY()
		{
			locY += Math.sin(angle) * speed;//establishes movement of bullet by referencing y component of angle
			return locY;
		}
	}//end Bullet
	
	//Purpose: Creates object for the Mine
	//Purpose: Object for the Mine
	public class Mine
	{
		public int locX;
		public int locY;
		public int diameter;
		
		public Mine(int x, int y)
		{
			locX = x;
			locY = y;
			diameter = 20;
		}
	}//end Mine
}//end game




