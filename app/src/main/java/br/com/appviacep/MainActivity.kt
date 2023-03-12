package br.com.appviacep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import br.com.appviacep.databinding.ActivityMainBinding
import br.com.cepgpt.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var cep: TextView
private lateinit var logradouro: TextView
private lateinit var complemento: TextView
private lateinit var bairro: TextView
private lateinit var localidade: TextView
private lateinit var uf: TextView

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        buscaResults()

        binding.btnbuscar.setOnClickListener {
            buscaCepAPi()
        }

    }

    private fun buscaResults() {
        cep = binding.cep
        logradouro = binding.logradouro
        complemento = binding.complemento
        bairro = binding.bairro
        localidade = binding.localidade
        uf = binding.uf
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://viacep.com.br/ws/") // URL base da API
        .addConverterFactory(GsonConverterFactory.create()) // Conversor Gson para converter JSON em objetos Kotlin
        .build()

    private val cepApi = retrofit.create(ApiCep::class.java)

    private fun buscaCepAPi (){

        val input = binding.input.text.toString()

        val call = cepApi.buscaCep(input) // Chamada assíncrona da API para o CEP

        call.enqueue(object : Callback<Endereco> {
            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                if (response.isSuccessful) {
                    val address = response.body() // Endereço obtido com sucesso
                    cep.text = "CEP: ${address?.cep.toString()}"
                    logradouro.text = "Logradouro: ${address?.logradouro.toString()}"
                    complemento.text = "Complemento: ${address?.complemento.toString()}"
                    bairro.text = "Bairro: ${address?.bairro.toString()}"
                    localidade.text = "Localidade ${address?.localidade.toString()}"
                    uf.text = "UF: ${address?.uf.toString()}"

                } else {
                    Toast.makeText(this@MainActivity, "ocorreu um erro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Endereco>, t: Throwable) {
                Toast.makeText(this@MainActivity, "ocorreu um erro", Toast.LENGTH_SHORT).show()
            }
        })

    }


}