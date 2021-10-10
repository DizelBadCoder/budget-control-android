package dizel.budget_control.budget.domain.mappers

import dizel.budget_control.budget.domain.entity.Category

object CategoryToHashMapMapper {
    fun map(input: Category): Map<String, Map<String, Any>> {
        return input.let { category ->
            hashMapOf(
                category.id to category.toHashMap()
            )
        }
    }
}