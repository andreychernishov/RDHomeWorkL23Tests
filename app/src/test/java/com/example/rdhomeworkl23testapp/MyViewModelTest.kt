package com.example.rdhomeworkl23testapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response


class MyViewModelTest{

    @Rule
    fun getRule() = InstantTaskExecutorRule()

    @Before
    fun setup(){
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        MyDispatchers.io = testDispatcher
        MyDispatchers.main = testDispatcher
    }

    @Test
    fun when_responses_success_return_update_data(){
        val repository = Mockito.mock(Repository::class.java)
        val successResponse = BitcoinResponce(Data("1","27 000"))
        val successResponse1 = Response.success(successResponse)
//
        val viewModel = MyViewModel(repository)

        var eventList = mutableListOf<MyViewModel.UIState>()
        viewModel.uiState.observeForever{
            eventList.add(it)
        }
        runBlocking {
            Mockito.`when`(repository.getCurrencyByName("bitcoin")).thenReturn(successResponse1)
        }
        viewModel.getData()
        Assert.assertEquals(MyViewModel.UIState.Empty,eventList[0])
        Assert.assertEquals(MyViewModel.UIState.Processing,eventList[1])
        Assert.assertEquals(MyViewModel.UIState.Result("1 27 000"),eventList[2])
    }
    @Test
    fun when_responses_error(){
        val repository = Mockito.mock(Repository::class.java)
        val errorResponse = Response.error<BitcoinResponce>(500, "  ".toResponseBody())
//
        val viewModel = MyViewModel(repository)

        var eventList = mutableListOf<MyViewModel.UIState>()
        viewModel.uiState.observeForever{
            eventList.add(it)
        }
        runBlocking {
            Mockito.`when`(repository.getCurrencyByName("bitcoin")).thenReturn(errorResponse)
        }
        viewModel.getData()
        Assert.assertEquals(MyViewModel.UIState.Empty,eventList[0])
        Assert.assertEquals(MyViewModel.UIState.Processing,eventList[1])
        Assert.assertEquals(MyViewModel.UIState.Error("Error code 500"),eventList[2])
    }
    @Test
    fun viewmodel_to_repository_success(){
        val apiInterface = Mockito.mock(ApiInterface::class.java)
        val repository = Repository(apiInterface)

        val successResponse = BitcoinResponce(Data("1","27 000"))
        val successResponse1 = Response.success(successResponse)
//
        val viewModel = MyViewModel(repository)

        var eventList = mutableListOf<MyViewModel.UIState>()
        viewModel.uiState.observeForever{
            eventList.add(it)
        }
        runBlocking {
            Mockito.`when`(apiInterface.getCryptoByName("bitcoin")).thenReturn(successResponse1)
        }
        viewModel.getData()
        Assert.assertEquals(MyViewModel.UIState.Empty,eventList[0])
        Assert.assertEquals(MyViewModel.UIState.Processing,eventList[1])
        Assert.assertEquals(MyViewModel.UIState.Result("1 27 000"),eventList[2])

    }
@Test
    fun viewmodel_to_repository_error(){
        val apiInterface = Mockito.mock(ApiInterface::class.java)
        val repository = Repository(apiInterface)

        val errorResponse = Response.error<BitcoinResponce>(500, "  ".toResponseBody())
//
        val viewModel = MyViewModel(repository)

        var eventList = mutableListOf<MyViewModel.UIState>()
        viewModel.uiState.observeForever{
            eventList.add(it)
        }
        runBlocking {
            Mockito.`when`(apiInterface.getCryptoByName("bitcoin")).thenReturn(errorResponse)
        }
        viewModel.getData()
        Assert.assertEquals(MyViewModel.UIState.Empty,eventList[0])
        Assert.assertEquals(MyViewModel.UIState.Processing,eventList[1])
        Assert.assertEquals(MyViewModel.UIState.Error("Error code 500"),eventList[2])

    }

}