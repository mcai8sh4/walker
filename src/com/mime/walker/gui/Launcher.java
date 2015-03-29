package com.mime.walker.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mime.walker.Configuration;
import com.mime.walker.Display;
import com.mime.walker.RunGame;
import com.mime.walker.input.InputHandler;

public class Launcher extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	protected JPanel window = new JPanel();
	private JButton play, options, help, quit;
	private Rectangle rplay, roptions, rhelp, rquit;
	Configuration config = new Configuration();

	private int width = 800;
	private int height = 400;
	protected int buttonWidth = 80;
	protected int buttonHeight = 35;
	boolean running = false;
	Thread thread;
	JFrame frame = new JFrame();

	public Launcher(int id) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame.setUndecorated(true);
		frame.setTitle("Walker Launcher...");
		frame.setSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// getContentPane().add(window);
		frame.add(this);
		// frame.add(display);
		frame.setLocationRelativeTo(null);

		frame.setResizable(false);
		frame.setVisible(true);
		window.setLayout(null);

		if (id == 0) {
			drawButtons();
		}
		InputHandler input = new InputHandler();
		addMouseListener(input);
		addMouseMotionListener(input);
		addKeyListener(input);
		addFocusListener(input);
		startMenu();
		repaint();
	}

	public void updateFrame() {
		if (InputHandler.dragged) {
			Point p = frame.getLocation();
			frame.setLocation(p.x + InputHandler.MouseDX - InputHandler.MousePX, p.y + InputHandler.MouseDY - InputHandler.MousePY);
		}
	}

	public void startMenu() {
		running = true;
		thread = new Thread(this, "menu");
		thread.start();
	}

	public void stopMenu() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (running) {
			try {
				renderMenu();
			} catch (IllegalStateException e) {
				System.out.println("Handled, baby!");
			}
			updateFrame();
		}
	}

	private void renderMenu() throws IllegalStateException {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 400);
		try {
			g.drawImage(ImageIO.read(Display.class.getResource("/launcher_img.jpg")), 0, 0, 800, 400, null);

			// MENU BUTTONS
			// play
			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 100 && InputHandler.MouseY > 130 && InputHandler.MouseY < 130 + 30) {
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/play_on.png")), 690, 130, 80, 30, null);
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/arrow.png")), 620, 130, 80, 30, null);
				if (InputHandler.MouseButton == 1) {
					System.out.println("PLAY");
					config.loadConfiguration("res/settings/config.xml");
					frame.dispose();
					new RunGame();
				}
			} else {
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/play_off.png")), 690, 130, 80, 30, null);
			}

			// options
			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 100 && InputHandler.MouseY > 165 && InputHandler.MouseY < 165 + 30) {
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/options_on.png")), 690, 165, 80, 30, null);
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/arrow.png")), 620, 165, 80, 30, null);
				if (InputHandler.MouseButton == 1) {
					new Options();
				}
			} else {
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/options_off.png")), 690, 165, 80, 30, null);
			}

			// help
			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 100 && InputHandler.MouseY > 200 && InputHandler.MouseY < 200 + 30) {
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/help_on.png")), 690, 200, 80, 30, null);
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/arrow.png")), 620, 200, 80, 30, null);
				if (InputHandler.MouseButton == 1) {
					System.out.println("HELP");
				}
			} else {
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/help_off.png")), 690, 200, 80, 30, null);
			}

			// quit
			if (InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 100 && InputHandler.MouseY > 235 && InputHandler.MouseY < 235 + 30) {
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/quit_on.png")), 690, 235, 80, 30, null);
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/arrow.png")), 620, 235, 80, 30, null);
				if (InputHandler.MouseButton == 1) {
					System.exit(0);
				}
			} else {
				g.drawImage(ImageIO.read(Display.class.getResource("/buttons/quit_off.png")), 690, 235, 80, 30, null);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		g.dispose();
		bs.show();

	}

	private void drawButtons() {
		play = new JButton("Play");
		rplay = new Rectangle(((width / 2) - (buttonWidth / 2)), 90, buttonWidth, buttonHeight);
		play.setBounds(rplay);
		window.add(play);

		options = new JButton("Options");
		roptions = new Rectangle(((width / 2) - (buttonWidth / 2)), 140, buttonWidth, buttonHeight);
		options.setBounds(roptions);
		window.add(options);

		help = new JButton("Help");
		rhelp = new Rectangle(((width / 2) - (buttonWidth / 2)), 190, buttonWidth, buttonHeight);
		help.setBounds(rhelp);
		window.add(help);

		quit = new JButton("Quit");
		rquit = new Rectangle(((width / 2) - (buttonWidth / 2)), 240, buttonWidth, buttonHeight);
		quit.setBounds(rquit);
		window.add(quit);

		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.loadConfiguration("res/settings/config.xml");
				frame.dispose();
				new RunGame();
			}
		});

		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new Options();
			}
		});

		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("help button");
			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

	}

}
