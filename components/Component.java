package components;

// Generic component
public abstract class Component {
    int mountAddrStart;
    int mountAddrEnd;

    public void mount(int start, int end) {
        mountAddrStart = start;
        mountAddrEnd = end;
    }

    public Boolean isSelected(int address) {
        return address >= mountAddrStart && address < mountAddrEnd;
    }

    /**
     * Acessa o componente
     * 
     * @param address endereço para acessar
     * @param data    dados para enviar
     * @param flags   flags
     * @return
     */
    public abstract int access(int address, int data, int flags);
}
