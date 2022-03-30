import java.io.*;
import java.net.*;

public class MyClient {

    private static final String HELO = "HELO\n";
    private static final String AUTH = "AUTH USER\n";
    private static final String REDY = "REDY\n";
    private static final String QUIT = "QUIT\n";
  

    /*
     * public static void main(String[] args) {
     * try{
     * Socket s=new Socket("localhost",50000);
     * DataOutputStream dout=new DataOutputStream(s.getOutputStream());
     * dout.write(("HELO").getBytes());
     * dout.flush();
     * dout.close();
     * s.close();
     * }catch(Exception e){System.out.println(e);}
     * }
     * }
     */

    public static void main(String args[]) throws Exception {
        Socket s = new Socket("localhost", 50000);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        

        dout.write((HELO).getBytes());
        dout.flush();

        System.out.println(din.readLine());

        dout.write((AUTH).getBytes());
        dout.flush();
        
        System.out.println(din.readLine());

        dout.write((REDY).getBytes());
        dout.flush();

        String msg = din.readLine();
        System.out.println(msg);

        while(msg.equals("QUIT")){
            
        }
        
        System.out.println(din.readLine());

        dout.write((QUIT).getBytes());
        dout.flush();
        
        
        /*
        String str = "", str2 = "";
        while (!str.equals("stop")) {
            str = br.readLine();
            dout.write(("HELO").getBytes());
            dout.flush();
            str2 = din.readUTF();
            System.out.println("Server says: " + str2);
        }

        */




        dout.close();
        s.close();
    }

    public void GetsCapable (){

    }
}

public void findBiggest(){

}
