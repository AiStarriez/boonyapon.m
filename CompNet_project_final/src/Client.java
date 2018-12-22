import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class Client {
    public static String IPSwitch;
    public static String PortSwitch;
    public static void main(String[] args) throws IOException {
        System.out.println("\n");
        System.out.println("--------------- START CLIENT PROGRAM ----------------");
        System.out.println("\n");

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Server IP >> ");
            IPSwitch = scanner.next();
            System.out.println("\nServer Port >> ");
            PortSwitch = scanner.next();

            InetAddress serverIP = InetAddress.getByName(IPSwitch);

            Socket socketIO = new Socket(serverIP, Integer.parseInt(PortSwitch));

            DataInputStream dataInput = new DataInputStream(socketIO.getInputStream());
            DataOutputStream dataOutput = new DataOutputStream(socketIO.getOutputStream());

            dataOutput.writeUTF(GetInfo.GetAddress("mac") + "##" + GetInfo.GetAddress("ip"));

            String msg;
            String[] checkString = new String[101];

            System.out.println(dataInput.readUTF());

            while (true) {
                System.out.print("Ping >> ");
                msg = scanner.next();
                checkString = msg.split(" ");

//				if (checkString[0].equals("ping")) {
                if (checkString.length != 0) {

                    if (isValidIp(checkString[0])) {
                        StringBuilder sb = new StringBuilder();

                        try {
                            serverIP = InetAddress.getLocalHost();
                            NetworkInterface ni = NetworkInterface.getByInetAddress(serverIP);
                            byte[] MacAd = ni.getHardwareAddress();

                            for (int i = 0; i < MacAd.length; i++) {
                                sb.append(String.format("%02X%s", MacAd[i], (i < MacAd.length - 1) ? "-" : ""));
                            }

                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (SocketException e) {
                            e.printStackTrace();
                        }

                        dataOutput.writeUTF(checkString[0] + " " + sb.toString()); // IP

                        String clientInput = dataInput.readUTF();

                        System.out.println(clientInput);

                        continue;
                    }

                }
                scanner.close();
                dataInput.close();
                dataOutput.close();
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidIp(String serverIP) {

        Matcher matcher;
        Pattern IPStructure;

       IPStructure = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

        matcher = IPStructure.matcher(serverIP);
        return matcher.matches();
    }
}

class GetInfo {

    public static String GetAddress(String addressType) {

        String macAddress = "";
        InetAddress lanIp = null;

        try {
            String ipAddress;
            Enumeration<NetworkInterface> net;
            net = NetworkInterface.getNetworkInterfaces();

            while (net.hasMoreElements()) {
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();

                while (addresses.hasMoreElements() && !isVMMac(element.getHardwareAddress())) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        if (ip.isSiteLocalAddress()) {
                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }
                    }
                }
            }
            if (lanIp == null)
                return null;
            if (addressType.equals("ip")) {
                macAddress = lanIp.toString().replaceAll("^/+", "");
            }
            else if (addressType.equals("mac")) {
                macAddress = getMacAddress(lanIp);
            }
            else {
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }



    private static String getMacAddress(InetAddress serverIP) {
        String macAddress = null;
        try {

            NetworkInterface ni = NetworkInterface.getByInetAddress(serverIP);
            byte[] mac = ni.getHardwareAddress();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            macAddress = sb.toString();

        } catch (SocketException ex) {

            ex.printStackTrace();
        }
        return macAddress;
    }


    private static boolean isVMMac(byte[] mac) {
        if (null == mac)
            return false;
        byte invalidMacs[][] = { { 0x00, 0x05, 0x69 },{ 0x00, 0x1C, 0x14 },{ 0x00, 0x0C, 0x29 },{ 0x00, 0x50, 0x56 },{ 0x08, 0x00, 0x27 },{ 0x0A, 0x00, 0x27 },{ 0x00, 0x03, (byte) 0xFF }, { 0x00, 0x15, 0x5D }};

        for (byte[] invalid : invalidMacs) {
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2])
                return true;
        }
        return false;
    }
}

