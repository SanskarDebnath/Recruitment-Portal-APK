package com.example.recruitment_portal

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class StackPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    alpha = 1f
                    translationX = 0f
                    translationY = 0f
                    scaleX = 1f
                    scaleY = 1f
                    translationZ = 1f // Keep top card above
                    rotation = position * 15f // Add dynamic rotation when swiping
                }
                position <= 3 -> { // (0, 3]
                    // Keep the page opaque to prevent seeing overlapping text from cards behind
                    alpha = 1f
                    
                    // Counteract the default slide transition
                    translationX = pageWidth * -position
                    
                    // Move it downwards
                    translationY = position * 45f
                    
                    // Scale the page down more noticeably
                    val scaleFactor = 0.80f + (1 - 0.80f) * (1 - position / 3f)
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    
                    // Put it behind the current page
                    translationZ = -position
                    rotation = 0f // Keep background cards straight
                }
                else -> { // (3,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}
