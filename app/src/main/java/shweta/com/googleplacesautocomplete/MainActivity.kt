package shweta.com.googleplacesautocomplete

import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var placeAdapter: PlaceArrayAdapter? = null
    private lateinit var mPlacesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        Places.initialize(this, "GOOGLE_MAPS_KEY")
        mPlacesClient = Places.createClient(this)

        val mapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.mapLocation) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        placeAdapter = PlaceArrayAdapter(this, R.layout.layout_item_places, mPlacesClient)
        autoCompleteEditText.setAdapter(placeAdapter)

        autoCompleteEditText.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val place = parent.getItemAtPosition(position) as PlaceDataModel
            autoCompleteEditText.apply {
                setText(place.fullText)
                setSelection(autoCompleteEditText.length())
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {

    }
}