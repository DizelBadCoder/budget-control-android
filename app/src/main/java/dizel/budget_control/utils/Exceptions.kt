package dizel.budget_control.utils

class MissingDataException: Exception() {
    override val message: String
        get() = "Invalid data"
}