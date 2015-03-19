import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class Encyptor extends JFrame{
	
	private StringPane stringPane = new StringPane();
	private FilePane filePane = new FilePane();
	private final JPanel panel = new JPanel();
	private final JLabel lblPassword = new JLabel("Password:");
	private final JTextField txtPassword = new JTextField();
	private final JLabel lblHash = new JLabel("Hash:");
	private final JTextField textField_1 = new JTextField();
	private String defaultPassword = "default";
	private long hash = Math.abs(defaultPassword.hashCode());
	private String last = "";

	public Encyptor() {
		textField_1.setText(hash + "");
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		txtPassword.setText(defaultPassword);
		txtPassword.setColumns(10);
		txtPassword.addKeyListener(new KeyAdapter() {
		 
			@Override
			public void keyTyped(KeyEvent e) {
				
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						hash = Math.abs(txtPassword.getText().hashCode());
						textField_1.setText(hash + "");
						say(txtPassword.getText() + " = " + textField_1.getText());
						
						if (last.equals("input"))
							stringPane.output.setText(stringEncrypt(stringPane.input.getText(), hash));
						else if (last.equals("output"))
							stringPane.input.setText(stringDecrypt(stringPane.output.getText(), hash));
					}
				});
				
			}

		});
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.add("String", stringPane);
		tabbedPane.add("File", filePane);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		panel.add(lblPassword);
		panel.add(txtPassword);
		panel.add(lblHash);
		panel.add(textField_1);
		
		this.setTitle("String and File Encyptor");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(400,400));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
	
	public static void say(Object s){
		System.out.println(s);
	}

	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){};
		
		new Encyptor();
	}
	
	class StringPane extends JPanel{
		
		private JPanel split;
		private JTextArea input = new JTextArea();
		private JTextArea output = new JTextArea();

		public StringPane() {
			
			input.setLineWrap(true);
			input.addKeyListener(new KeyAdapter() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							output.setText(stringEncrypt(input.getText(), hash));
							last = "input";
						}
					});
				}
				
			});
			output.setLineWrap(true);
			output.addKeyListener(new KeyAdapter() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					EventQueue.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							input.setText(stringDecrypt(output.getText(), hash));
							last = "output";
						}
					});
				}
				
			});
			//output.setBackground(new Color(240,240,240));
			
			split = new JPanel();
			GridLayout layout = new GridLayout(1,2);
			layout.setHgap(10);
			split.setLayout(layout);
			
			JScrollPane inputScroll = new JScrollPane(input);
			inputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			JPanel inputContainer = new JPanel();
			inputContainer.setLayout(new BorderLayout());
			inputContainer.add(inputScroll);
			inputContainer.add(new JLabel("Encrypt:"), BorderLayout.NORTH);
			
			split.add(inputContainer);
			
			JScrollPane outputScroll = new JScrollPane(output);
			outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			JPanel outputContainer = new JPanel();
			outputContainer.setLayout(new BorderLayout());
			outputContainer.add(outputScroll);
			outputContainer.add(new JLabel("Decrypt:"), BorderLayout.NORTH);
			
			split.add(outputContainer);
			
			this.setLayout(new GridLayout());
			this.add(split);
		}		
		
	}
	
	public class FilePane extends JPanel{
		private JTextField source;
		private JTextField textField_1;
		private JTextField destination;

		public FilePane() {
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(new EmptyBorder(10, 5, 0, 5));
			this.add(panel_1);
			panel_1.setLayout(new BorderLayout(0, 0));
			
			JLabel lblFile = new JLabel("Source:");
			panel_1.add(lblFile, BorderLayout.WEST);
			
			source = new JTextField();
			source.setText("C:\\");
			panel_1.add(source, BorderLayout.CENTER);
			source.setColumns(10);
			
			JButton btnBrowse = new JButton("Browse");
			btnBrowse.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser();
					int choice = chooser.showOpenDialog(Encyptor.this);
					
					if (choice == JFileChooser.APPROVE_OPTION){
						source.setText(chooser.getSelectedFile().getPath());
					}
				}
			});
			panel_1.add(btnBrowse, BorderLayout.EAST);
			
			JPanel panel_4 = new JPanel();
			panel_4.setBorder(new EmptyBorder(10, 5, 10, 5));
			this.add(panel_4);
			panel_4.setLayout(new BorderLayout(0, 0));
			
			JLabel lblDestination = new JLabel("Destination:");
			panel_4.add(lblDestination, BorderLayout.WEST);
			
			JButton btnBrowse_1 = new JButton("Browse");
			btnBrowse_1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser();
					int choice = chooser.showOpenDialog(Encyptor.this);
					
					if (choice == JFileChooser.APPROVE_OPTION){
						destination.setText(chooser.getSelectedFile().getPath());
					}
				}
			});
			panel_4.add(btnBrowse_1, BorderLayout.EAST);
			
			destination = new JTextField();
			destination.setText("C:\\");
			panel_4.add(destination, BorderLayout.CENTER);
			destination.setColumns(10);
			
			JPanel panel_2 = new JPanel();
			this.add(panel_2);
			
			JLabel lblPassword = new JLabel("Password:");
			panel_2.add(lblPassword);
			
			textField_1 = new JTextField();
			textField_1.setText(defaultPassword);
			panel_2.add(textField_1);
			textField_1.setColumns(10);
			
			JPanel panel_3 = new JPanel();
			this.add(panel_3);
			
			JButton btnEncrypt = new JButton("Encrypt");
			btnEncrypt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					encryptFile(new File(source.getText()), new File(destination.getText()), Math.abs(textField_1.getText().hashCode()));
				}
			});
			panel_3.add(btnEncrypt);
			
			JButton btnDecrypt = new JButton("Decrypt");
			btnDecrypt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					decryptFile(new File(source.getText()), new File(destination.getText()), Math.abs(textField_1.getText().hashCode()));
				}
			});
			panel_3.add(btnDecrypt);
			
			JPanel panel_5 = new JPanel();
			this.add(panel_5);
			panel_5.setLayout(new BorderLayout(0, 0));
			panel_5.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		}

	}
	
	private String stringEncrypt(String in, long hash){
		String out = "";
		
		for (char c : in.toCharArray()){
			int temp = (int) (c + (hash % 255));
			out += (char) (temp > 255 ? 255 - temp : temp);
			say(c + " --> " + (temp > 255 ? 255 - temp : temp));
		}
		
		return out;
	}
	
	private String stringDecrypt(String in, long hash){
		String out = "";
		
		for (char c : in.toCharArray()){
			int temp = (int) (c - (hash % 255));
			out += (char) (temp < 0 ? 255 + temp : temp);
		}
		
		return out;
	}
	
	private byte byteDecrypt(byte b, int offset){
		int temp = b - offset;
		if (temp < Byte.MIN_VALUE) temp += Byte.MAX_VALUE - Byte.MIN_VALUE;
		return (byte) temp;
	}
	
	
	private byte byteEncrypt(byte b, int offset){
		int temp = b + offset;
		if (temp > Byte.MAX_VALUE) temp -= Byte.MAX_VALUE - Byte.MIN_VALUE;
		return (byte) temp;
	}
	
	
	
	private void decryptFile(File source, File dest, long hash){
		
		try {
			
			long fileSize = source.length();
			long total = 0;
			int offset = (int) hash % 255;
			
			FileInputStream input = new FileInputStream(source);
			FileOutputStream output = new FileOutputStream(dest);
			
			do{
				
				byte[] buffer = new byte[100];
				int count = input.read(buffer);
				total += count;
				
				for (int i=0; i < count; i++){
					output.write(byteDecrypt(buffer[i], offset));
				}
				
				try{
					say(total/(double)fileSize * 100 + "%");
				}catch(Exception e){e.printStackTrace();}
				
			}while (input.available() > 0);
				
			output.close();
			input.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void encryptFile(File source, File dest, long hash){
		
		try {
			
			long fileSize = source.length();
			long total = 0;
			int offset = (int) hash % 255;
			
			FileInputStream input = new FileInputStream(source);
			FileOutputStream output = new FileOutputStream(dest);
		
			do{
				
				byte[] buffer = new byte[100];
				int count = input.read(buffer);
				total += count;
				
				for (int i=0; i < count; i++){
					output.write(byteEncrypt(buffer[i], offset));
				}
				
				try{
					say(total/(double)fileSize * 100 + "%");
				}catch(Exception e){e.printStackTrace();}
				
			}while (input.available() > 0);
			
			output.close();
			input.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
