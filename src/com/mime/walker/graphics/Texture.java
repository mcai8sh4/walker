package com.mime.walker.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {
	public static Render floor = loadBitmap("/textures/floor.png");

	public static Render loadBitmap(String fileName) {
		try {
			BufferedImage image = ImageIO.read(Texture.class.getResource(fileName));
			int width = image.getWidth();
			int height = image.getHeight();
			Render result = new Render(width, height);
			image.getRGB(0, 0, width, height, result.pixels, 0, width);
			return result;
		} catch (Exception e) {
			System.out.println("Crash!");
			throw new RuntimeException(e);
		}
	}

}
