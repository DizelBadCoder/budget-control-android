package dizel.budget_control.utils

import androidx.fragment.app.Fragment
import dizel.budget_control.R

fun Fragment.replaceFragment(fragment: Fragment) {
    parentFragmentManager
        .beginTransaction()
        .replace(R.id.vFragmentContainer, fragment)
        .addToBackStack(null)
        .commit()
}

fun Fragment.replaceFragmentWithoutBackStack(fragment: Fragment) {
    parentFragmentManager
        .beginTransaction()
        .replace(R.id.vFragmentContainer, fragment)
        .commit()
}