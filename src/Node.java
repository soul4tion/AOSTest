import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

//Object to reprsents a node in the distributed system
class Node
{
	// node identifier
	private NodeID identifier;

	//
	private Socket sc = null;

	// constructor
	public Node(NodeID identifier, String configFile, Listener listener)
	{
		//Your code goes here
		// for Test
		int listenPort = 5056;
		int destPort = 5057;
		String destHost = "localhost";

		//
		try {
			//Get Connection
			Thread tt = new ConnectToNeighbor(sc, destHost, destPort);
			tt.start();

			//Listen
			ServerSocket ss = new ServerSocket(listenPort);

			while (true) {
				Socket s = null;

				try {
					s = ss.accept();
					System.out.println("A new client is connect : " + s);

					DataInputStream dis = new DataInputStream(s.getInputStream());
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					Thread t = new ClientHandler(s, dis, dos, listener);
					t. start();
				}
				catch (Exception e) {
					s.close();
					e.printStackTrace();
				}
			}

		} catch (Exception e) {

		}
	}

	// methods
	public NodeID[] getNeighbors()
	{
		//Your code goes here
		return null;
	}

	public void send(Message message, NodeID destination)
	{
		//Your code goes here
		try {
			if(sc != null) {
				DataOutputStream dos = new DataOutputStream(sc.getOutputStream());
				dos.writeUTF("send");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendToAll(Message message)
	{
		//Your code goes here
	}
	
	public void tearDown()
	{
		//Your code goes here
	}
}


class ClientHandler extends Thread {

	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	final Listener l;

	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, Listener listener) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
		this.l = listener;

		l.receive(null);
	}

	public void run() {
		String received;

		while(true) {
			try {
				dos.writeUTF("Connected");
				received = dis.readUTF();

				//TODO: received -> Message
				l.receive(null);

				if (received.equals("Exit")) {
					this.s.close();
					System.out.println("Connection Closed");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			this.dis.close();
			this.dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


class ConnectToNeighbor extends Thread {
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	Socket sc;
	String host;
	int port;

	public ConnectToNeighbor(Socket sc, String destHost, int destPort) {
		this.sc = sc;
		this.host = destHost;
		this.port = destPort;
	}

	public void run() {
		boolean isConnect = false;
		while(!isConnect) {
			try {
				InetAddress ip = InetAddress.getByName(host);
				Socket s = new Socket(ip, port);
				isConnect = true;
			} catch (Exception e) {
				Date date = new Date();
				System.out.println("Connect failed, waiting and trying again after 5 sec, " + fortime.format(date));
				try {
					Thread.sleep(5000);//5 seconds
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}
}