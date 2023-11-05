
package expression

import table.TableCellValue

abstract class Expression {
    def evaluate(): TableCellValue
}
