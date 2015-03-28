package com.mime.walker;

import java.awt.event.KeyEvent;

import com.mime.walker.input.Controller;
import com.mime.walker.level.Level;

public class Game {
	public int time;
	
	public Controller controls;
	public Level level;
	
	public Game() {
		controls = new Controller();
		level = new Level(20,20);
		
	}
	
	public void tick(boolean[] key) {
		time++;
		boolean forward = key[KeyEvent.VK_W];
		boolean back = key[KeyEvent.VK_S];
		boolean left = key[KeyEvent.VK_A];
		boolean right = key[KeyEvent.VK_D];
		boolean jump = key[KeyEvent.VK_SPACE];
		boolean crouch = key[KeyEvent.VK_CONTROL];
		boolean run = key[KeyEvent.VK_SHIFT];
		
		boolean exit = key[KeyEvent.VK_ESCAPE];
		
		
		controls.tick(forward, back, left, right, jump, crouch, run, exit);
	}

}
