import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5057);

        while (true) {
            Socket s = null;

            try {
                s = ss.accept();

                System.out.println("A new client is connect : " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                Thread t = new ClientHandler2(s, dis, dos);
                t. start();
            }
            catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler2 extends Thread {
    DateFormat fordate = new SimpleDateFormat("yyyy/mmd/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    public ClientHandler2(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public void run() {
        String received;
        String toreturn;

        while(true) {
            try {
                dos.writeUTF("What do you want?");

                received = dis.readUTF();

                if (received.equals("Exit")) {
                    this.s.close();
                    System.out.println("Connection Closed");
                    break;
                }

                Date date = new Date();
                switch(received) {
                    case "Date" :
                        toreturn = fordate.format(date);
                        System.out.println("Date returned");
                        dos.writeUTF(toreturn);
                        break;

                    case "Time" :
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    default :
                        dos.writeUTF("Invalid Input");
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
