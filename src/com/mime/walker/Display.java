package com.mime.walker;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import com.mime.walker.graphics.Screen;
import com.mime.walker.gui.Launcher;
import com.mime.walker.input.Controller;
import com.mime.walker.input.InputHandler;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static int width = 800;
	public static int height = 600;

	public static final String TITLE = "Walker : pre-alpha 0.05";

	private Thread thread;
	private Screen screen;
	private Game game;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	private InputHandler input;
	private int newX = 0;
	private int oldX = 0;
	private int fps;
	public static int selection = 0;
	public static int MouseSpeed;

	static Launcher launcher;

	public Display() {
		Dimension size = new Dimension(getGameWidth(), getGameHeight());
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		screen = new Screen(getGameWidth(), getGameHeight());
		game = new Game();
		img = new BufferedImage(getGameWidth(), getGameHeight(), BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	public static Launcher getLauncherInstance() {
		if (launcher == null) {
			launcher = new Launcher(0);
		}
		return launcher;
	}

	public static int getGameWidth() {

		return width;
	}

	public static int getGameHeight() {

		return height;
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this, "game");
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
			System.exit(1);
		}
	}

	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;
		requestFocus();

		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;

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

				// render();
				frames++;
			}
			if (ticked) {
				render();
				frames++;
			}

		}
	}

	private void tick() {
		game.tick(input.key);
		newX = InputHandler.MouseX;
		if (newX > oldX) {
			// System.out.println("RIGHT!!!!");
			Controller.turnRight = true;

		}
		if (newX < oldX) {
			// System.out.println("LEFT!!!!");
			Controller.turnLeft = true;
		}
		if (newX == oldX) {
			// System.out.println("STILL!!!!");
			Controller.turnRight = false;
			Controller.turnLeft = false;
		}
		MouseSpeed = Math.abs(newX - oldX);

		oldX = newX;
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(game);

		for (int i = 0; i < getGameWidth() * getGameHeight(); i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, getGameWidth(), getGameHeight(), null);
		g.setFont(new Font("Verdana", 2, 25));
		g.setColor(Color.YELLOW);
		g.drawString(fps + "fps", 10, 25);
		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		getLauncherInstance();
	}

}