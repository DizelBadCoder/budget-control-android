package dizel.budget_control.utils

import dizel.budget_control.budget.domain.Currency

fun Double.toMoneyMask(currency: Currency): String {
    var string = this.toString()

    var postfix = (string.takeLastWhile { it.toString() != "." } + ".").reversed()
    string = string.dropLastWhile { it.toString() != "." }.dropLast(1)

    val money = string
        .reversed()
        .chunked(3)
        .reduce { j, i -> "$j $i" }
        .reversed()

    if (postfix == ".0") postfix = ""

    return "$money$postfix ${currency.symbol}"
}