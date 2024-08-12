/* Do not change the code below. */

interface MyInterface {
    fun print()
    val amount: Int
}

class AdditionPrinter : MyInterface {
    override fun print() { for (i in 1..amount) print("*") }
    override val amount: Int = 3 + 7 // 10
}

class SubtractionPrinter : MyInterface {
    override fun print() { for (i in 1..amount) print("*") }
    override val amount: Int = 23 - 15 // 8
}

class MultiplicationPrinter : MyInterface {
    override fun print() { for (i in 1..amount) print("*") }
    override val amount: Int = 3 * 4 // 12
}

class Printer(base: MyInterface) : MyInterface by base {
    override fun print() {
        for (i in 1..(amount + 2)) print("*")
    }
}

/* Do not change the code above. */

fun main() {

    // Your code here
    val p = AdditionPrinter()

    // Do not change the code below
    p.print()
}