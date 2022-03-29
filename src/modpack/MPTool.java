package modpack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class MPTool extends JFrame {

	private JPanel contentPane;
	private Path src;
	private Path dest;
	private String[] modids;
	private String srcString;
	private Path modFolderPath;
	private static JButton startButton;
	private static JLabel statusLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MPTool frame = new MPTool();
					frame.setVisible(true);
					startButton.setEnabled(false);
					statusLabel.setText("Status: Waiting on input");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MPTool() {
		setResizable(false);
		setTitle("Stellaris Modpack Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 601, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		statusLabel = new JLabel("Status:");
		statusLabel.setBounds(186, 197, 399, 14);
		contentPane.add(statusLabel);
		
		JLabel modFolderLabel = new JLabel("Workshop Mod Folder:");
		modFolderLabel.setBounds(105, 15, 480, 14);
		contentPane.add(modFolderLabel);
		
		JLabel outputFolderLabel = new JLabel("Output Folder:");
		outputFolderLabel.setBounds(105, 49, 480, 14);
		contentPane.add(outputFolderLabel);
		
		JTextArea modidsTextArea = new JTextArea();
		modidsTextArea.setBounds(10, 117, 166, 135);
		contentPane.add(modidsTextArea);
		
		JButton selectModFolderBtn = new JButton("Select");
		selectModFolderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            JFileChooser chooser = new JFileChooser();
	            chooser.setCurrentDirectory(new java.io.File("C:\\Program Files (x86)\\Steam\\steamapps\\workshop\\content\\281990"));
	            chooser.setDialogTitle("Select workshop mods folder");
	            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            chooser.setAcceptAllFileFilterUsed(false);

	            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	                srcString = chooser.getSelectedFile().toString();
	                src = chooser.getSelectedFile().toPath();
	                modFolderLabel.setText("Workshop Folder: " + chooser.getSelectedFile().toString());
	                modFolderLabel.setToolTipText(chooser.getSelectedFile().toString());
	            } else {
	                System.out.println("No Selection ");
	            }
	            
	            //status update
	            if(src == null || dest == null) {
	            	startButton.setEnabled(false);
	            }else {
	            	startButton.setEnabled(true);
					statusLabel.setText("Status: Ready!");
	            }
			}
		});
		selectModFolderBtn.setBounds(10, 11, 85, 23);
		contentPane.add(selectModFolderBtn);
		
		JButton selectOutputFolderBtn = new JButton("Select");
		selectOutputFolderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            JFileChooser chooser = new JFileChooser();
	            chooser.setCurrentDirectory(new java.io.File(FileSystemView.getFileSystemView().getDefaultDirectory().toString() + "\\Paradox Interactive\\Stellaris\\mod"));
	            chooser.setDialogTitle("Select output mod folder");
	            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            chooser.setAcceptAllFileFilterUsed(false);

	            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	                dest = chooser.getSelectedFile().toPath();
	                outputFolderLabel.setText("Output Folder: " + chooser.getSelectedFile().toString());
	                outputFolderLabel.setToolTipText(chooser.getSelectedFile().toString());
	            } else {
	                System.out.println("No Selection ");
	            }
	            
	            //status update
	            if(src == null || dest == null) {
	            	startButton.setEnabled(false);
	            }else {
	            	startButton.setEnabled(true);
					statusLabel.setText("Status: Ready!");
	            }
			}
		});
		selectOutputFolderBtn.setBounds(10, 45, 85, 23);
		contentPane.add(selectOutputFolderBtn);
		
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						
						startButton.setEnabled(false);
						
						if (modidsTextArea.getText().length() > 0) {
							
							modids = modidsTextArea.getText().split("\n");
							
							for(int i = 0; i < modids.length; i++) {
								statusLabel.setText("Status: Copying mod " + (i + 1) + " out of " + modids.length);
								String modFolderPathString = srcString + "\\" + modids[i];
								modFolderPath = Paths.get(modFolderPathString);
								try {
									try {
										Stream<Path> files = Files.walk(modFolderPath);
										
										files.forEach(file ->{
											
											if(!file.getFileName().toString().contains(".mod")) {
												try {
										            Files.copy(file, dest.resolve(modFolderPath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
												} catch(IOException e2) {
													e2.printStackTrace();
												}
											}
										});
										
										files.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}catch (InvalidPathException balls) {
									balls.printStackTrace();
								}
							}
							
							JOptionPane.showMessageDialog(new JFrame(), "Finished!");
							statusLabel.setText("Status: Waiting on input");
						}else {
							JOptionPane.showMessageDialog(new JFrame(), "Put in some mod IDs before starting!");
							statusLabel.setText("Status: Waiting on input");
						}
						
						startButton.setEnabled(true);
						
						
						
					}
					
				});
				thread.start();
				
				
			}
		});
		startButton.setBounds(186, 163, 85, 23);
		contentPane.add(startButton);
		
		JLabel lblNewLabel = new JLabel("Mod IDs");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(10, 92, 47, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnGetFrmJson = new JButton("Get from JSON");
		btnGetFrmJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String jsonlocation = "";
				JFileChooser chooser = new JFileChooser();
	            chooser.setCurrentDirectory(new java.io.File("C:\\"));
	            chooser.setDialogTitle("Select json file");
	            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	            chooser.setAcceptAllFileFilterUsed(false);

	            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	                jsonlocation = chooser.getSelectedFile().toPath().toString();
	            } else {
	                System.out.println("No Selection ");
	            }
	            
	            JSONParser parser = new JSONParser();
	            try {
					JSONObject a = (JSONObject) parser.parse(new FileReader(jsonlocation));
					JSONArray mods = (JSONArray) a.get("mods");
					
					for (Object o : mods) {
						JSONObject mod = (JSONObject) o;
						modidsTextArea.append((String) mod.get("steamId") + "\n");
					}
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnGetFrmJson.setBounds(57, 88, 119, 23);
		contentPane.add(btnGetFrmJson);
		
		
	}
}
