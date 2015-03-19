import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class Encyptor extends JFrame{
	
	private StringPane stringPane = new StringPane();
	private FilePane filePane = new FilePane();
	private String tab = "String";
	private final JPanel panel = new JPanel();
	private final JLabel lblPassword = new JLabel("Password:");
	private final JTextField txtPassword = new JTextField();
	private final JLabel lblHash = new JLabel("Hash:");
	private final JTextField textField_1 = new JTextField();
	private String defaultPassword = "password";
	private long hash = defaultPassword.hashCode();

	public Encyptor() {
		textField_1.setText(hash + "");
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		txtPassword.setText("password");
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
					}
				});
				
			}

		});
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.add("String", stringPane);
		tabbedPane.add("File", filePane);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tab = ((JTabbedPane) e.getSource()).getTitleAt(((JTabbedPane) e.getSource()).getSelectedIndex());
			}
		});
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		panel.add(lblPassword);
		panel.add(txtPassword);
		panel.add(lblHash);
		panel.add(textField_1);
		
		this.setTitle("String and File Encyptor");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(400,400);
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
							output.setText(encrypt(input.getText()));
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
							input.setText(decrypt(output.getText()));
						}
					});
				}
				
			});
			output.setBackground(new Color(240,240,240));
			
			split = new JPanel();
			GridLayout layout = new GridLayout(1,2);
			layout.setHgap(10);
			split.setLayout(layout);
			JScrollPane inputScroll = new JScrollPane(input);
			inputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			split.add(inputScroll);
			JScrollPane outputScroll = new JScrollPane(output);
			outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			split.add(outputScroll);
			
			this.setLayout(new GridLayout());
			this.add(split);
		}
		
		
		
	}
	
	class FilePane extends JPanel{
		
		public FilePane() {

		}
		
	}
	
	private String encrypt(String in){
		String out = "";
		
		for (char c : in.toCharArray()){
			int temp = (int) (c + (hash % 255));
			out += (char) (temp > 255 ? 255 - temp : temp);
		}
		
		return out;
	}
	
	private String decrypt(String in){
		String out = "";
		
		for (char c : in.toCharArray()){
			int temp = (int) (c - (hash % 255));
			out += (char) (temp < 0 ? 255 + temp : temp);
		}
		
		return out;
	}

}
