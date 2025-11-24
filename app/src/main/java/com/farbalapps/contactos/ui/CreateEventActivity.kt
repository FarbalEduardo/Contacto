import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.farbalapps.contactos.EventDatabase
import com.farbalapps.contactos.R
import com.farbalapps.contactos.databinding.ActCreateEventBinding
import com.farbalapps.contactos.intarfaces.EventDao
import com.farbalapps.contactos.model.EventEntity

class CreateEventActivity : AppCompatActivity() {
    private lateinit var db: EventDatabase
    private lateinit var eventDao: EventDao
    private lateinit var binding: ActCreateEventBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Aplicar el tema con la barra de estado roja
        super.onCreate(savedInstanceState)
        binding = ActCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = EventDatabase.getDatabase(this)
        eventDao = db.eventDao()
        // ... resto del código existente ...
    }
    
//    private fun saveEvent() {
//        val event = EventEntity(
//            title = binding.titleEditText.text.toString(),
//            date = calendar.timeInMillis,
//            startTime = getStartTime(),
//            endTime = getEndTime(),
//            location = binding.locationEditText.text.toString(),
//            isAllDay = binding.allDaySwitch.isChecked,
//            description = null
//        )
//
//        lifecycleScope.launch {
//            eventDao.insertEvent(event)
//            finish()
//        }
//    }
}