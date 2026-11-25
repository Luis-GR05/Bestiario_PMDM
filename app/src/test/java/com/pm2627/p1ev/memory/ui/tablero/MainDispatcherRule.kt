package com.pm2627.p1ev.memory.ui.tablero

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * viewModelScope usa Dispatchers.Main, que en la JVM no existe. Esta regla lo sustituye por
 * un dispatcher de test (apuntes 3C.4 §4 y 3C.1 §5.4). Uso StandardTestDispatcher: las
 * corrutinas no corren solas, avanzan cuando el test mueve el tiempo virtual
 * (advanceTimeBy / runCurrent). Así pruebo el tiempo sin esperar.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) = Dispatchers.setMain(testDispatcher)
    override fun finished(description: Description) = Dispatchers.resetMain()
}