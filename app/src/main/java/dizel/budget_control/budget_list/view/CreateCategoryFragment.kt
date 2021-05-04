package dizel.budget_control.budget_list.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.databinding.FragmentCreateCategoryBinding

class CreateCategoryFragment : Fragment(R.layout.fragment_create_category) {

    private var _binding: FragmentCreateCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateCategoryBinding.bind(view).apply {

        }

        setUpToolbar()
    }

    private fun setUpToolbar() {
        activity?.actionBar?.setTitle(R.string.create_category)
    }
}