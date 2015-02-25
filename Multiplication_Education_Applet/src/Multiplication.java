/*
 * 3/31/14: My first task is learning how to use a JFrame. Today I have studied how to start
 *  everything which is actually taking some time.
 * Because of graphics I'm not sure whether I want to use JFrame or JPanel
 * 
 * 4/4/14: I am beginning to understand how the JFrame works. Right now I am 
 * attemting to impliement buttons and figure out how to use graphics. I actually am deciding to
 * avoid graphics currently becaue its slightly difficult and just use a jlabel to display text.
 *
 * 4/5/14: Today I am attempting to make the ActionListener work. Using everything in JFrame is much
 *more diffiult then i first anticipated.
 * 
 * 4/7/14: Transferred into an applet so I can work quicker and get more done. Today I made methods
 * that make a table and assign values to my array.
 * 
 * 4/9/14: Today I am working on implementing checkboxes for the quiz mode. Once that is complete 
 * can begin to develop the quizes.
 * 
//Cole Schafer


import java.applet.Applet;
import java.awt.*;
import javax.swing.*; 
import java.awt.event.*;

public class Multiplication extends Applet implements ActionListener, ItemListener
{

	private static final long serialVersionUID = 1L;
	//Fonts
	private Font fnt12P = new Font("Helvetica", Font.PLAIN, 12);
	private Font fnt16P = new Font("Helvetica", Font.PLAIN, 16);
	private Font fnt20P = new Font("Helvetica", Font.PLAIN, 20);
	private Font fnt36P = new Font("Helvetica", Font.PLAIN, 36);
	private Font fnt48P = new Font("Helvetica", Font.PLAIN, 48);
	
	//Colors
	private static Color black = new Color(0,0,0);
	private Color white = new Color(255,255,255);
	
	//Screen Size
	public static int WIDTH = 900;
	public static int HEIGHT = 600;
	
	//Buttons
	private Button b_PracticeMode, b_QuizMode, b_Menu, b_Reset, b_RealQuiz, b_NextQue;
	private TextField tf_QuizSol;
	
	//Display booleans
	boolean menu = true;
	boolean practice = false;
	boolean quiz = false;
	boolean realQuiz = false;
	boolean badFormat = false;
	boolean quizDone = false;
	
	//counts correct and incorrect responses
	public int correct = 0;
	public int incorrect = 0;
	
	//graphic buffer and repaint
	private Graphics buffG;
	private Image offscreen;
	
	private Timer timer;//establish timer
	
	//quiz related variables
	public int maxQue = 50;
	public int questions=10;
	public int currentQue = 0;
	public double percentCorr;
	
	//arrays
	String[][] tableInput = new String[13][13];//array of strings of data in the table
	TextField[][] entry = new TextField[13][13];//array of text fields for table
	boolean[][] correctTable = new boolean[13][13];//correct answers in the table
	Checkbox[] cb_quizSelect = new Checkbox[13];//array of checkboxes for quiz mode
	boolean[] numOn = new boolean[13];//boolean to determine if a number has been selected
	Question[] questArr = new Question[maxQue+1];//array of question objects
	String[] quizInput = new String[maxQue+1];//records input for each answer
	boolean[] questCor = new boolean[maxQue+1];//determines whether response is correct or incorrect
	String[] solution = new String[145];//strings to compare resonses to
	
	CheckboxGroup numQues;//radio buttons for number of questions
	Checkbox numQues1,numQues2,numQues3, numQues4, numQues5;//radio buttons defined

	//Images
	ImageIcon check;
	ImageIcon xMark;
	
//---------JAVA METHODS---------------------------------------------------------------------------------
	
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
	
	//Purpose: intializes applet
	public void init()
	{
		setSize(WIDTH, HEIGHT);//sets size of the frame
		
		setLayout(null);//turn off Layout manager
		setFocusable(true);
		setBackground(Color.LIGHT_GRAY);//sets background
		
		defineValueArray();//defines arrays
		defineTextFieldArray();
		defineCheckboxArray();
		
		check = new ImageIcon(getClass().getResource("check.png"));//defines images
		xMark = new ImageIcon(getClass().getResource("x.png"));
		
		b_PracticeMode = new Button("PRACTICE MODE"); //construct the button
		b_PracticeMode.setSize(200,50); //set size of button
		b_PracticeMode.setLocation(WIDTH/2 - 100, 150);//set location
		b_PracticeMode.setFont(fnt20P); //sets font of the button text
		b_PracticeMode.setVisible(true);//button visible
		b_PracticeMode.setBackground(white);//set background color
		b_PracticeMode.setForeground(black);//set text color
		b_PracticeMode.addActionListener(this);//makes button respond to clicks
		add(b_PracticeMode);//adds button to applet
		
		b_QuizMode = new Button("QUIZ MODE"); //construct the button
		b_QuizMode.setSize(200,50); //set size of button
		b_QuizMode.setLocation(WIDTH/2 - 100, 250);//set location
		b_QuizMode.setFont(fnt20P); //sets font of the button text
		b_QuizMode.setVisible(true);//button visible
		b_QuizMode.setBackground(white);//set background color
		b_QuizMode.setForeground(black);//set text color
		b_QuizMode.addActionListener(this);//makes button respond to clicks
		add(b_QuizMode);//adds button to applet
		
		b_Menu = new Button("MENU"); //construct the button
		b_Menu.setSize(100,40); //set size of button
		b_Menu.setLocation(0, HEIGHT - 40);//set location
		b_Menu.setFont(fnt20P); //sets font of the button text
		b_Menu.setVisible(false);//button visible
		b_Menu.setBackground(white);//set background color
		b_Menu.setForeground(black);//set text color
		b_Menu.addActionListener(this);//makes button respond to clicks
		add(b_Menu);//adds button to applet
		
		b_Reset = new Button("RESET"); //construct the button
		b_Reset.setSize(100,40); //set size of button
		b_Reset.setLocation(100, HEIGHT - 40);//set location
		b_Reset.setFont(fnt20P); //sets font of the button text
		b_Reset.setVisible(false);//button visible
		b_Reset.setBackground(white);//set background color
		b_Reset.setForeground(black);//set text color
		b_Reset.addActionListener(this);//makes button respond to clicks
		add(b_Reset);//adds button to applet
		
		b_RealQuiz = new Button("BEGIN QUIZING"); //construct the button
		b_RealQuiz.setSize(200,40); //set size of button
		b_RealQuiz.setLocation(WIDTH/2 - 100, 500);//set location
		b_RealQuiz.setFont(fnt20P); //sets font of the button text
		b_RealQuiz.setVisible(false);//button visible
		b_RealQuiz.setBackground(white);//set background color
		b_RealQuiz.setForeground(black);//set text color
		b_RealQuiz.addActionListener(this);//makes button respond to clicks
		add(b_RealQuiz);//adds button to applet
		
		b_NextQue = new Button("NEXT QUESTION"); //construct the button
		b_NextQue.setSize(200,40); //set size of button
		b_NextQue.setLocation(WIDTH/2 - 100, HEIGHT-250);//set location
		b_NextQue.setFont(fnt20P); //sets font of the button text
		b_NextQue.setVisible(false);//button visible
		b_NextQue.setBackground(white);//set background color
		b_NextQue.setForeground(black);//set text color
		b_NextQue.addActionListener(this);//makes button respond to clicks
		add(b_NextQue);//adds button to applet
		
		tf_QuizSol = new TextField("");//construct the textfield
		tf_QuizSol.setSize(90, 55);//sets size
		tf_QuizSol.setLocation(525, HEIGHT/2 -40);//set location
		tf_QuizSol.setFont(fnt48P);//sets font
		tf_QuizSol.setVisible(false);//sets visiblity
		tf_QuizSol.setBackground(white);//sets background color
		tf_QuizSol.setForeground(black);//sets text color
		add(tf_QuizSol);//adds to applet
		
		numOn[0] = true;//initializes zero to true
		
		for (int i = 0; i <= 144;i++)
		{
			solution[i] = ""+i;
		}
		
		timer = new Timer(15, new MyTimer());//set timer
		timer.start();//starts timer
	}

	//Purpose: paints on the applet
	public void paint(Graphics g)
	{
		g.setColor(Color.BLACK);//set color black
		g.setFont(fnt36P);//set font
		
		//menu screen
		if (menu == true)
		{
			g.drawString("MULTIPLICATION FUN", WIDTH/2 - 175, 75 );
			g.setFont(fnt16P);
			g.drawString("Created by Cole Schafer", 355,550);
		}
		
		//practice table
		if (practice == true)
		{
			paintTable(g);
		}
		
		//starts quiz
		if (quiz == true)
		{
			g.drawString("Select which numbers you would like to quiz on", 100,100);
			g.setFont(fnt12P);
			g.drawString("If you fail to select any numbers you will be quized on zeros only",300,125);
			g.drawLine(0,250,WIDTH,250);
			g.setFont(fnt36P);
			g.drawString("Select how many questions you would like", 120,310);
		}
		
		//displayed while quizing
		if (realQuiz == true)
		{
			g.setFont(fnt48P);
			g.drawString(questArr[currentQue].fac1 + "  x  " + questArr[currentQue].fac2 + " = ", 300, HEIGHT/2);
			g.drawString("Question " + (currentQue+1) + " of " + questions,300, HEIGHT/4);
			g.setFont(fnt20P);
			g.drawString("If you press the MENU button your quiz will be terminated", 125, HEIGHT-15);
		}
		
		//if no answer is put in
		if (badFormat == true)
		{
			g.setColor(Color.RED);
			g.setFont(fnt48P);
			g.drawString("Enter format correctly", 250, 200);
		}
		
		//displayed when the quiz is over
		if (quizDone == true)
		{
			g.setFont(fnt20P);
			g.drawString("Out of "+ questions + " questions you got " + correct + " correct and "+ incorrect + " incorrect.",200,50);
			g.drawString("You answered " + percentCorr + "% correctly",300,100);
			
			g.setFont(fnt16P);
			int count = 1;
			for (int i = 0; i < questions; i++)
			{
				if (i <= 19) //displays correct and incorrect questions
				{
					g.setColor(Color.BLACK);
					g.drawString((i+1)+".",100,125 + 20*count);
					if (questCor[i] == false)
					{
						g.setColor(Color.RED);
						g.drawString(questArr[i].fac1 + "  x  " + questArr[i].fac2 + " = " + quizInput[i] , 125, 125 + 20*count);
						g.setColor(Color.BLACK);
						g.drawString("Correct: " + questArr[i].solution, 225, 125 + 20*count);
						g.drawImage(xMark.getImage(),80,115 + 20*count,10,10,this);//draws x marks if wrong
					}	
						
					if (questCor[i] == true)
					{
						g.setColor(Color.BLACK);
						g.drawString(questArr[i].fac1 + "  x  " + questArr[i].fac2 + " = " + quizInput[i] , 125, 125 + 20*count);
						g.drawImage(check.getImage(),80,115 + 20*count,10,10,this);//draws check if true
					}
				}
					
				if (i > 19 && i <= 39) //same applies below each is for a different row
				{
					g.drawString((i+1)+".",350,125 + 20*(count-20));
					
					if (questCor[i] == false)
					{
						g.setColor(Color.RED);
						g.drawString(questArr[i].fac1 + "  x  " + questArr[i].fac2 + " = " + quizInput[i] , 375, 125 + 20*(count-20));
						g.setColor(Color.BLACK);
						g.drawString("Correct: " + questArr[i].solution, 475, 125 + 20*(count-20));
						g.drawImage(xMark.getImage(),330,115 + 20*(count-20),10,10,this);
					}	
						
					if (questCor[i] == true)
					{
						g.setColor(Color.BLACK);
						g.drawString(questArr[i].fac1 + "  x  " + questArr[i].fac2 + " = " + quizInput[i] , 375, 125 + 20*(count-20));
						g.drawImage(check.getImage(),330,115 + 20*(count-20),10,10,this);
					}
				}
					
				if (i > 39 && i <= 49) 
				{
					g.drawString((i+1)+".",600,125 + 20*(count-40));
					
					if (questCor[i] == false)
					{
						g.setColor(Color.RED);
						g.drawString(questArr[i].fac1 + "  x  " + questArr[i].fac2 + " = " + quizInput[i] , 625, 125 + 20*(count-40));
						g.setColor(Color.BLACK);
						g.drawString("Correct: " + questArr[i].solution, 725, 125 + 20*(count-40));
						g.drawImage(xMark.getImage(),580,115 + 20*(count-40),10,10,this);
					}	
						
					if (questCor[i] == true)
					{
						g.setColor(Color.BLACK);
						g.drawString(questArr[i].fac1 + "  x  " + questArr[i].fac2 + " = " + quizInput[i] , 625, 125 + 20*(count-40));
						g.drawImage(check.getImage(),580,115 + 20*(count-40),10,10,this);
					}
				}
				count++;//increments count variable
			}
		}
		
		graphCorrect(g);//checks for correct graph entries
	}
	
	//Purpose: Responds to button on the applet
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		
		if (source == b_PracticeMode)
		{
			menu = false;
			practice = true;
			quiz = false;
			
			b_PracticeMode.setVisible(false);
			b_QuizMode.setVisible(false);
			
			b_Menu.setVisible(true);//button visible
			b_Reset.setVisible(true);
			
			tfSetVisible(true);
		}
		
		//sets everything for the menu.
		if (source == b_Menu)
		{
			menu = true;
			practice = false;
			quiz = false;
			realQuiz = false;
			quizDone = false;
			badFormat = false;
			
			currentQue = 0;
			correct = 0;
			incorrect = 0;
			
			for (int i = 0; i <= 12; i++)
			{
				if (i == 0) cb_quizSelect[i].setState(true);
				else cb_quizSelect[i].setState(false);
			}
			
			for (int i = 0; i <= 12; i++)
			{
				numOn[i] = false;
			}
			
			b_PracticeMode.setVisible(true);
			b_QuizMode.setVisible(true);
			
			b_Menu.setVisible(false);//button visible
			b_Reset.setVisible(false);
			b_RealQuiz.setVisible(false);//button visible
			tf_QuizSol.setVisible(false);//sets visiblity
			b_NextQue.setVisible(false);//button visible
			
			tfSetVisible(false);
			cbSetVisible(false);
		}
		
		//resets table
		if (source == b_Reset)
		{
			for (int i = 0;i <= 12; i++)
			{
				for (int j = 0; j <= 12; j++)
				{
					entry[i][j].setText("");
				}
			}
		}
		
		//displays proper stuff for quiz
		if (source == b_QuizMode)
		{
			quiz = true;
			menu = false;
			practice = false;
			
			questions = 10;
			numOn[0] = true;
			
			b_PracticeMode.setVisible(false);
			b_QuizMode.setVisible(false);
			
			b_Menu.setVisible(true);//button visible
			b_RealQuiz.setVisible(true);//button visible
			
			cbSetVisible(true);
		}
		
		//displays proper stuff while quizing occurs
		if (source == b_RealQuiz)
		{
			quiz = false;
			realQuiz = true;
			
			b_RealQuiz.setVisible(false);//button visible
			b_NextQue.setVisible(true);//button visible
			tf_QuizSol.setVisible(true);//sets visiblity
			
			cbSetVisible(false);
			makeQuiz(questions);
			tf_QuizSol.requestFocusInWindow();
		}
		
		//correctly transitions from question to question
		if (source == b_NextQue)
		{
			if (tf_QuizSol.getText().equals(""))//checks to see if something has been entered
			{
				badFormat = true;
			}
			
			if (!tf_QuizSol.getText().equals(""))//if something is entered
			{
				int var = 0;
				for (int j = 0; j <= 144; j++)
				{
					if (tf_QuizSol.getText().equals(solution[j])) var++;
				}
				if (var == 0) badFormat = true;
				if (var != 0) badFormat = false;
			}
			
			if (!tf_QuizSol.getText().equals("") && badFormat == false)//if something is entered
			{
				badFormat = false;
				quizInput[currentQue] = tf_QuizSol.getText();//records response
				if (Integer.parseInt(quizInput[currentQue]) == questArr[currentQue].solution)
				{
					questCor[currentQue] = true;//transistions to next question
					tf_QuizSol.setText(null);
					tf_QuizSol.requestFocusInWindow();
				}
			
				if (Integer.parseInt(quizInput[currentQue]) != questArr[currentQue].solution && !quizInput[currentQue].equals(""))
				{
					questCor[currentQue] = false;//if wrong records wrong answer
					tf_QuizSol.setText(null);
					tf_QuizSol.requestFocusInWindow();
				}
				currentQue++;
			}
			
			if (currentQue == questions)//if the quiz is done
			{
				quizDone = true;
				realQuiz = false;
				
				b_NextQue.setVisible(false);
				tf_QuizSol.setVisible(false);
				
				for (int i = 0; i <= questions ; i++)//checks for number of correct responses
				{
					if (questCor[i] == true) correct++;
				}
				incorrect = questions - correct;//determines number of incorrect resonses
				
				percentCorr = correct*100.0/questions;//determines percentage correct
			}
		}	
			
		repaint();//repaints to the screen
	}
	
	//responds to check boxes
	public void itemStateChanged(ItemEvent p1)
	{	
		for (int i = 0; i <= 12; i++)
		{	
			if (cb_quizSelect[i].getState())
			{
				numOn[i] = true;
			}
			else
			{
				numOn[i] = false;
			}
		}	
		
		int count = 0;
		for (int j = 0; j <=12; j++)
		{
			if (numOn[j] == false) count++;
		}
		if (count == 13) 
		{
			numOn[0] = true;
			cb_quizSelect[0].setState(true);
		}	
		
		if (p1.getSource() == numQues1) questions = 10;
		if (p1.getSource() == numQues2) questions = 20;
		if (p1.getSource() == numQues3) questions = 30;
		if (p1.getSource() == numQues4) questions = 40;
		if (p1.getSource() == numQues5) questions = 50;
	}

//runs the timer
	private class MyTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent p1)//method that responds to the timer
		{
			checkCorrect();
			repaint();//repaints application
		}
	}

//------------Practice Table Methods----------------------------------------------------------------------------	
	
	//Purpose: Paint the table and axis on the practice table
	public void paintTable(Graphics g)
	{
		int x = WIDTH/14;//divides up the screen
		int y = (HEIGHT - 40)/14;
		
		g.drawLine(0,0,WIDTH,0);
		g.drawLine(0,0,0,HEIGHT);
		
		//draws lines onto the screen
		for (int i = 1; i<=14; i++)
		{
			g.drawLine(i * x, 0, i * x, HEIGHT-42);
			if (i < 14) g.drawString("  "+(i-1), i * x+1, 35);
		}
		
		//draws lines onto the screen
		for (int j = 1; j<=14; j++)
		{
			g.drawLine(0, j * y, WIDTH-4, j * y);
			if (j < 14) g.drawString("  "+(j-1), 0 ,((j+1) * y)-5);
		}	
	}
	
	//Purpose: Define the values of each spot on the table
	public void defineValueArray()
	{
		for (int i = 0;i <= 12; i++)
		{
			for (int j = 0; j <= 12; j++)
			{
				tableInput[i][j] = ""+i*j; //arrayis defined
			}
		}
	}
	
	//Purpose: Defines each of the text fields on the table
	public void defineTextFieldArray()
	{
		int x = WIDTH/14;
		int y = (HEIGHT-40)/14;
		
		for (int i = 0;i <= 12; i++)
		{
			for (int j = 0; j <= 12; j++)
			{
				entry[i][j] = new TextField(""); 
				entry[i][j].setSize(30, 20);//sets size
				entry[i][j].setLocation(((i+1) * x)+2, ((j+1) * y)+2);//set location
				entry[i][j].setFont(fnt12P);//sets font
				entry[i][j].setVisible(false);//sets visiblity
				entry[i][j].setBackground(Color.WHITE);//sets background color
				entry[i][j].setForeground(Color.BLACK);//sets text color
				add(entry[i][j]);//adds to applet
			}
		}
	}
	
	//Purpose: sets the text fields to visible or not
	public void tfSetVisible(boolean visible)
	{
		for (int i = 0;i <= 12; i++)
		{
			for (int j = 0; j <= 12; j++)
			{
				if (visible == true) entry[i][j].setVisible(true);//sets visiblity
				if (visible == false) entry[i][j].setVisible(false);//sets visiblity
			}
		}
	
	}
	
	//Purpose: checks to see if the value in the text field is equal to the correct value
	public void checkCorrect()
	{
		for (int i = 0;i <= 12; i++)
		{
			for (int j = 0; j <= 12; j++)
			{
				if (correctTable[i][j] == false && entry[i][j].getText().equals(tableInput[i][j])) 
				{
					correctTable[i][j] = true;
				}
				
				if (correctTable[i][j] == true && !entry[i][j].getText().equals(tableInput[i][j]))
				{
					correctTable[i][j] = false;
				}
			}
		}
	}
	
	//Purpose: paints a check or x
	public void graphCorrect(Graphics g)
	{
		int x = WIDTH/14;
		int y = (HEIGHT-40)/14;
		
		for (int i = 0;i <= 12; i++)
		{
			for (int j = 0; j <= 12; j++)
			{
				if (correctTable[i][j] == true && practice == true) g.drawImage(check.getImage(),((i+1) * x)+35,((j+1) * y)+10,25,25,this);
				if (correctTable[i][j] == false && !entry[i][j].getText().equals("") && practice == true) g.drawImage(xMark.getImage(),((i+1) * x)+35,((j+1) * y)+10,25,25,this);
			}
		}
	}
	
//----QUIZ MODE METHODS---------------------------------------------------------
	
	//Purpose: defines checkbox array of checkboxes
	public void defineCheckboxArray()
	{
		for (int i = 0; i <= 12; i++)
		{
			if (i == 0) cb_quizSelect[i] = new Checkbox(i+"'s", true);//construct the checkbox
			if (i != 0) cb_quizSelect[i] = new Checkbox(i+"'s", false);//construct the checkbox
			cb_quizSelect[i].setSize(40, 20);//sets size
			cb_quizSelect[i].setLocation(55*(i+2),200);//set location
			cb_quizSelect[i].setFont(fnt12P);//sets font
			cb_quizSelect[i].setVisible(false);//sets visiblity
			cb_quizSelect[i].setBackground(white);//sets background color
			cb_quizSelect[i].setForeground(black);//sets text color
			cb_quizSelect[i].addItemListener(this);//allows response to click
			add(cb_quizSelect[i]);//adds to applet
		}
		
		numQues = new CheckboxGroup();
		
		numQues1 = new Checkbox("10", true, numQues);
		numQues1.setSize(40, 20);//sets size
		numQues1.setLocation(WIDTH/6,400);//set location
		numQues1.setVisible(false);//sets visiblity
		numQues1.setBackground(white);//sets background color
		numQues1.setForeground(black);//sets text color
		numQues1.addItemListener(this);//allows response to click
		add(numQues1);
		
		numQues2 = new Checkbox("20", false, numQues);
		numQues2.setSize(40, 20);//sets size
		numQues2.setLocation((WIDTH/6) * 2,400);//set location
		numQues2.setVisible(false);//sets visiblity
		numQues2.setBackground(white);//sets background color
		numQues2.setForeground(black);//sets text color
		numQues2.addItemListener(this);//allows response to click
		add(numQues2);
		
		numQues3 = new Checkbox("30", false, numQues);
		numQues3.setSize(40, 20);//sets size
		numQues3.setLocation((WIDTH/6) * 3,400);//set location
		numQues3.setVisible(false);//sets visiblity
		numQues3.setBackground(white);//sets background color
		numQues3.setForeground(black);//sets text color
		numQues3.addItemListener(this);//allows response to click
		add(numQues3);
		
		numQues4 = new Checkbox("40", false, numQues);
		numQues4.setSize(40, 20);//sets size
		numQues4.setLocation((WIDTH/6) * 4,400);//set location
		numQues4.setVisible(false);//sets visiblity
		numQues4.setBackground(white);//sets background color
		numQues4.setForeground(black);//sets text color
		numQues4.addItemListener(this);//allows response to click
		add(numQues4);
		
		numQues5 = new Checkbox("50", false, numQues);
		numQues5.setSize(40, 20);//sets size
		numQues5.setLocation((WIDTH/6) * 5,400);//set location
		numQues5.setVisible(false);//sets visiblity
		numQues5.setBackground(white);//sets background color
		numQues5.setForeground(black);//sets text color
		numQues5.addItemListener(this);//allows response to click
		add(numQues5);
	}
	
	//Purpose: sets visible or indivisible the checkboxes
	public void cbSetVisible(boolean visible)
	{
		for (int i = 0; i <= 12; i++)
		{
			if (visible == true) cb_quizSelect[i].setVisible(true);//sets visiblity
			if (visible == false) cb_quizSelect[i].setVisible(false);//sets visiblity
		}
		
		for (int j = 0; j <= 9; j++)
		{
			if (visible == true) 
			{
				numQues1.setVisible(true);//sets visiblity
				numQues2.setVisible(true);//sets visiblity
				numQues3.setVisible(true);//sets visiblity
				numQues4.setVisible(true);//sets visiblity
				numQues5.setVisible(true);//sets visiblity
			}
			
			if (visible == false) 
			{
				numQues1.setVisible(false);//sets visiblity
				numQues2.setVisible(false);//sets visiblity
				numQues3.setVisible(false);//sets visiblity
				numQues4.setVisible(false);//sets visiblity
				numQues5.setVisible(false);//sets visiblity
			}
		}
	}
	
	//generates random number based on those selected
	public int ranSelectNum()
	{
		int num = 0;
		boolean numFound = false;
		while (!numFound)
		{	
			num = ranNum(0,13);
			if (numOn[num] == true) 
			{
				numFound = true;
			}
		}
		return num;
	}
	
	//defines the quiz
	public void makeQuiz(int numQ)
	{
		for (int i = 0; i < numQ; i++)
		{
			questArr[i] = new Question(ranSelectNum() ,ranNum(0,12));//defines it with a randomly defined number and another random
		}
	}
	
	//public void 
	
//----------OTHER METHODS-------------------------------------	
	
	//Purpose: generates random numbers between two values
	//Parameters: min is minimum number you want and max is maximum number you want
	//Postcondition: returns the random number
 	public int ranNum (int min, int max)
	{
		int ranNum = (int)(min + max * Math.random());//random number generate
		return ranNum;
	}//end ranNum
 	
 //----Question Class-------------------------------------------------------------------------
 	
 	//Purpose: Object for each question
 	public class Question
 	{
 		public int fac1;//factor 1
 		public int fac2;//factor 2
 		public int solution;//solution
 		
 		public Question(int numOne, int numTwo)
 		{
 			fac1 = numOne;
 			fac2 = numTwo;
 			solution = numOne * numTwo;//solution is product of both factors
 		}
 	}
}
