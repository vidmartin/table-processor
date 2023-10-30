
package table

import scala.collection.immutable.HashMap
import tableReader.TableReader

class Table[T <: TableCell](content: HashMap[TableCellPosition, TableCell]) {
    def get(pos: TableCellPosition): TableCell = {
        content.getOrElse(pos, new ValueTableCell(EmptyTableCellValue))
    }

    def nonEmpty(): Iterator[TableCellPosition] = {
        content.iterator.flatMap({
            case (pos, ValueTableCell(EmptyTableCellValue)) => None
            case (pos, _) => Some(pos)
        })
    }
}

object Table {
    def parse(tableReader: TableReader): Table[TableCell] = {
        val map = HashMap.from(
            tableReader.zipWithIndex.flatMap({
                case (row, i) => row.iterator.zipWithIndex.map({
                    case (cell, j) => (
                        TableCellPosition(i, j),
                        TableCell.parse(cell).getOrElse(
                            throw new Exception() /* TODO: more specific exception */
                        )
                    )
                })
            })
        )

        new Table(map)
    }
}
