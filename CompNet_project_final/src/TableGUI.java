import java.text.*;
import java.util.*;
import java.util.Timer;

public class TableGUI {
        protected ArrayList<TableStructure> structure = new ArrayList<>();

        public TableGUI() {
            Timer timer = new Timer();
            TimerTask timerTask = new TableReset();
            timer.schedule(timerTask, 5000, 5000);
        }

        public void add(TableStructure tbStruc) {
            structure.add(tbStruc);
            displayTable();
        }
        public void displayTable() {
            Vector<String> macForTable = new Vector<>(50);
            Vector<String> portForTable = new Vector<>(50);
            Vector<String> columnName = new Vector<>(2);
            if (structure.size() != 0) {
                System.out.println("---------------------------------------------");
                System.out.println("Server time : "  + ShowTime.time());
                System.out.println("MAC Address\t\t\tPort");
                columnName.addElement("MAC Address");
                columnName.addElement("Port");
                TableStructure tbStruct;
                if (structure.size() != 0) {
                    for (int i = 0; i < structure.size(); i++) {
                        tbStruct = structure.get(i);
                        macForTable.addElement(tbStruct.getMacAdd());
                        portForTable.addElement(tbStruct.getClientPort());
                        isMacDuplicate(macForTable);
                        isPortDuplicate(portForTable);
                        System.out.println(macForTable.get(i) + "\t" + portForTable.get(i));
                    }
                }
                System.out.println("\n");
            }
            else {
                whenTableEmpty();
            }
        }

        public void whenTableEmpty() {
            System.out.println("-------------DELETED ALL TABLES-------------");
            System.out.println("Server time : "  + ShowTime.time());
            System.exit(0);
        }
        public Vector isMacDuplicate(Vector e) {
            int counter = 0;
            for(int i = 0; i < e.size(); i++) {
                if (e.get(i) == Server.MacAddress.get(i)) {
                    counter++;
                }
                if (counter == 2) {
                    e.remove(i);
                    break;
                }
            }
            return e;
        }
    public Vector isPortDuplicate(Vector e) {
        int counter = 0;
        for(int i = 0; i < e.size(); i++) {
            if (e.get(i) == Server.portName.get(i)) {
                counter++;
            }
            if (counter == 2) {
                e.remove(i);
                break;
            }
        }
        return e;
    }

        protected class TableReset extends TimerTask {
            @Override
            public void run() {
                if (structure.size() != 0) {
                    structure.remove(0);
                    displayTable();
                }
            }
        }
}

class ShowTime {
    public static String time() {
        SimpleDateFormat patternTime = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String timeInFormat = patternTime.format(currentTime);
        return timeInFormat;
    }
}

