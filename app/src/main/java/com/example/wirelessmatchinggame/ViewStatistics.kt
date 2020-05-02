package com.example.wirelessmatchinggame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jjoe64.graphview.helper.GraphViewXML
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.DataPointInterface
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.Series
import java.text.SimpleDateFormat
import java.util.*


class ViewStatistics : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    var db = FirebaseFirestore.getInstance()

    private lateinit var statsReference: DatabaseReference

    private inner class ProductViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        internal fun setCount(count: String) {
            val textView = view.findViewById<TextView>(R.id.countTxt)
            textView.text = count+" flips"
        }

        internal fun setResult(result: String) {
            val textView = view.findViewById<TextView>(R.id.resultTxt)
            textView.text = result
        }

        internal fun setTime(time: String) {
            val textView = view.findViewById<TextView>(R.id.timeTxt)
            textView.text = time+" seconds"
        }

        internal fun setDateTime(dateTime: Timestamp) {
            val textView = view.findViewById<TextView>(R.id.datetimeTxt)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val netDate = Date(dateTime.seconds.toLong()*1000)
            textView.text = sdf.format(netDate)
        }
    }

    private inner class ProductFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<Statistic>) : FirestoreRecyclerAdapter<Statistic, ProductViewHolder>(options) {
        override fun onBindViewHolder(productViewHolder: ProductViewHolder, position: Int, productModel: Statistic) {
            productViewHolder.setDateTime(productModel.date)
            productViewHolder.setCount(productModel.count)
            productViewHolder.setResult(productModel.result)
            productViewHolder.setTime(productModel.time)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stats, parent, false)
            return ProductViewHolder(view)
        }
    }

    private var adapter: ProductFirestoreRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_statistics)

        /* CHECK LOGGED IN YET ? */
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        if(user == null) {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        /* NAVIGATION CODE */

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.menu.getItem(2).isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_account -> {
                    // do this event
                    val accountIntent = Intent(this, ManageAccount::class.java)
                    startActivity(accountIntent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_home -> {
                    // do this event
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(homeIntent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_stats -> {
                    // do this event
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        val query = db!!.collection("statistics-db").document("stats").collection("vectorx2263@hotmail.com").orderBy("date", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<Statistic>().setQuery(query, Statistic::class.java).build()
        val recView = findViewById<RecyclerView>(R.id.recyclerView)
        recView.layoutManager = LinearLayoutManager(this)

        adapter = ProductFirestoreRecyclerAdapter(options)
        recView.adapter = adapter




        db.collection("statistics-db").document("stats").collection("vectorx2263@hotmail.com")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("ViewStatistics", "document data >> "+ document.data)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ViewStatistics", "Error getting documents.", exception)
            }

        val graph = findViewById<GraphViewXML>(R.id.graphViewXML)
        val data1 = arrayOf ( DataPoint(0.12, 3.22) , DataPoint(0.22,0.69))
        val seriesD = LineGraphSeries<DataPoint>(data1);
        graph.removeAllSeries()
        graph.addSeries(seriesD)
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()

        if (adapter != null) {
            adapter!!.stopListening()
        }
    }
}
