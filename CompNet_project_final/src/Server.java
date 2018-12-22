import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
    static ArrayList<String> MacAddress = new ArrayList<>(50);
    static ArrayList<String> IPAddress = new ArrayList<>(50);
    static ArrayList<String> portName = new ArrayList<>(50);
    public static TableGUI table = new TableGUI();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ServerSocket serverSocket = new ServerSocket(8080);
        int portSequence = 0;
        for (int i = 0; i < 70; i++) {
            MacAddress.add(i, "");
            IPAddress.add(i, "");
            portName.add(i, "");
        }
        System.out.println("\n");
        System.out.println("----------------- START SERVER PROGRAM ------------------");
        System.out.println("\n");

        while (true) {
            Socket socket = null;

            try {
                socket = serverSocket.accept();
                portSequence++;
                System.out.println("Notification : f0/" + portSequence + " connected.");
                DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
                Thread t = new ClientHandler(socket, dataInput, dataOutput, portSequence, MacAddress, IPAddress,portName);
                t.start();
            } catch (Exception e) {
                socket.close();
                e.printStackTrace();
            }
           /* String exitCommand = scanner.next();
            if (exitCommand.equalsIgnoreCase("exit")) {
                System.out.println("Server has closed.");
                System.exit(0);
            }*/
        }
    }
}


class ClientHandler extends Thread {

    boolean portCorrect = true;
    final DataInputStream dataInput;
    final DataOutputStream dataOutput;
    final Socket socket;
    ArrayList<String> IPAddress;
    ArrayList<String> MacAddress;
    ArrayList<String> portName;
    int portSequence;

    public ClientHandler(Socket socket, DataInputStream dataInput, DataOutputStream dataOutput, int portSequence,
                         ArrayList<String> MacAddress, ArrayList<String> IPAddress, ArrayList<String> portName) {
        this.socket = socket;
        this.dataInput = dataInput;
        this.dataOutput = dataOutput;
        this.portSequence = portSequence;
        this.MacAddress = MacAddress;
        this.IPAddress = IPAddress;
        this.portName = portName;
    }


    protected String getPortFromIP(String ip) {
        for (int i = 0; i < IPAddress.size(); i++) {
            if (IPAddress.get(i).equals(ip)) {
                return portName.get(i);
            }
        }
        return null;
    }

    @Override
    public void run() {
        String clientInput;
        Server.table = new TableGUI();
        while (true) {
            try {
                clientInput = dataInput.readUTF();
                String[] splitReceived = clientInput.split(" ");
                clientInput = splitReceived[0];
                if (portCorrect) {
                    String[] socket = clientInput.split("##");
                    IPAddress.add(portSequence, socket[1]);
                    MacAddress.add(portSequence, socket[0]);
                    portName.add(portSequence, Integer.toString(portSequence));
                    dataOutput.writeUTF("\n\t   Server Port is correct \n-----------   Connect successful   -----------\n");
                    System.out.println("--------------------------------------------------");
                    portCorrect = false;
                }
                if (clientInput.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4]"
                        + "[0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
                    if (IPAddress.contains(clientInput)) {
                        dataOutput.writeUTF("\nNotification : " + clientInput + " can found." + "\nMAC address: " + MacAddress.get(IPAddress.indexOf(clientInput)) + "\n");
                        System.out.println(clientInput + " found.");
                        Server.table.add(new TableStructure(MacAddress.get(portSequence), "f0/" + getPortFromIP(clientInput)));
                        Server.table.add(new TableStructure(MacAddress.get(IPAddress.indexOf(clientInput)), "f0/" + Integer.toString(portSequence)));
                        Server.table.displayTable();
                    }
                    else if (clientInput.equalsIgnoreCase("exit")) {
                        dataOutput.writeUTF("Client has exit the program.");
                        System.exit(0);
                    }
                    else {
                        System.out.println(clientInput + " not found.");
                        dataOutput.writeUTF("Notification :" + socket.getInetAddress() + " not found.");
                    }
                }
            } catch (IOException e) {
            }
        }
    }

}
