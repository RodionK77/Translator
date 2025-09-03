package com.example.translator.ui.resources

import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes id: Int): String
}