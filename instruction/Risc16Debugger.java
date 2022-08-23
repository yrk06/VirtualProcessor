package instruction;

public class Risc16Debugger implements InstructionDebugger {

    @Override
    public String decode(Instruction ins) {
        Risc16Instruction instruction = (Risc16Instruction) ins;
        if (instruction.opcode == 0b111 && instruction.regA == 0 && instruction.regB == 0 && instruction.imm7b != 0)
            return "halt";
        switch (instruction.opcode) {
            // add
            case 0b000:
                return String.format("add x%d, x%d, x%d", instruction.regA, instruction.regB, instruction.regC);
            // addi
            case 0b001:
                return String.format("addi x%d, x%d, %d", instruction.regA, instruction.regB, instruction.imm7b);
            // nand
            case 0b010:
                return String.format("nand x%d, x%d, x%d", instruction.regA, instruction.regB, instruction.regC);
            // lui
            case 0b011:
                return String.format("lui x%d, %d", instruction.regA, (instruction.imm10b << 6));
            // lw
            case 0b100:
                return String.format("lw x%d, x%d, %d", instruction.regA, instruction.regB, instruction.imm7b);
            // sw
            case 0b101:
                return String.format("sw x%d, x%d, %d", instruction.regA, instruction.regB, instruction.imm7b);
            // beq
            case 0b110:
                return String.format("beq x%d, x%d, %d", instruction.regA, instruction.regB, instruction.imm7b);
            // jalr
            case 0b111:
                return String.format("jalr x%d, x%d", instruction.regA, instruction.regB);
        }
        return "unk";
    }

}
