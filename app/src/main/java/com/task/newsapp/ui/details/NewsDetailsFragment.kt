package com.task.newsapp.ui.details

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.task.newsapp.databinding.FragmentNewsDetailsBinding
import com.task.newsapp.model.Articles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailsFragment : Fragment() {

    private var binding: FragmentNewsDetailsBinding? = null
    private val arguments: NewsDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding?.apply {
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    progressbar.visibility = View.VISIBLE
                    textNoResults.visibility = View.GONE
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    progressbar.visibility = View.GONE
                    textNoResults.visibility = View.GONE
                    super.onPageFinished(view, url)
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?,
                                             error: WebResourceError?) {
                    progressbar.visibility = View.GONE
                    textNoResults.visibility = View.VISIBLE
                    super.onReceivedError(view, request, error)
                }
            }
        }

        displayDetails(arguments.article)
    }

    private fun displayDetails(article: Articles?) {
        article?.let { news ->
            binding?.apply {

                if(news.url != null) {
                    textNoResults.visibility = View.GONE
                    webView.loadUrl(news.url)
                } else {
                    webView.visibility = View.GONE
                    textNoResults.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}