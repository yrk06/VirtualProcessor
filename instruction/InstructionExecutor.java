package instruction;

import components.CPU;

// Generic executor
public abstract class InstructionExecutor {

    public CPU cpu;

    public InstructionExecutor(CPU cpu) {
        this.cpu = cpu;
    }

    public abstract boolean execute(Instruction instruction);
}
