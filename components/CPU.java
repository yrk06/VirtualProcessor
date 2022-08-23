package components;

// Interface para o executor acessar a CPU
public interface CPU {
    public void setRegister(int code, short data);

    public short getRegister(int code);

    public void setProgramCounter(short data);

    public short getProgramCounter();

    public int accessComponent(int addr, int data, int flags);
}
