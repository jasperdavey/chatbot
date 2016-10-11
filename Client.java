import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;

public class Client extends JFrame implements Runnable, ActionListener{
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	JTextArea ta;
	JTextField tf;
	JButton button;
	JList<String> list;
	Thread t;
	String line;
	String name;
	DefaultListModel<String> listModel;
	JScrollPane scrollPane;
	String ip;
	int port;
		
	public Client(){
		this("localhost", 3000);
	}	
		
	public Client(String ip, int port){
		this.ip = ip;
		this.port = port;
		setSize(600,600);
		setTitle("Chat Client");
		setUIFont (new javax.swing.plaf.FontUIResource("SansSerif",Font.BOLD,16));
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				pw.println(name + " has exited");
				System.exit(0);
			}
		});
		tf = new JTextField();
		tf.addActionListener(this);
		tf.setText("Type your name here before connecting.");
		tf.selectAll();
		getContentPane().add(tf, BorderLayout.NORTH);
		ta = new JTextArea();
		getContentPane().add(ta, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		button = new JButton("Connect");
		button.addActionListener(this);
		buttonPanel.add(button);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		listModel = new DefaultListModel<String>();
		listModel.addElement("   Chatters   ");
		list = new JList<String>(listModel);
		scrollPane = new JScrollPane(list);
		

		list.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		ta.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		getContentPane().add(scrollPane, BorderLayout.WEST);
		setVisible(true);
	}
	public static void setUIFont (javax.swing.plaf.FontUIResource f){
    java.util.Enumeration keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get (key);
      if (value != null && value instanceof javax.swing.plaf.FontUIResource)
        UIManager.put (key, f);
      }
    } 
	
	public void connect(){
		try{
			s = new Socket(ip, port);
		
		
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
			t = new Thread(this);
			t.start();
			
			
		}catch(UnknownHostException uhe){
			System.out.println("Unknown Host Exception");
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		
		}
	}
	protected boolean alreadyInList(String name) {
            return listModel.contains(name);
    }

	
	public void run(){
		try{
			while(t != null){
				line = br.readLine();
				if(line.startsWith("names")){
					//ta.append(line + "   from names \n");
					String[] temp = line.split(":");
					for(int i = 1; i < temp.length; i++){
						if(!alreadyInList(temp[i])){
							listModel.addElement(temp[i]);
						}
					}
						//split and add to list
				}else if(line.endsWith("has entered")){
					pw.println("getnames");
					ta.append(line + "\n");
					String[] temp = line.split(":");
					for(int i = 1; i < temp.length; i++){
						if(!alreadyInList(temp[i])){
							listModel.addElement(temp[i]);
						}
					}
					
					
				}else if(line.endsWith("has exited")){
					ta.append(line + "\n");
					String[] temp = line.split(" ");
					for(int i = 0; i < listModel.getSize(); i++){
						if(	listModel.get(i).equals(temp[0].trim())){
							 listModel.remove(i);
						}
					}
				}else{
					pw.println("getnames");
					ta.append(line + "\n");
				}
			}
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
		
	}

	public void actionPerformed(ActionEvent ae){
		
		if(ae.getSource() == button){
			if(ae.getActionCommand().equals("Connect")){
				if(name == null){
					tf.setText("You must enter your name before connecting.");
					tf.selectAll();
				}else{
					connect();
					pw.println(name + " has entered");
					pw.println("getnames");
					tf.setText("");
					button.setText("Disconnect");
					button.setActionCommand("Disconnect");
				}
			}else{
				pw.println(name + " has exited");
				System.out.println(name + " has exited");
				System.exit(0);
			}
		}else if(ae.getSource() == tf){
			if(t == null){
				name = tf.getText();
				tf.setText("You must connect.");
				tf.selectAll();
			}else{
				pw.println(name + ": " + tf.getText());
				tf.setText("");
			}
		}
		

	}
		
	public static void main(String[] args){
	
		if(args.length == 2){
			Client c = new Client(args[0], Integer.parseInt(args[1]));
		}else{
			Client c = new Client();
		}
		
	}
}