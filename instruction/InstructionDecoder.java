package instruction;

// Generic decoder
public interface InstructionDecoder {
    public Instruction decode(int instruction);
}
