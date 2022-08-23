import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import components.*;
import instruction.Instruction;
import instruction.InstructionDebugger;
import instruction.InstructionDecoder;
import instruction.InstructionExecutor;
import instruction.Risc16Debugger;
import instruction.Risc16Decoder;
import instruction.Risc16Executor;

public class Processor implements CPU {

    // A component is everything connected to the CPU address and data bus
    Component[] components;

    short[] registers;
    short programCounter;

    @Override
    public short getProgramCounter() {
        return programCounter;
    }

    @Override
    public short getRegister(int code) {
        return registers[code];
    }

    @Override
    public void setProgramCounter(short data) {
        programCounter = data;

    }

    @Override
    public void setRegister(int code, short data) {
        registers[code] = data;
    }

    // The ISA is decoupled from the actual processor, so you can implement
    // whichever ISA you like
    InstructionDecoder decoder;
    InstructionExecutor executor;
    InstructionDebugger debugger;

    public Processor(Component ram) {

        // Initialize Registers
        registers = new short[7];

        // Initialize stack with 128 words
        registers[0] = 128;

        // Create the instruction decoders and executors
        decoder = new Risc16Decoder();
        executor = new Risc16Executor(this);
        debugger = new Risc16Debugger();

        // Create the slots for the other components
        components = new Component[4];

        // Mount and add the ram
        ram.mount(0, 1024);
        components[0] = ram;

        // Create a serial module to print stuff
        Serial serial = new Serial();
        serial.mount(1024, 1025);
        components[1] = serial;

        GUI gui = new GUI();
        gui.mount(1025, 1030);
        components[2] = gui;
    }

    public void Run() {
        Run(fetch((short) 128));
    }

    public void Run(int addr) {
        programCounter = (short) addr;
        try {
            // Main CPU loop
            while (true) {
                int instruction_code = fetch(programCounter);
                Instruction instruction = decode(instruction_code);
                boolean halt = execute(instruction);
                if (halt)
                    break;
                programCounter++;
            }
            printStatus();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("At instruction %d", programCounter);
            printStatus();
        }
    }

    public void Debug() {
        Debug(fetch((short) 128));
    }

    public void Debug(int addr) {
        LinkedList<String> trace = new LinkedList<>();
        int step_until = 0;
        Scanner input = new Scanner(System.in);
        programCounter = (short) addr;
        System.out.println("Starting debugger");
        try {
            while (true) {
                int instruction_code = fetch(programCounter);
                Instruction instruction = decode(instruction_code);
                boolean step = false;

                // Debug command interpreter
                while (!step) {

                    trace.add(String.format("%d : %s\n", programCounter, debugger.decode(instruction)));
                    System.out.printf("%d : %s \t> ", programCounter, debugger.decode(instruction));
                    if (step_until != 0) {
                        if (programCounter != step_until) {
                            System.out.println();
                            break;
                        }
                        step_until = 0;
                    }
                    String command = input.nextLine();
                    switch (command) {
                        case "step":
                            step = true;
                            break;
                        case "sts":
                            printStatus();
                            break;
                    }
                    if (command == "step until end") {
                        step_until = 1;
                        break;
                    } else if (command.startsWith("step until")) {
                        step_until = Integer.parseInt(command.replace("step until ", ""));
                        break;
                    }
                }
                boolean halt = execute(instruction);
                if (halt)
                    break;
                programCounter++;
            }
            printStatus();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("At instruction %d\n", programCounter);
            printStatus();
        } finally {
            input.close();
        }

        try {
            FileOutputStream fos = new FileOutputStream("program.trace");
            for (String string : trace) {
                fos.write(string.getBytes());
            }
            fos.close();
        } catch (Exception e) {
            //
        }

    }

    // Print internal register status
    public void printStatus() {
        System.out.println("╔══════════╦════════╦═══════╗");
        System.out.println("║ Register ║  Hex   ║  Dec  ║");
        System.out.println("╟──────────╫────────╫───────╢");
        System.out.printf("║    PC    ║ 0x%04x ║ %05d ║\n", Short.toUnsignedInt(programCounter),
                Short.toUnsignedInt(programCounter));
        System.out.println("╟──────────╫────────╫───────╢");
        System.out.printf("║    SP    ║ 0x%04x ║ %05d ║\n", Short.toUnsignedInt(registers[0]),
                Short.toUnsignedInt(registers[0]));
        System.out.println("╟──────────╫────────╫───────╢");
        for (int i = 1; i < 7; i++) {
            System.out.printf("║    x%d    ║ 0x%04x ║ %05d ║\n", i, Short.toUnsignedInt(registers[i]),
                    Short.toUnsignedInt(registers[i]));
            if (i != 6)
                System.out.println("╟──────────╫────────╫───────╢");
        }
        System.out.println("╚══════════╩════════╩═══════╝");
    }

    // Access components mounted to the bus
    public int accessComponent(int addr, int data, int flags) {
        for (Component component : components) {
            if (!component.isSelected(addr))
                continue;
            return component.access(addr, data, flags);
        }
        return data;
    }

    // Wrapper functions
    private int fetch(short addr) {
        return accessComponent(Short.toUnsignedInt(addr), 0, 1);
    }

    private Instruction decode(int instruction) {
        return decoder.decode(instruction);
    }

    private boolean execute(Instruction inst) {
        return executor.execute(inst);
    }
}
