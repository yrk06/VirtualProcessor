package instruction;

import components.CPU;

public class Risc16Executor extends InstructionExecutor {

    public Risc16Executor(CPU cpu) {
        super(cpu);
    }

    @Override
    public boolean execute(Instruction ins) {
        Risc16Instruction instruction = (Risc16Instruction) ins;
        if (instruction.opcode == 0b111 && instruction.regA == 0 && instruction.regB == 0 && instruction.imm7b != 0)
            return true;
        switch (instruction.opcode) {
            // add
            case 0b000:
                cpu.setRegister(instruction.regA,
                        (short) (cpu.getRegister(instruction.regB) + cpu.getRegister(instruction.regC)));
                break;
            // addi
            case 0b001:
                cpu.setRegister(instruction.regA,
                        (short) (cpu.getRegister(instruction.regB) + instruction.imm7b));
                break;
            // nand
            case 0b010:
                cpu.setRegister(instruction.regA,
                        (short) (~(cpu.getRegister(instruction.regB) & cpu.getRegister(instruction.regC))));
                break;
            // lui
            case 0b011:
                cpu.setRegister(instruction.regA, (short) (instruction.imm10b << 6));
                break;
            // lw
            case 0b100:
                cpu.setRegister(instruction.regA,
                        (short) cpu.accessComponent(
                                Short.toUnsignedInt((short) (cpu.getRegister(instruction.regB) + instruction.imm7b)), 0,
                                1));

                break;
            // sw
            case 0b101:
                cpu.accessComponent(
                        Short.toUnsignedInt((short) (cpu.getRegister(instruction.regB) + instruction.imm7b)),
                        cpu.getRegister(instruction.regA), 0);
                break;
            // beq
            case 0b110:
                if (cpu.getRegister(instruction.regA) == cpu.getRegister(instruction.regB)) {
                    cpu.setProgramCounter((short) (cpu.getProgramCounter() + instruction.imm7b - 1));
                }
                break;
            // jalr
            case 0b111:
                short currentInstruction = (short) (cpu.getProgramCounter() + 1);
                cpu.setProgramCounter((short) (cpu.getRegister(instruction.regB) - 1));
                cpu.setRegister(instruction.regA, currentInstruction);
                break;
        }
        return false;
    }
}
