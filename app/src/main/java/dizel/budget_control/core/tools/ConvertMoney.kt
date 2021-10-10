package dizel.budget_control.core.tools

import dizel.budget_control.budget.domain.entity.Currency

object ConvertMoney {
    fun convert(money: Double, from: Currency, to: Currency): Double {
        if (from == to) return money

        return (money / to.cost) * from.cost
    }
}