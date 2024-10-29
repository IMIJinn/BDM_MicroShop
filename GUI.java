import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class GUI {

	private JFrame mainWindow;
	private JMenuBar menuBar;
	private JMenu menuFile, menuEdit, menuAbout;
	private JMenuItem itemOpen, itemNew, itemSave, itemClose;
	private JMenuItem itemCut, itemCopy, itemPaste, itemSelect;
	private JPanel contentPanel, buttonsPanel;
    private JLabel message;
    private int guiWidth, guiHeight;
    
    JFileChooser fileChooser;
    
    private ImagePanel picturePanel;
	
	public GUI(String name, int width, int height) {
		
		// Get the current screen's size using AWT
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		guiWidth = width;
		guiHeight = height;
		
		// Create an app window, called "frame" in Java
		mainWindow = new JFrame(name);
//		mainWindow.setLayout(new GridLayout(1, 2)); // if needed
		mainWindow.setSize(width + width/2, height);
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// Without the last one, the window closes but the application stays active!
		
		// Create a menu bar for the window
		menuBar = new JMenuBar();
		
		// Create menu buttons for the bar
		menuFile = new JMenu("File");
		menuEdit = new JMenu("Edit");
		menuAbout = new JMenu("About");
		
		// Add menu buttons to the bar
		menuBar.add(menuFile);
		menuBar.add(menuEdit);
		menuBar.add(menuAbout);
		
		// Create items for a "file" menu button
		itemOpen = new JMenuItem("Open");
		itemNew = new JMenuItem("New");
		itemSave = new JMenuItem("Save");
		itemClose = new JMenuItem("Close");
		
		// Add functions to the items
		itemOpen.addActionListener(event -> openFile());
		itemNew.addActionListener(event -> showText(itemNew.getText()));
		itemSave.addActionListener(event -> saveFile());
		itemClose.addActionListener(event -> showText(itemClose.getText()));
		
		// Add items to the "edit" menu
		menuFile.add(itemOpen);
		menuFile.add(itemNew);
		menuFile.add(itemSave);
		menuFile.add(itemClose);
		
		// Second menu items, same structure
		itemCut = new JMenuItem("Cut");
		itemCopy = new JMenuItem("Copy");
		itemPaste = new JMenuItem("Paste");
		itemSelect = new JMenuItem("Select All");
		
		itemCut.addActionListener(event -> showText(itemCut.getText()));
		itemCopy.addActionListener(event -> showText(itemCopy.getText()));
		itemPaste.addActionListener(event -> showText(itemPaste.getText()));
		itemSelect.addActionListener(event -> showText(itemSelect.getText()));
		
		menuEdit.add(itemCut);
		menuEdit.add(itemCopy);
		menuEdit.add(itemPaste);
		menuEdit.add(itemSelect);
		
		// Don't forget about the menu button without items
		menuAbout.addMenuListener(new EmptyMenuListener());
		
        // Function buttons
        buttonsPanel = new JPanel();
//        buttonsPanel.setBackground(Color.red);
        buttonsPanel.setBounds(0, 0, width/2, height);
//        buttonsPanel.setLayout(new GridLayout(4, 1));
        JButton but1 = new JButton("but1");
        JButton but2 = new JButton("but2");
        JButton but3 = new JButton("but3");
        JButton but4 = new JButton("but4");
        buttonsPanel.add(but1);
        buttonsPanel.add(but2);
        buttonsPanel.add(but3);
        buttonsPanel.add(but4);
		
		// The actual content panel
		picturePanel = new ImagePanel();
//		picturePanel.setBackground(Color.blue);
		picturePanel.setBounds(width/2, 0, width, height);
		message = new JLabel("content window message");
		picturePanel.add(message);
        
        // put it all together
		mainWindow.add(menuBar);
		mainWindow.add(buttonsPanel);
		mainWindow.add(picturePanel);
		mainWindow.setJMenuBar(menuBar);
		// make the complete window visible
		mainWindow.setVisible(true);
		
		fileChooser = new JFileChooser();
	    fileChooser.setFileFilter(new FileNameExtensionFilter("Image files (jpg, png ...)", "jpg", "jpeg", "png", "gif", "bmp"));
	    fileChooser.setAcceptAllFileFilterUsed(false);
	}
	
	private void showText(String text) {
		message.setText("menu item \"" + text + "\" clicked.");
	}
	
	private void openFile() {

        switch (fileChooser.showOpenDialog(null)) {
		case JFileChooser.APPROVE_OPTION:
			picturePanel.setImage(fileChooser.getSelectedFile());
			mainWindow.repaint();
			break;
		case JFileChooser.CANCEL_OPTION:
			System.out.println("Opening canceled");
			break;
		case JFileChooser.ERROR_OPTION:
			System.out.println("error");
		}
	}
	
	private void saveFile() {
        switch (fileChooser.showSaveDialog(null)) {
		case JFileChooser.APPROVE_OPTION:
			try {
				ImageIO.write(picturePanel.getImage(), "png", fileChooser.getSelectedFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case JFileChooser.CANCEL_OPTION:
			System.out.println("Saving canceled");
			break;
		case JFileChooser.ERROR_OPTION:
			System.out.println("error");
		}
	}
	
	/**
	 * Empty menus need special listeners that need
	 * to be specifically implemented as their own class.
	 * This is done in an internal private class -
	 * as it is only needed here.
	 */
	private class EmptyMenuListener implements MenuListener {

	    @Override
	    public void menuSelected(MenuEvent e) {
	    	JFrame about = new JFrame("About Window");
	    	about.setSize(160, 120);

            JPanel stuff = new JPanel();
            stuff.setBounds(0, 0, 160, 120);
            
            JLabel text = new JLabel("About Window");
            stuff.add(text);
            
            about.add(stuff);
            about.setLocationRelativeTo(null);
            about.setVisible(true);
	    }

	    @Override
	    public void menuDeselected(MenuEvent e) {
	    }

	    @Override
	    public void menuCanceled(MenuEvent e) {
	    }
	}
	
	/**
	 * Private inner class to handle image display in a panel
	 * accoding to <a href="#{@link}">{@link https://www.w3docs.com/snippets/java/how-to-add-an-image-to-a-jpanel.html}</a>
	 */
	private class ImagePanel extends JPanel {
		
		private BufferedImage image;

		public BufferedImage getImage() {
            return image;
        }
		
		public void setImage(File file) {
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        if (image != null) {
	            g.drawImage(image, guiWidth/2, 0, this.getWidth()-guiWidth/2, this.getHeight(), this);
	        }
	    }
	}
}