package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = object : SquareBoard {
    private val cells: Map<Pair<Int, Int>, Cell>

    init {
        val localCells = mutableMapOf<Pair<Int, Int>, Cell>()
        for (i in 1..width) {
            for (j in 1..width) {
                localCells[Pair(i, j)] = Cell(i, j)
            }
        }
        cells = localCells.toMap()
    }

    override val width: Int
        get() = width

    override fun getCellOrNull(i: Int, j: Int): Cell? = cells[Pair(i, j)]

    override fun getCell(i: Int, j: Int): Cell = cells[Pair(i, j)]!!

    override fun getAllCells(): Collection<Cell> = cells.values

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val rowCells = cells.values.filter {
            it.i == i && it.j in jRange
        }
        if (jRange.last < jRange.first) {
            return rowCells.sortedByDescending { it.j }
        }
        return rowCells
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val rowCells = cells.values.filter {
            it.j == j && it.i in iRange
        }
        if (iRange.last < iRange.first) {
            return rowCells.sortedByDescending { it.i }
        }
        return rowCells
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? = when (direction) {
        UP -> getCellOrNull(this.i - 1, this.j)
        LEFT -> getCellOrNull(this.i, this.j - 1)
        DOWN -> getCellOrNull(this.i + 1, this.j)
        RIGHT -> getCellOrNull(this.i, this.j + 1)
    }
}

fun <T> createGameBoard(width: Int): GameBoard<T> = object : GameBoard<T> {
    private val squareBoard: SquareBoard = createSquareBoard(width)
    private val cellValues = mutableMapOf<Cell, T?>()

    init {
        cellValues.putAll(squareBoard.getAllCells().map { it to null })
    }

    override fun get(cell: Cell): T? = cellValues[cell]

    override fun all(predicate: (T?) -> Boolean): Boolean = cellValues.all { (_, value) -> predicate(value) }

    override fun any(predicate: (T?) -> Boolean): Boolean = cellValues.any { (_, value) -> predicate(value) }

    override fun find(predicate: (T?) -> Boolean): Cell? = filter(predicate).firstOrNull()

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        cellValues.filter { (_, value) -> predicate(value) }.keys

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override val width: Int
        get() = squareBoard.width

    override fun getCellOrNull(i: Int, j: Int): Cell? = squareBoard.getCellOrNull(i, j)

    override fun getCell(i: Int, j: Int): Cell = squareBoard.getCell(i, j)

    override fun getAllCells(): Collection<Cell> = squareBoard.getAllCells()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> = squareBoard.getRow(i, jRange)

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> = squareBoard.getColumn(iRange, j)

    override fun Cell.getNeighbour(direction: Direction): Cell? =
        squareBoard.getCell(this.i, this.j).getNeighbour(direction)

}

