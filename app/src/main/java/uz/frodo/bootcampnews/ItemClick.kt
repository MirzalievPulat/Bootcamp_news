package uz.frodo.bootcampnews

import android.view.View

interface ItemClick {
    fun onMoreClick(view: View,news: News)
    fun onItemClick(news:News)
}