package dizel.budget_control.utils

import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Generate key for database
 * @return "id_<random_int>"
 */
fun generateKey(): String {
    val randomInt = Random.nextInt(0..Integer.MAX_VALUE)
    return "id_${randomInt}"
}