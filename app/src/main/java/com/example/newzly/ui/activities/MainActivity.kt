package com.example.newzly.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.newzly.R
import com.example.newzly.data.model.NewsViewModel
import com.example.newzly.databinding.ActivityMainBinding
import com.example.newzly.databinding.FullArticleItemBinding
import com.example.newzly.ui.adapters.NewsAdapter
import me.saket.inboxrecyclerview.animation.ItemExpandAnimator
import me.saket.inboxrecyclerview.dimming.DimPainter
import me.saket.inboxrecyclerview.page.InterceptResult

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        binding.newsRv.expandablePage = binding.expandablePage
        binding.newsRv.itemExpandAnimator = ItemExpandAnimator.scale()
        binding.newsRv.dimPainter = DimPainter.listAndPage(Color.WHITE, 0.65f)

        viewModel.news.observe(this) { news ->
            binding.loadingProgress.isVisible = false

            val adapter = NewsAdapter(news.articles) { article ->

                //give each article special id (it's url) to expand it
                val itemId = article.url.hashCode().toLong()
                binding.newsRv.expandItem(itemId)

                binding.expandablePage.post {
                    binding.expandablePage.removeAllViews()

                    //full article custom layout
                    val bindingFull = FullArticleItemBinding.inflate(layoutInflater)
                    binding.expandablePage.addView(bindingFull.root)

                    binding.searchBarContainer.visibility = View.GONE

                    //populate the full article custom layout with data
                    Glide.with(this).load(article.urlToImage).into(bindingFull.articleImg)
                    bindingFull.articleTitle.text = article.title
                    bindingFull.articleAuthor.text = article.author
                    bindingFull.articleDate.text = article.publishedAt.split("T").firstOrNull() ?: article.publishedAt
                    bindingFull.articleContent.text = article.content ?: "No content Available"

                    bindingFull.shareBtn.setOnClickListener {
                        ShareCompat.IntentBuilder(this).setType("text/plain").setChooserTitle("${article.title}").setText(article.url).startChooser()
                    }

                    // collapse the article custom layout when back button is pressed
                    bindingFull.backBtn.setOnClickListener {
                        binding.newsRv.collapse()
                        binding.searchBarContainer.visibility = View.VISIBLE
                    }

                    binding.expandablePage.setOnTouchListener { _, _ ->
                        binding.searchBarContainer.visibility = View.VISIBLE
                        false
                    }
                }
            }
            binding.newsRv.adapter = adapter
        }
        // handle errors
        viewModel.error.observe(this) { error ->
            binding.loadingProgress.isVisible = false
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        binding.swipeRefresh.setOnRefreshListener { loadNews() }

        loadNews()

        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true) {

            // if the article is open collapse it, otherwise go back normally
            override fun handleOnBackPressed() {

                if (binding.expandablePage.isExpanded) {
                    binding.newsRv.collapse()
                    binding.searchBarContainer.visibility = View.VISIBLE
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun loadNews() {
        viewModel.loadNews()
        binding.swipeRefresh.isRefreshing = false
    }
}