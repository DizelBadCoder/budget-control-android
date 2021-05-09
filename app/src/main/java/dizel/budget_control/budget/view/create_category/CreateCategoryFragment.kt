package dizel.budget_control.budget.view.create_category

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
        _binding = FragmentCreateCategoryBinding.bind(view)
    }
}