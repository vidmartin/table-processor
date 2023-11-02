
package evaluation

import table._

abstract class BaseTableEvaluator {
    def evaluateTable(table: Table[TableCell]): Table[ValueTableCell]
}