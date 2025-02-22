package io.github.h4rz.fetchrewardsandroid.di

import io.github.h4rz.fetchrewardsandroid.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}