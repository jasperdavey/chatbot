import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
	ArrayList<Handler>chatters;
	ServerSocket ss;
	Socket s;
	static int port = 3000;
	
	public void connect(){
		chatters = new ArrayList<Handler>();	
		try{
			ss = new ServerSocket(port);
		}catch(IOException ioe){
			System.out.println("Server could not bind to port number " + port);
		}
		for(;;){
			try{
				s = ss.accept();
			}catch(IOException ioe){
				System.out.println("Problem accepting incoming connection.");
			}
			new Handler(s, chatters).start();
		}
	}

	public static void main(String[] args){
		if(args.length == 1){
			port = Integer.parseInt(args[0]);
		}
		Server server = new Server();
		server.connect();
	}
}
class Handler extends Thread{
	ArrayList<Handler>chatters;
	Iterator<Handler>iter;
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	String line;
	String name;
	boolean nameSet = false;
	
	public Handler(Socket s, ArrayList<Handler>chatters){
		this.s = s;
		this.chatters = chatters;
		chatters.add(this);
		try{
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
		}catch(IOException ioe){
			System.out.println("Problem with stream creation."); 
		}
	}
	public String getUserName(){
		return name;
	}
	public void remove(){
		chatters.remove(this);
	}
	public void run(){
		for(;;){
			try{
				line = br.readLine();
				if(line.startsWith("getnames")){
					getNames();
					continue;
				}else if(line.endsWith("has entered")){
					if(!nameSet){
						String[] temp = line.split(" ");
						name = temp[0];
						nameSet = true;
						//getNames();
						System.out.println(line);
						//continue;
				
					}
				}
			}catch(IOException ioe){
				System.out.println("Client terminated connection.");
				//System.out.println(chatters.size());
				remove();
				//System.out.println(chatters.size());
				break;
			}
			//getNames();
			broadcast(line);
		}
	
	}
	public void send(String message){
	
		pw.println(message);
	}
	public synchronized void getNames(){
		String names = "names";
		iter = chatters.iterator();
		while ( iter.hasNext() ){
			//System.out.println( iter.next() );
			Handler current = iter.next();
			names += ":" + current.getUserName();
		}
		send(names);
		
	}
	public synchronized void broadcast(String message){
		// Create an iterator for the list
		iter = chatters.iterator();

		// Use the iterator to visit each element
		while ( iter.hasNext() ){
			//System.out.println( iter.next() );
			Handler current = iter.next();
			current.send(message);
		}
	
	}
}