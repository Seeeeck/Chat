package chat.server;

import java.io.Closeable;
import java.io.IOException;

public class ChatUtils {
    /**
     * 关闭io流
     */
    public static void close(Closeable... targets){
        for(Closeable target:targets){
            try{
                if(null!=target){
                    target.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
