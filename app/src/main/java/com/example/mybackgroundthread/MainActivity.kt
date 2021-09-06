package com.example.mybackgroundthread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btn_start)
        val tvStatus = findViewById<TextView>(R.id.tv_status)

        /*
        Executor untuk membuat proses berjalan secara asynchronous dan
        Handler untuk tetap bisa berkomunikasi (synchronize) dengan ui thread.
         */

        /*
        newSingleThreadExecutor yang artinya hanya satu thread yang Anda buat.
        Sehingga ketika Anda klik tombol berkali-kali hanya satu proses yang dijalankan
        dan proses lainnya akan dieksekusi setelahnya proses sebelumnya selesai.
        Jika Anda ingin lebih dari satu thread dan melihat perbedaannya, Anda bisa
        menggunakan newFixedThreadPool atau newCachedThreadPool
         */
        val executor = Executors.newSingleThreadExecutor()
        /*
        getMainLooper karena Anda ingin proses yang di dalam Handler dijalankan di main/ui thread.
        Pada kasus lain, jika Anda ingin Handler berjalan dengan thread yang sama dengan sebelumnya
        Anda bisa menggunakan myLooper
         */
        val handler = Handler(Looper.getMainLooper())

        btnStart.setOnClickListener {
            executor.execute {
                try {
                    for (i in 0..10) {
                        /*
                        Thread.Sleep yaitu dia akan mem-block proses secara keseluruhan. Sedangkan
                        jika kita menggunakan delay, yang berhenti hanya yang di dalam coroutine itu
                        saja, sedangkann coroutine lain masih bisa dijalankan
                         */
                        Thread.sleep(500) // 500 ms x 10 = 5000 ms / 5 detik.
                        val percentage = i * 10
                        /*
                        Kemudian ketika akan menampilkan status pada sebuah TextView, Anda tidak dapat
                        melakukannya langsung dari background thread, melainkan Anda harus melakukannya
                        di main thread, karena itulah dibutuhkan Handler.post untuk berpindah antar thread tersebut.
                         */
                        handler.post {
                            if (percentage == 100) {
                                tvStatus.setText(R.string.task_completed)
                            } else {
                                tvStatus.text =
                                    String.format(getString(R.string.compressing), percentage)
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
}