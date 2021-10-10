package dizel.budget_control.core.tools

import kotlin.random.Random
import kotlin.random.nextLong

/**
 * Generate key for database
 * @return "id_<random_int>"
 */
fun generateKey(): String {
    val randomInt = Random.nextLong(0L..Long.MAX_VALUE)
    return "id_${randomInt}"
}