package instruction;

public class Risc16Decoder implements InstructionDecoder {

    @Override
    public Instruction decode(int instruction) {
        Risc16Instruction inst = new Risc16Instruction();
        inst.opcode = (short) (instruction >> 13);
        inst.regA = (short) ((instruction >> 10) & 0x7);
        inst.regB = (short) ((instruction >> 7) & 0x7);
        inst.regC = (short) (instruction & 0x7);
        inst.imm10b = (short) (instruction & 0x3FF);
        inst.imm7b = (short) (instruction & 0x7F);

        inst.imm7b = imm7b2short((short) inst.imm7b);
        return inst;
    }

    public static short imm7b2short(short num) {
        boolean negative = (num >> 6) == 1;
        if (negative) {
            return (short) ((((~num) + 1) & 0x7F) * -1);
        }
        return num;
    }
}
