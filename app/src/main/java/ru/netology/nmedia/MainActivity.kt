package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.focusAndShowKeyboard
import ru.netology.nmedia.util.hiddenKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = PostsAdapter(viewModel)

        binding.postRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }


        binding.saveButton.setOnClickListener {
            with(binding.content) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)

                clearFocus()
                hiddenKeyboard()
            }
        }

        binding.closeEditButton.setOnClickListener {
            with(binding.content) {
                viewModel.onCloseEditClicked()
                clearFocus()
                hiddenKeyboard()
            }
            binding.groupForEdit.visibility = View.GONE
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding.content) {
                val content = currentPost?.content
                setText(content)
                if (content != null) {
                    binding.editMessageTextContent.text = content
                    binding.groupForEdit.visibility = View.VISIBLE
                } else {
                    binding.groupForEdit.visibility = View.GONE
                    clearFocus()
                    hiddenKeyboard()
                }
            }

        }
    }
}





