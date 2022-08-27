package net.tyzen.io;

public class Config {

    public static String url() {
        String urls="-"; //Enter your server ip
        return urls;
    }
    public static String api() {
        String urls="http://-/piWallet2/api.php?a="; //Enter your server ip
        return urls;
    }
    public static String key() {
        String keys = "-"; //Enter your license key
        return keys;
    }
    public static String pass() {
        String passs = "RandomInitVector"; //Enter your license pass
        return passs;
    }
    public static String code() {
        String codes = "-"; // Enter your code for server connection
        return codes;
    }
    public static String smartcontract() {
        String smartcontracts = "0x524dc4b2db22761bf4df9cecf5f25890865c086d";//Smart contract of your ERC20 token
        return smartcontracts;
    }
    public static String ethplorerAPI() {
        String api = "";//contact https://ethplorer.io/ for this API
        return api;
    }
    public static String privacy() {
        String pr="https://studio.tyzen.io/tyzenwallet/privacy"; //Enter link privacy
        return pr;
    }
    public static String terms() {
        String ter="https://studio.tyzen.io/tyzenwallet/terms"; //Enter terms and condition
        return ter;
    }
}
