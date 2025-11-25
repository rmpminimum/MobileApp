package com.nano.min.viewmodel

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel

open class ViewModelRes(application: Application) : AndroidViewModel(application) {
    protected fun getString(id: Int):  String {
        return getApplication<Application>().getString(id)
    }
}