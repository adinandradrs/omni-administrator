package id.codefun.omni.administrator.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String RDS_LOGIN = "BOLOGIN";
    public static final Map<Integer, String> MONTH_MAP_INDONESIA = new HashMap<Integer, String>(){{
        put(0,"Januari"); put(1,"Februari");
        put(2,"Maret"); put(3,"April");
        put(4,"Mei"); put(5,"Juni");
        put(6,"Juli"); put(7,"Agustus");
        put(8,"September"); put(9,"Oktober");
        put(10,"November"); put(11,"Desember");
    }};
 
    public static final Map<Integer, String> DAY_MAP_INDONESIA = new HashMap<Integer, String>(){{
        put(1,"Senin"); put(2,"Selasa");
        put(3,"Rabu"); put(4,"Kamis");
        put(5,"Jumat"); put(6,"Sabtu");
        put(7,"Minggu");
    }};
    
}
