package com.example.myapplication

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.DiaryEntry
import com.example.myapplication.repository.DiaryRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {

    fun saveDiaryEntry(entry: DiaryEntry) {
        repository.saveDiaryEntry(entry)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Entry saved successfully
            }, { error ->
                // Error saving entry
            })
    }
}

