import java.io.*;
import java.net.*;
import java.util.*;

public class BotClient extends Thread{
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	String name;
	String incoming, response;

	public BotClient(){
		this("localhost", 3000);
	}

	public BotClient(String ip, int port){

		name = "Bot";

		try{
			s = new Socket(ip, port);


			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
			pw.println(name + " has entered");
			while((incoming = br.readLine()) != null){
				int colon = incoming.indexOf(":");
				if(colon!=-1){
					String tempincoming = incoming.substring(colon);
					String tempname = incoming.substring(0,colon);
					if(!tempname.equals("Bot")){
						pw.println(name + ": " + reply(incoming));
					}
				}
			}
		}catch(UnknownHostException uhe){
			System.out.println("Unknown Host Exception");
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());

		}
	}

	public static void main(String[] args){

		if(args.length == 2){

			BotClient bc = new BotClient(args[0], Integer.parseInt(args[1]));
		}else{

			BotClient sc = new BotClient();
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////

	public String reply(String input){

		//To Do: Add your magic here. You don't need to change any other code but this.
		System.out.println(input); //uncomment this line for debugging.

		String temp = input.toLowerCase();

		Boolean desiresIphone = false;
		Boolean desiresIpad = false;
		Boolean desiresMac = false;
		Boolean productPurchaseIphone = false;
		Boolean productPurchaseIpad = false;
		Boolean productPurchaseMac = false;
		// Greetings
		ArrayList<String> greetings = new ArrayList<String>();
		greetings.add("hello");
		greetings.add("hey");
		greetings.add("hi");

		int counter = 0;
		while ( counter < greetings.size() )
		{
			Boolean found = Arrays.asList(temp.split(" ")).contains(greetings.get(counter));
			if (found)
			{
				temp = "Hello, thank you for contacting Apple Support, how may I help you?";
				System.out.println( temp );
			}
			counter++;
		}

		ArrayList<String> products = new ArrayList<String>();
		products.add("iphone");
		products.add("mac");
		products.add("ipad");
		products.add("computer");
		products.add("tablet");
		products.add("phone");

		ArrayList<String> helpOptionsPurchase = new ArrayList<String>();
		helpOptionsPurchase.add("buy");
		helpOptionsPurchase.add("get");
		helpOptionsPurchase.add("purchase");

		ArrayList<String> carriers = new ArrayList<String>();
		carriers.add("verizon");
		carriers.add("sprint");
		carriers.add("att");
		carriers.add("t-mobile");

		ArrayList<String> agree = new ArrayList<String>();
		agree.add("yes");
		agree.add("yeah");
		agree.add("yep");
		if (desiresIphone)
		{
			counter = 0;
			while ( counter < agree.size() )
			{
				Boolean found = Arrays.asList(temp.split(" ")).contains(carriers.get(counter));
				if (found)
				{
					temp = "Perfect we'll ship you an iPhone the next business day.";
					System.out.println(temp);
				}
			}
		}


		if (productPurchaseIphone)
		{
			counter = 0;
			while ( counter < carriers.size())
			{
				Boolean found = Arrays.asList(temp.split(" ")).contains(carriers.get(counter));
				if (found)
				{
					if ( carriers.get(counter) == "verizon" )
					{
						temp = "Perfect, your Verizon iPhone will be shipped within the next business day. The payment account on your record will be charged";
						System.out.println(temp);
					}
					else if ( carriers.get(counter) == "sprint" )
					{
						temp = "Perfect, your Sprint iPhone will be shipped within the next business day. The payment account on your record will be charged";
						System.out.println(temp);
					}
					else if ( carriers.get(counter) == "att" )
					{
						temp = "Perfect, your AT&T iPhone will be shipped within the next business day. The payment account on your recrod will be charged";
						System.out.println(temp);
					}
					else if ( carriers.get(counter) == "t-mobile" )
					{
						temp = "Perfect, your AT&T iPhone will be shipped within the next business day. THe payment account on your record will be charged";
						System.out.println(temp);
					}
					else
					{
						temp = "I'm sorry I don't know that carrier. Please try Verizon, AT&T, Sprint, or T-Mobile.";
						System.out.println(temp);
					}
				}
			}
		}



		counter = 0;
		while ( counter < greetings.size() )
		{
			Boolean found = Arrays.asList(temp.split(" ")).contains(helpOptionsPurchase.get(counter));
			if (found)
			{
				counter = 0;
				while ( counter < products.size() )
				{
					Boolean foundProduct = Arrays.asList(temp.split(" ")).contains(products.get(counter));
					if (foundProduct)
					{
						if (products.get(counter) == "iphone" || products.get(counter) == "phone")
						{
							temp = "Sure, I can help you get an iPhone today. To start, who's your cellular carrier?";
							productPurchaseIphone = true;
						}
						else if (products.get(counter) == "ipad" || products.get(counter) == "tablet")
						{
							temp = "Sure, I can help you get an iPad today. Which size iPad would you like?";
							productPurchaseIpad = true;
						}
						else if (products.get(counter) == "mac" || products.get(counter) == "computer")
						{
							temp = "Sure, I can help you get a Mac today. Which model would you like?";
							productPurchaseMac = true;
						}
					}
					else
					{
						temp = "We sell the iPhone, iPad, and Mac. Which one would you like to buy?";
					}
					counter++;
				}
			}
			counter++;
		}

		ArrayList<String> helpOptionsPrice = new ArrayList<String>();
		helpOptionsPrice.add("much");
		helpOptionsPrice.add("price");

		counter = 0;
		while ( counter < greetings.size() )
		{
			Boolean found = Arrays.asList(temp.split(" ")).contains(helpOptionsPrice.get(counter));
			if (found)
			{
				counter = 0;
				while ( counter < products.size() )
				{
					Boolean foundProduct = Arrays.asList(temp.split(" ")).contains(products.get(counter));
					if (foundProduct)
					{
						if (products.get(counter) == "iphone" || products.get(counter) == "phone")
						{
							temp = "The newest iPhone is $649. Can I help you with your purchase?";
							desiresIphone = true;
						}
						else if (products.get(counter) == "ipad" || products.get(counter) == "tablet")
						{
							temp = "The newest iPad is $799. Can I help you with your purchase?";
							desiresIpad = true;
						}
						else if (products.get(counter) == "mac" || products.get(counter) == "computer")
						{
							temp = "The newest Mac is $1,299. Can I help you with your purchase?";
							desiresMac = true;
						}
					}
					else
					{
						temp = "The iPhone is $649, the iPad is $799, and the Mac is $1,299.";
					}

					counter++;
				}
			}
			counter++;
		}

		String output = temp;

		return output;
	}


}
