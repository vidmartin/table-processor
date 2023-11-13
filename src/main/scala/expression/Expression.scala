
package expression

import table.TableCellValue
import table.TableCellPosition

abstract class Expression {
    def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue
}
