package com.example.recruitment_portal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.card.MaterialCardView

object NotificationHelper {

    private const val MAX_STACK = 4

    fun showStackedNotification(container: LinearLayout, message: String) {
        val context = container.context
        val inflater = LayoutInflater.from(context)
        
        // Remove oldest if limit reached
        if (container.childCount >= MAX_STACK) {
            val oldest = container.getChildAt(0)
            val slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            oldest.startAnimation(slideOut)
            container.removeViewAt(0)
        }

        // Create new notification view
        val notificationView = inflater.inflate(R.layout.layout_stacked_notification, container, false)
        val card = notificationView.findViewById<MaterialCardView>(R.id.notificationCard)
        val textView = notificationView.findViewById<TextView>(R.id.tvMessage)
        
        textView.text = message

        // Add to container
        container.addView(notificationView)
        
        // Animation
        val slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        notificationView.startAnimation(slideIn)

        // Adjust scales/offsets for stacking effect
        updateStackEffects(container)

        // Auto-remove after delay
        notificationView.postDelayed({
            if (notificationView.parent != null) {
                val fadeOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
                notificationView.startAnimation(fadeOut)
                container.removeView(notificationView)
                updateStackEffects(container)
            }
        }, 4000)
    }

    private fun updateStackEffects(container: LinearLayout) {
        val count = container.childCount
        for (i in 0 until count) {
            val child = container.getChildAt(i)
            val card = child.findViewById<MaterialCardView>(R.id.notificationCard)
            
            // Newer items (higher index) are at the front/bottom
            // We want older items to look like they are behind
            val positionFromNewest = count - 1 - i
            
            val scale = 1.0f - (positionFromNewest * 0.05f)
            val alpha = 1.0f - (positionFromNewest * 0.2f)
            val translationY = positionFromNewest * -20f // Move "up" to look like stack

            card.scaleX = scale
            card.scaleY = scale
            card.alpha = alpha
            card.translationY = translationY
            card.elevation = (count - positionFromNewest).toFloat()
        }
    }
}