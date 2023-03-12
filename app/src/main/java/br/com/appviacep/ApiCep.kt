package br.com.appviacep

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiCep {
    @GET("{cep}/json")
    fun buscaCep(@Path("cep") cep: String): Call<Endereco>
}