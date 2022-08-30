package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.View
import androidx.activity.result.launch
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostContentActivityBinding
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



        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }


//        viewModel.currentPost.observe(this) { currentPost ->
//            with(binding.content) {
//                val content = currentPost?.content
//                setText(content)
//                if (content != null) {
//                    binding.editMessageTextContent.text = content
//                    binding.groupForEdit.visibility = View.VISIBLE
//                } else {
//                    binding.groupForEdit.visibility = View.GONE
//                    clearFocus()
//                    hiddenKeyboard()
//                }
//            }
//
//        }

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent =
                Intent.createChooser(
                    intent, getString(R.string.chooser_share_post)
                )
            startActivity(shareIntent)
        }


        val postContentActivityLauncher = registerForActivityResult(
            PostContentActivity.ResultContract
        ) { postContent ->
            postContent ?: return@registerForActivityResult
            viewModel.onSaveButtonClicked(postContent)

        }
        viewModel.navigateToPostContentScreenEvent.observe(this) {
            val contentForEdit = viewModel.currentPost.value?.content
            postContentActivityLauncher.launch(contentForEdit)
        }

        viewModel.playVideo.observe(this) { videoUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

    }
}






