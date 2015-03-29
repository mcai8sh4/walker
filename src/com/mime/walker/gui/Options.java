package com.mime.walker.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mime.walker.Configuration;

public class Options extends JFrame {
	private static final long serialVersionUID = 1L;

	private int width = 550;
	private int height = 450;
	private JButton OK;
	private int buttonWidth = 80;
	private int buttonHeight = 35;
	private Rectangle rOK, rresolution;
	private JTextField tWidth, tHeight;
	private JLabel ldefSizes, lWidth, lHeight;
	Configuration config = new Configuration();

	int w = 0;
	int h = 0;

	JPanel window = new JPanel();

	private Choice resolution = new Choice();

	public Options() {

		setTitle("Walker - Options");
		setSize(new Dimension(width, height));
		add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);

		drawButtons();
		window.repaint();
	}

	private void drawButtons() {
		OK = new JButton("OK");
		rOK = new Rectangle(width - 100, height - 70, buttonWidth, buttonHeight);
		OK.setBounds(rOK);
		window.add(OK);

		ldefSizes = new JLabel("Standard Sizes");
		ldefSizes.setBounds(57, 60, 120, 20);
		window.add(ldefSizes);
		rresolution = new Rectangle(50, 80, 110, 35);
		resolution.setBounds(rresolution);
		resolution.add("640 x 480");
		resolution.add("800 x 600");
		resolution.add("1024 x 768");
		resolution.select(1);
		window.add(resolution);

		lWidth = new JLabel("Width ");
		lWidth.setBounds(50, 150, 120, 20);
		window.add(lWidth);
		tWidth = new JTextField();
		tWidth.setBounds(95, 150, 60, 20);
		window.add(tWidth);

		lHeight = new JLabel("Height ");
		lHeight.setBounds(50, 180, 120, 20);
		window.add(lHeight);
		tHeight = new JTextField();
		tHeight.setBounds(95, 180, 60, 20);
		window.add(tHeight);

		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Launcher(0);
				config.saveConfiguration("width", parseWidth());
				config.saveConfiguration("height", parseHeight());

			}
		});
	}

	private void drop() {
		int selection = resolution.getSelectedIndex();
		if (selection == 0) {
			w = 640;
			h = 480;
		}
		if (selection == 1 || selection == -1) {
			w = 800;
			h = 600;
		}
		if (selection == 2) {
			w = 1024;
			h = 768;
		}

	}

	private int parseWidth() {
		try {
			int w = Integer.parseInt(tWidth.getText());
			return w;
		} catch (NumberFormatException e) {
			drop();
			return w;
		}
	}

	private int parseHeight() {
		try {
			int h = Integer.parseInt(tHeight.getText());
			return h;
		} catch (NumberFormatException e) {
			drop();
			return h;
		}
	}
}
