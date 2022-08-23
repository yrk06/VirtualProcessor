package components;

public class Cache extends Component {
    private int mem[];
    private Ram ram;

    private int currentBlock = -5;

    /**
     * Cria uma cache
     * 
     * @param tamanho quantidade de palavras para armazenar na cache
     * @param r       memoria ram para montar a cache
     */
    public Cache(int tamanho, Ram r) {
        int real_size = Math.ceilDiv(tamanho, 2);
        mem = new int[real_size];

        ram = r;
    }

    /**
     * Confere se o endereço está no bloco atual
     * 
     * @param addr Endereço
     * @return Verdadeiro se o endereço está no bloco atual
     */
    private boolean isInBlock(int addr) {
        return addr >= (currentBlock * mem.length * 2) && addr < ((currentBlock + 1) * mem.length * 2);
    }

    /**
     * Copia um bloco de memória da RAM para a cache
     * 
     * @param addr endereço do bloco para copiar
     */
    private void copyInImmediate(int addr) {
        copyOutImmediate();
        currentBlock = Math.floorDiv(addr, mem.length);
        for (int i = 0; i < mem.length; i++) {
            int ram_addr = (currentBlock * mem.length / 2) + i;
            mem[i] = ram.readDoubleWord(ram_addr);
        }
    }

    /**
     * Copia o bloco atual para a memoria Ram
     */
    private void copyOutImmediate() {
        if (currentBlock < 0) {
            return;
        }
        for (int i = 0; i < mem.length; i++) {
            int ram_addr = (currentBlock * mem.length / 2) + i;
            ram.writeDoubleWord(ram_addr, mem[i]);
        }
    }

    /**
     * Le dados utilizando a cache
     * 
     * @param addr endereço para ler os dados
     * @return o dado no endereço apresentado
     */
    public int read(int addr) {
        if (!isInBlock(addr)) {
            copyInImmediate(addr);
        }
        int real_addr = addr / 2 - (currentBlock * mem.length / 2);
        return (mem[real_addr] >> (16 * ((addr + 1) % 2))) & 0xFFFF;
    }

    /**
     * Escreve dados utilizando a cache
     * 
     * @param addr o endereço para escrever
     * @param data os dados para escrever
     */
    public void write(int addr, int data) {
        if (!isInBlock(addr)) {
            copyInImmediate(addr);
        }
        int real_addr = addr / 2 - (currentBlock * mem.length / 2);
        mem[real_addr] = mem[real_addr] & (0xFFFF << 16 * ((addr) % 2));
        mem[real_addr] |= (data & 0xFFFF) << (16 * ((addr + 1) % 2));
    }

    public void writeDoubleWord(int addr, int data) {
        mem[addr] = data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see components.Component#access(int, int, int)
     */
    @Override
    public int access(int address, int data, int flags) {
        Boolean rw = ((flags & 1) == 1) ? true : false;
        if (rw)
            return read(address);
        write(address, data);
        return data;
    }
}
