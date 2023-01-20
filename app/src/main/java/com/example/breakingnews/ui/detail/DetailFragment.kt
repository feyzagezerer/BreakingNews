package com.example.breakingnews.ui.detail
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.breakingnews.R
import com.example.breakingnews.data.model.Article
import com.example.breakingnews.databinding.FragmentDetailBinding
import com.example.breakingnews.ui.news.NewsViewModel
import com.example.breakingnews.util.Constants.Companion.NEW_IS_ADDED_TO_FAVORITES
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val viewModel: NewsViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    lateinit var article: Article
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        article = args.article
        isFavorite = args.favorite
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        bind()
    }

    private fun bind() {
        binding.apply {
            newsTitle.text = article.title
            newsText.text = article.content
            newsSource.text = article.source?.name
            newsDate.text = article.publishedAt
            Picasso.get().load(article.urlToImage).into(newsImage)
        }
    }

    private fun setIconFavorite(item: MenuItem) {
        item.icon =
            if (isFavorite)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_favorite)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_favorite_border)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_action_bar, menu)
        setIconFavorite(menu.getItem(0))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                if (isFavorite) {
                    viewModel.removeArticleFromFavorites(article)
                    val totalCountOfFavorites = viewModel.getCount().minus(1)
                    Snackbar.make(binding.root,"Deleted successfully, total count of favorites: $totalCountOfFavorites", Snackbar.LENGTH_LONG).show()
                } else {
                    val totalCountOfFavorites = viewModel.getCount().plus(1)
                    viewModel.addArticleToFavorites(article)
                    Snackbar.make(binding.root,"Added successfully, total count of favorites: $totalCountOfFavorites", Snackbar.LENGTH_LONG).show()
                    val bundle = Bundle()
                    val articleID = article.id
                    if (articleID != null) {
                        bundle.putInt("articleID", articleID)
                    }
                    firebaseAnalytics.logEvent(NEW_IS_ADDED_TO_FAVORITES, bundle)
                }
                isFavorite = !isFavorite
                setIconFavorite(item)
                true
            }
            else -> {
                findNavController().navigateUp()
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}