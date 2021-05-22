package dizel.budget_control.utils

import dizel.budget_control.budget.domain.Currency

object ConvertMoney {
    fun convert(money: Double, from: Currency, to: Currency): Double {
        if (from == to) return money

        return (money / to.cost) * from.cost
    }
}