import java.awt.BorderLayout;
import java.awt.Dimension;
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
	private String defaultPassword = "default";
	private long hash = defaultPassword.hashCode();
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
							stringPane.output.setText(encrypt(stringPane.input.getText()));
						else if (last.equals("output"))
							stringPane.input.setText(decrypt(stringPane.output.getText()));
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
							output.setText(encrypt(input.getText()));
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
							input.setText(decrypt(output.getText()));
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
