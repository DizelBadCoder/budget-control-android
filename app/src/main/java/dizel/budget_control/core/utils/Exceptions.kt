package dizel.budget_control.core.utils

class MissingDataException: Exception() {
    override val message: String
        get() = "Invalid data"
}