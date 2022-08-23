package components;

public class Ram extends Component {

    // Unsigned, I think
    private int[] mem;

    /*
     * Create a new Ram component
     * 
     * @param size The ram size in 16 bits increments
     */
    public Ram(int size) {
        int real_size = Math.ceilDiv(size, 2);
        mem = new int[real_size];
    }

    /*
     * Returns data from a virtual address
     * 
     * @param addr The address to read data
     */
    public int read(int addr) {
        int real_addr = addr / 2;
        return (mem[real_addr] >> (16 * ((addr + 1) % 2))) & 0xFFFF;
    }

    public int readDoubleWord(int addr) {
        return mem[addr];
    }

    /*
     * Writes data to a virtual address
     * 
     * @param addr The address to write data
     * 
     * @param data The data to write
     */
    public void write(int addr, int data) {
        int real_addr = addr / 2;
        mem[real_addr] = mem[real_addr] & (0xFFFF << 16 * ((addr) % 2));
        mem[real_addr] |= (data & 0xFFFF) << (16 * ((addr + 1) % 2));
    }

    public void writeDoubleWord(int addr, int data) {
        mem[addr] = data;
    }

    @Override
    public int access(int address, int data, int flags) {
        Boolean rw = ((flags & 1) == 1) ? true : false;
        if (rw)
            return read(address);
        write(address, data);
        return data;
    }

}
