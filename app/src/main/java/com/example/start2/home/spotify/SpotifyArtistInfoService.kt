import com.example.start2.home.spotify.SpotifyArtistInfoResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SpotifyArtistInfoService {
    @GET("/v1/artists/{id}")
    suspend fun getArtistInfo(
        @Header("Authorization") authHeader: String,
        @Path("id") artistId: String
    ): Response<SpotifyArtistInfoResponse>
}

object SpotifyArtistInfoServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: SpotifyArtistInfoService by lazy {
        retrofit.create(SpotifyArtistInfoService::class.java)
    }
}
