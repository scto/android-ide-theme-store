package moe.smoothie.androidide.themestore.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import moe.smoothie.androidide.themestore.data.MicrosoftStoreRequestPayload
import moe.smoothie.androidide.themestore.ui.VisualStudioThemeCardState
import moe.smoothie.androidide.themestore.util.hasNetwork
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.coroutines.executeAsync
import javax.inject.Inject

@HiltViewModel
class MicrosoftStoreViewModel @Inject constructor(
    private val httpClient: OkHttpClient
) : ViewModel(), StoreFrontViewModel<VisualStudioThemeCardState> {
    private val mutableItems = MutableStateFlow<List<VisualStudioThemeCardState>>(emptyList())
    override val items: StateFlow<List<VisualStudioThemeCardState>> = mutableItems

    private val mutableIsLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = mutableIsLoading

    private val mutableAllItemsLoaded = MutableStateFlow(false)
    override val allItemsLoaded: StateFlow<Boolean> = mutableAllItemsLoaded

    private val mutableErrorReceiving = MutableStateFlow(false)
    override val errorReceiving: StateFlow<Boolean> = mutableErrorReceiving

    private val mutableDeviceHasNetwork = MutableStateFlow(true)
    override val deviceHasNetwork: StateFlow<Boolean> = mutableDeviceHasNetwork

    private val mutableErrorParsingResponse = MutableStateFlow(false)
    override val errorParsingResponse: StateFlow<Boolean> = mutableErrorParsingResponse

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun loadItems(context: Context, pageSize: Int) {
        if (!hasNetwork(context)) {
            mutableDeviceHasNetwork.update { false }
            return
        }

        mutableDeviceHasNetwork.update { true }
        mutableErrorReceiving.update { false }
        mutableErrorParsingResponse.update { false }

        viewModelScope.launch(Dispatchers.IO) {
            if (mutableAllItemsLoaded.value) {
                return@launch
            }

            if (mutableIsLoading.value) {
                return@launch
            }

            mutableIsLoading.update { true }

            val pageNumber = 1 + items.value.size / pageSize
            val payload =
                Json.encodeToString(MicrosoftStoreRequestPayload.construct(pageSize, pageNumber))
            val request = Request.Builder()
                .url("https://marketplace.visualstudio.com/_apis/public/gallery/extensionquery")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .post(payload.toRequestBody("application/json".toMediaType()))
                .build()

            try {
                httpClient.newCall(request).executeAsync().use {

                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                mutableErrorReceiving.update { true }
            }

            mutableIsLoading.update { false }
        }
    }

    override fun reload(context: Context, pageSize: Int) {
        TODO("Not yet implemented")
    }

}