import java.io.FileInputStream;

import components.Cache;
import components.Ram;

public class Runtime {
    public static void main(String args[]) throws Exception {

        // Creating the 1024-word long ram for the processor
        Ram ram = new Ram(1024);

        // Open the binary executable and read into ram
        FileInputStream fis = new FileInputStream(args[0]);
        int addr = 0;
        while (fis.available() > 0) {
            byte[] buffer = new byte[4];
            fis.read(buffer);
            int data = ((buffer[0] & 0xFF) << 24) | ((buffer[1] & 0xFF) << 16) | ((buffer[2] & 0xFF) << 8)
                    | (buffer[3] & 0xFF);
            ram.writeDoubleWord(addr++, data);
        }
        fis.close();

        Processor cpu = new Processor(ram);

        boolean debug = false;
        for (String string : args) {
            if (string.compareTo("-d") == 0) {
                debug = true;
            }
        }

        Cache c = new Cache(8, ram);

        if (debug) {
            cpu.Debug();
        } else {
            cpu.Run();
        }

    }
}