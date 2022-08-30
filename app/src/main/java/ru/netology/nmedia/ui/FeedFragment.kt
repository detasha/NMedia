package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter

import ru.netology.nmedia.databinding.FeedFragmentBinding

import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel by viewModels<PostViewModel>()

    private val Fragment.packageManager
        get() = activity?.packageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        viewModel.playVideo.observe(this) { videoUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
            if (packageManager?.let { intent.resolveActivity(it) } != null) {
                startActivity(intent)
            }
        }


        viewModel
            .navigateToPostContentScreenEvent
            .observe(viewLifecycleOwner) { initialContent ->
                val direction =
                    FeedFragmentDirections.toPostContentFragment(
                        initialContent, PostContentFragment.REQUEST_CURRENT_POST_KEY
                    )
                findNavController().navigate(direction)
            }

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)
        }

    }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

            val adapter = PostsAdapter(viewModel)
            binding.postRecyclerView.adapter = adapter

            viewModel.data.observe(viewLifecycleOwner) { posts ->
                adapter.submitList(posts)
            }

            binding.fab.setOnClickListener {
                viewModel.onAddClicked()
            }
        }.root

    }







