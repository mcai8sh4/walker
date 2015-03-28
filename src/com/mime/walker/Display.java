package com.mime.walker;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.mime.walker.graphics.Screen;
import com.mime.walker.input.Controller;
import com.mime.walker.input.InputHandler;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final String TITLE = "Walker : pre-alpha 0.02";

	private Thread thread;
	private Screen screen;
	private Game game;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	private InputHandler input;
	private int newX = 0;
	private int newY = 0;
	private int oldX = 0;
	private int fps;

	public static int MouseSpeed;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		screen = new Screen(WIDTH, HEIGHT);
		game = new Game();
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();

		System.out.println("Working!!");
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;

			requestFocus();

			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					System.out.println(frames + "fps");
					fps = frames;
					previousTime += 1000;
					frames = 0;
				}
			}
			if (ticked) {
				render();
				frames++;
			}

			render();
			frames++;
			newX = InputHandler.MouseX;
			newY = InputHandler.MouseY;
			MouseSpeed = Math.abs(newX - oldX);

			Robot robot = null;
			try {
				robot = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (newX > oldX) {
			 System.out.println("RIGHT!!!!");
				Controller.turnRight = true;
				if (newX > (WIDTH - 100)) {
					robot.mouseMove((InputHandler.MouseScX - WIDTH / 2), InputHandler.MouseScY);
				newX = InputHandler.MouseX;
				oldX = newX +10;
				}
				
			}
			if (newX < oldX) {
				 System.out.println("LEFT!!!!");
				Controller.turnLeft = true;
				if (newX < 100) {
					robot.mouseMove((InputHandler.MouseScX + WIDTH / 2), InputHandler.MouseScY);
					newX = InputHandler.MouseX;
				}
			}
			if (newX == oldX) {
				 System.out.println("STILL!!!!");
				Controller.turnRight = false;
				Controller.turnLeft = false;
			}

			oldX = newX;

		}
	}

	private void tick() {
		game.tick(input.key);
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(game);

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		g.setFont(new Font("Verdana", 2, 25));
		g.setColor(Color.YELLOW);
		g.drawString(fps + "fps", 10, 25);
		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.pack();
		// frame.getContentPane().setCursor(blank);
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		System.out.println("Running...");

		game.start();
	}

}