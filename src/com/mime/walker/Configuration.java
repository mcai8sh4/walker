package com.mime.walker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {

	Properties properties = new Properties();

	public void saveConfiguration(String key, int value) {
		String path = "res/settings/config.xml";
		try {
			File file = new File(path);
			boolean exists = file.exists();
			if (!exists) {
				file.createNewFile();
			}
			OutputStream write = new FileOutputStream(path);
			properties.setProperty(key, Integer.toString(value));
			properties.storeToXML(write, "Resolution");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadConfiguration(String path) {
		try {
			InputStream read = new FileInputStream(path);
			properties.loadFromXML(read);
			String width = properties.getProperty("width");
			String height = properties.getProperty("height");
			setResolution(Integer.parseInt(width), Integer.parseInt(height));
			read.close();
		} catch (FileNotFoundException e) {
			saveConfiguration("width", 800);
			saveConfiguration("height", 600);
			loadConfiguration(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setResolution(int width, int height) {
		Display.width = width;
		Display.height = height;
	}

}
