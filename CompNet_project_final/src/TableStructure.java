
public class TableStructure {

    protected String macAddress;
    protected String port;

    public TableStructure(String macAddress, String port) {
        this.macAddress = macAddress;
        this.port = port;
    }

    public String getMacAdd() {
        return macAddress;
    }

    public void setMacAdd(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getClientPort() {
        return port;
    }

    public void setClientPort(String port) {
        this.port = port;
    }


}