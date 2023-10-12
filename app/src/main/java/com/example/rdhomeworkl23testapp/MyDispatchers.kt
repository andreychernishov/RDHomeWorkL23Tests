package com.example.rdhomeworkl23testapp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object MyDispatchers {

    var main: CoroutineDispatcher = Dispatchers.Main
    var io: CoroutineDispatcher = Dispatchers.IO

}