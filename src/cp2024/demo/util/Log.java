package cp2024.demo.util;

public class Log {
    public boolean logging = true;
    public synchronized void log(String msg) {
        if(logging)
        System.out.println(msg);
    }
    public static Log LOG = new Log();
}