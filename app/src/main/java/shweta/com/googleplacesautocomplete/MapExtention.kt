package shweta.com.googleplacesautocomplete

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

const val TASK_AWAIT = 120L
const val MAP_CAMERA_ZOOM = 11f
const val MAP_CAMERA_ZOOM_INT = 11

/**
 * This fun is used to move map
 * @receiver GoogleMap
 * @param latLng LatLng?
 */
fun GoogleMap.moveCameraOnMap(latLng: LatLng?) {
    this.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_CAMERA_ZOOM))
}

/**
 * This fun is used to move map
 * @receiver GoogleMap
 * @param latLng LatLng?
 */
fun GoogleMap.moveCameraOnMapBound(latLng: LatLngBounds?) {
    this.animateCamera(CameraUpdateFactory.newLatLngBounds(latLng, MAP_CAMERA_ZOOM_INT))
}

/**
 * This fun is used to get auto complete fields
 * @param mGeoDataClient GeoDataClient
 * @param constraint CharSequence
 * @return ArrayList<AutocompletePrediction>?
 */
fun getAutocomplete(mPlacesClient: PlacesClient, constraint: CharSequence): List<AutocompletePrediction> {
    var list = listOf<AutocompletePrediction>()
    val token = AutocompleteSessionToken.newInstance()
    val request = FindAutocompletePredictionsRequest.builder().setTypeFilter(TypeFilter.ADDRESS).setSessionToken(token).setQuery(constraint.toString()).build()
    val prediction = mPlacesClient.findAutocompletePredictions(request)
    try {
        Tasks.await(prediction, TASK_AWAIT, TimeUnit.SECONDS)
    } catch (e: ExecutionException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    } catch (e: TimeoutException) {
        e.printStackTrace()
    }

    if (prediction.isSuccessful) {
        val findAutocompletePredictionsResponse = prediction.result
        findAutocompletePredictionsResponse?.let {
            list = findAutocompletePredictionsResponse.autocompletePredictions
        }
        return list
    }
    return list
}