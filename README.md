# Virtual Processor
Virtual processor is a processor emulator built in java. The standard ISA implemented is [RiSC-16](https://user.eng.umd.edu/~blj/risc/)

## ISA
This virtual processor allows different ISAs to be implemented, as long as they extend from the generic `Instruction`, `InstructionDecoder` `InstructionExecutor` and `InstructionDebugger` classes.

## Components
This virtual processor support "components", which are devices connected to processor address and data busses. Componentes must extend the generic `component` class