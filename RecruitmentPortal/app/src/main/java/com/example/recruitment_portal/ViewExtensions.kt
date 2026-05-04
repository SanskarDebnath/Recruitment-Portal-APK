package com.example.recruitment_portal

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.animation.AnimationUtils

fun View.setClickAnimation() {
    this.setOnClickListener {
        val anim = AnimationUtils.loadAnimation(context, R.anim.button_click)
        this.startAnimation(anim)
    }
}

fun View.startClickAnimation() {
    context.vibrate(30) // Subtle 30ms vibration
    val anim = AnimationUtils.loadAnimation(context, R.anim.button_click)
    this.startAnimation(anim)
}

fun Context.vibrate(duration: Long = 50) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(duration)
    }
}

fun Context.getThemeColor(attrRes: Int): Int {
    val typedValue = android.util.TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}