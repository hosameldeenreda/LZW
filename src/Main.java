import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File original,compressed,uncompressed;
        original=new File("original.txt");
        compressed=new File("com.txt");
        uncompressed=new File("uncom.txt");
        LZW.compress(original,compressed);
        LZW.decompres(compressed,uncompressed);
    }
}
