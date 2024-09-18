package th.ac.rmutto.houseprice

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.os.StrictMode
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    @SuppressLint("DefaultLocale")

    lateinit var editTextAge: EditText
    lateinit var editTextGender: EditText
    lateinit var editTextWeekly: EditText
    lateinit var editTextAbsences: EditText
    lateinit var editTextTutor: EditText
    lateinit var Parental: Spinner
    lateinit var editTextExtra: EditText
    lateinit var editTextVolunt: EditText
    lateinit var editTextGPA: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        editTextAge = findViewById(R.id.editTextAge)
        editTextGender = findViewById(R.id.editTextGender)
        editTextWeekly = findViewById(R.id.editTextWeekly)
        editTextAbsences = findViewById(R.id.editTextAbsences)
        editTextTutor = findViewById(R.id.editTextTutor)
        Parental = findViewById(R.id.spinParental)
        editTextExtra = findViewById(R.id.editTextExtra)
        editTextVolunt = findViewById(R.id.editTextVolunt)
        editTextGPA = findViewById(R.id.editTextGPA)
        val btnPredict = findViewById<Button>(R.id.btnPredict)

        val adapParental = ArrayAdapter.createFromResource(
            this,
            R.array.Parental,
            android.R.layout.simple_spinner_item)
        adapParental.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Parental.adapter = adapParental

        btnPredict.setOnClickListener {
            if (editTextAge.text.isEmpty() || editTextGender.text.isEmpty() || editTextWeekly.text.isEmpty() || editTextAbsences.text.isEmpty() ||
                editTextTutor.text.isEmpty() || Parental.selectedItem.toString().isEmpty() || editTextExtra.text.isEmpty() || editTextVolunt.text.isEmpty() ||
                editTextGPA.text.isEmpty()) {
                Toast.makeText(applicationContext, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_LONG).show()
            }
            val url: String = getString(R.string.root_url)
            val okHttpClient = OkHttpClient()

            val formBody: RequestBody = FormBody.Builder()
                .add("Age", editTextAge.text.toString())
                .add("Gender", editTextGender.text.toString())
                .add("StudyTimeWeekly", editTextWeekly.text.toString())
                .add("Absences", editTextAbsences.text.toString())
                .add("Tutor", editTextTutor.text.toString())
                .add("Parental", Parental.selectedItemId.toString())
                .add("Extra", editTextExtra.text.toString())
                .add("Volunt", editTextVolunt.text.toString())
                .add("GPA", editTextGPA.text.toString())
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val data = JSONObject(response.body!!.string())
                if (data.length() > 0) {
                    val GradeClass = data.getString("Grade")
                    val message = "Grade Class ของคุณคือ $GradeClass "
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Grade Class")
                    builder.setMessage(message)
                    builder.setNeutralButton("OK", clearText())
                    val alert = builder.create()
                    alert.show()

                }
            } else {
                Toast.makeText(applicationContext, "ไม่สามารถเชื่อต่อกับเซิร์ฟเวอร์ได้", Toast.LENGTH_LONG).show()
            }
            }


        }//button predict

    private fun clearText(): DialogInterface.OnClickListener? {
        return DialogInterface.OnClickListener { dialog, which ->
            editTextAge.text.clear()
            editTextGender.text.clear()
            editTextWeekly.text.clear()
            editTextAbsences.text.clear()
            editTextTutor.text.clear()
            Parental.setSelection(0)
            editTextExtra.text.clear()
            editTextVolunt.text.clear()
            editTextGPA.text.clear()
        }
    } //main class
    }//onCreate function

