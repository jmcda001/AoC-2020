package day8

import Day
import java.io.File

class Day8(private val program: List<Instruction>) : Day {
    enum class OPERATION {
        ACC, JMP, NOP;

        override fun toString(): String {
            return super.toString().toLowerCase()
        }
    }
    private fun OPERATION.execute(state: State, amount: Int) = when(this) {
        OPERATION.ACC -> State(state.accumulator + amount, state.pc + 1)
        OPERATION.JMP -> State(state.accumulator, state.pc + amount)
        OPERATION.NOP -> State(state.accumulator, state.pc + 1)
    }
    data class State(val accumulator: Int = 0, val pc: Int = 0)
    data class Instruction(val op: OPERATION, val amount: Int, var executed: Boolean = false)

    private fun MutableList<Instruction>.reset(): MutableList<Instruction> = this.also {
        it.forEach { instr -> instr.executed = false }
    }
    private fun MutableList<Instruction>.run(start: State = State()): State {
        var currentState = start
        while (currentState.pc < this.size && !this[currentState.pc].executed) {
            val (op, amount) = this[currentState.pc]
            this[currentState.pc].executed = true
            currentState = op.execute(currentState, amount)
        }
        return currentState
    }
    override fun puzzle1(): Int? = program.toMutableList().reset().run().accumulator
    override fun puzzle2(): Int? {
        var currentState = State()
        val exe = program.toMutableList().reset()
        val executedInstructions: MutableSet<Int> = mutableSetOf()
        while(currentState.pc < exe.size && currentState.pc !in executedInstructions) {
            val (op, amount) = exe[currentState.pc]
            exe[currentState.pc].executed = true
            executedInstructions.add(currentState.pc)
            currentState = when (op) {
                OPERATION.ACC -> op.execute(currentState, amount)
                OPERATION.NOP -> {
                    val possibleEndState = exe.toMutableList().run(OPERATION.JMP.execute(currentState, amount))
                    if (possibleEndState.pc == exe.size) { possibleEndState }
                    else { op.execute(currentState, amount) }
                }
                OPERATION.JMP -> {
                    val possibleEndState = exe.toMutableList().run(OPERATION.NOP.execute(currentState, amount))
                    if (possibleEndState.pc == exe.size) { possibleEndState }
                    else { op.execute(currentState, amount) }
                }
            }
        }
        return currentState.accumulator
    }

    companion object {
        fun buildFromFile(filename: String) = Day8(
                File(filename).readLines().map {line ->
                    line.split(' ').let {
                        val op = it[0]
                        val amount = it[1].drop(1).toInt() * if (it[1][0] == '-') { -1 } else { 1 }
                        Instruction(OPERATION.valueOf(op.toUpperCase()), amount)
                    }
                }
        )
    }
}