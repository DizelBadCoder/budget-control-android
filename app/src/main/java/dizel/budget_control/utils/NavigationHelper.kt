package dizel.budget_control.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import dizel.budget_control.R

/**
 * Заменить текущий фрагмент на [fragment] с очисткой стека
 */
fun Fragment.replaceFragment(fragment: Fragment) {
    for (i in 0 until parentFragmentManager.backStackEntryCount) {
        parentFragmentManager.popBackStack()
    }
    parentFragmentManager.beginTransaction()
        .replace(R.id.vFragmentContainer, fragment)
        .commit()
}

fun Fragment.startFragment(fragment: Fragment, tag: String? = null) {
    parentFragmentManager.beginTransaction()
        .replace(R.id.vFragmentContainer, fragment, tag)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .addToBackStack(null)
        .commit()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, tag: String? = null) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.vFragmentContainer, fragment, tag)
        .commit()
}