package dizel.budget_control.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import dizel.budget_control.R

fun Fragment.replaceFragment(fragment: Fragment) {
    for (i in 0 until parentFragmentManager.backStackEntryCount) {
        parentFragmentManager.popBackStack()
    }
    startFragmentWithoutBackStack(fragment)
}

fun Fragment.startFragment(fragment: Fragment, backStackTag: String? = null) {
    parentFragmentManager.beginTransaction()
        .replace(R.id.vFragmentContainer, fragment)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .addToBackStack(backStackTag)
        .commit()
}

fun Fragment.startFragmentWithoutBackStack(fragment: Fragment) {
    parentFragmentManager.beginTransaction()
        .replace(R.id.vFragmentContainer, fragment)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .commit()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, tag: String? = null) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.vFragmentContainer, fragment, tag)
        .commit()
}