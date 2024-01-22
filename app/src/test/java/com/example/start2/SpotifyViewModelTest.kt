package com.example.start2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.foundation.layout.Box
import androidx.lifecycle.Observer
import com.example.start2.home.spotify.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.ArgumentMatchers.eq
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SpotifyViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: MockSpotifyRepository

    // Add more mock observers as needed for other LiveData objects
    @Mock
    private lateinit var topTracksObserver: Observer<TopTracksResponse>

    private lateinit var viewModel: SpotifyViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        @Spy
        @InjectMocks
        viewModel = SpotifyViewModel("testToken", isTest = true)
        viewModel.setRepository(repository)
        viewModel.topTracks.observeForever(topTracksObserver)
    }

    // Add test cases here
    @Test
    fun testGetUserTopTracks_Success() = runTest(UnconfinedTestDispatcher()) {
        // Arrange
        val mockTracks = listOf(
            Track(
                album = Album(
                    album_type = "single",
                    total_tracks = 10,
                    available_markets = listOf("US", "UK"),
                    external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                    href = "https://api.spotify.com/v1/albums/1",
                    id = "album1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                    name = "Album 1",
                    release_date = "2022-01-01",
                    release_date_precision = "day",
                    type = "album",
                    uri = "spotify:album:album1",
                    artists = listOf(Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    ))
                ),

                artists = listOf(
                    Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    )
                ),
                available_markets = listOf("US", "UK"),
                disc_number = 1,
                duration_ms = 210000,
                explicit = false,
                external_ids = mapOf("id1" to "123"),
                external_urls = mapOf("spotify" to "https://spotify.com/track1"),
                href = "https://api.spotify.com/v1/tracks/1",
                id = "track1",
                name = "Track 1",
                popularity = 80,
                preview_url = "https://preview.spotify.com/track1",
                track_number = 1,
                type = "track",
                uri = "spotify:track:track1",
                is_local = false
            ),
            Track(
                album = Album(
                    album_type = "single",
                    total_tracks = 10,
                    available_markets = listOf("US", "UK"),
                    external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                    href = "https://api.spotify.com/v1/albums/1",
                    id = "album1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                    name = "Album 1",
                    release_date = "2022-01-01",
                    release_date_precision = "day",
                    type = "album",
                    uri = "spotify:album:album1",
                    artists = listOf(Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    ))
                ),

                artists = listOf(
                    Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    )
                ),
                available_markets = listOf("US", "CA"),
                disc_number = 1,
                duration_ms = 250000,
                explicit = true,
                external_ids = mapOf("id2" to "456"),
                external_urls = mapOf("spotify" to "https://spotify.com/track2"),
                href = "https://api.spotify.com/v1/tracks/2",
                id = "track2",
                name = "Track 2",
                popularity = 85,
                preview_url = "https://preview.spotify.com/track2",
                track_number = 2,
                type = "track",
                uri = "spotify:track:track2",
                is_local = false
            )
        )
        val mockResponse = TopTracksResponse(
            href = "https://api.spotify.com/v1/me/top/tracks",
            limit = 20,
            next = "https://api.spotify.com/v1/me/top/tracks?offset=20",
            offset = 0,
            previous = null,
            total = 40,
            items = mockTracks)
        Mockito.`when`(repository.getUserTopTracks("testToken", "short_term", 0))
            .thenReturn(mockResponse)

        // Act
        viewModel.getUserTopTracks()

        // Assert
        Mockito.verify(repository).getUserTopTracks("testToken", "short_term", 0)
        Assert.assertEquals(mockResponse, viewModel.topTracks.value)
    }


    @Test
    fun testGetUserTopTracks_Failure() = runTest {
        // Arrange: Simulate a failure response from the repository
        Mockito.`when`(repository.getUserTopTracks(anyOrNull(), anyString(), anyInt())).thenReturn(null)

        // Act: Call the method in the ViewModel
        viewModel.getUserTopTracks()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getUserTopTracks(anyOrNull(), anyString(), anyInt())

        // Assert: Check if the topTracks LiveData is updated correctly
        Assert.assertNull(viewModel.topTracks.value)
    }
    @Test
    fun testGetUserTopTracks_WithParameters() = runTest {
        // Arrange: Define the term and offset parameters
        val term = "short_term"
        val offset = 20

        val mockTracks = listOf(
            Track(
                album = Album(
                    album_type = "single",
                    total_tracks = 10,
                    available_markets = listOf("US", "UK"),
                    external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                    href = "https://api.spotify.com/v1/albums/1",
                    id = "album1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                    name = "Album 1",
                    release_date = "2022-01-01",
                    release_date_precision = "day",
                    type = "album",
                    uri = "spotify:album:album1",
                    artists = listOf(Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    ))
                ),

                artists = listOf(
                    Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    )
                ),
                available_markets = listOf("US", "UK"),
                disc_number = 1,
                duration_ms = 210000,
                explicit = false,
                external_ids = mapOf("id1" to "123"),
                external_urls = mapOf("spotify" to "https://spotify.com/track1"),
                href = "https://api.spotify.com/v1/tracks/1",
                id = "track1",
                name = "Track 1",
                popularity = 80,
                preview_url = "https://preview.spotify.com/track1",
                track_number = 1,
                type = "track",
                uri = "spotify:track:track1",
                is_local = false
            ),
            Track(
                album = Album(
                    album_type = "single",
                    total_tracks = 10,
                    available_markets = listOf("US", "UK"),
                    external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                    href = "https://api.spotify.com/v1/albums/1",
                    id = "album1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                    name = "Album 1",
                    release_date = "2022-01-01",
                    release_date_precision = "day",
                    type = "album",
                    uri = "spotify:album:album1",
                    artists = listOf(Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    ))
                ),

                artists = listOf(
                    Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    )
                ),
                available_markets = listOf("US", "CA"),
                disc_number = 1,
                duration_ms = 250000,
                explicit = true,
                external_ids = mapOf("id2" to "456"),
                external_urls = mapOf("spotify" to "https://spotify.com/track2"),
                href = "https://api.spotify.com/v1/tracks/2",
                id = "track2",
                name = "Track 2",
                popularity = 85,
                preview_url = "https://preview.spotify.com/track2",
                track_number = 2,
                type = "track",
                uri = "spotify:track:track2",
                is_local = false
            )
        )


        // Mock response for TopTracksResponse
        val mockResponse = TopTracksResponse(
            href = "testHref",
            limit = 50,
            next = "testNext",
            offset = offset,
            previous = "testPrevious",
            total = 100,
            items = mockTracks
        )

        // When getUserTopTracks is called on the repository, return the mock response
        Mockito.`when`(repository.getUserTopTracks("testToken", "short_term", 0)).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel with specified parameters
        viewModel.selectedTerm.value = term

        viewModel.getUserTopTracks()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getUserTopTracks("testToken", "short_term", 0)

        // Assert: Check if the topTracks LiveData is updated as expected
        Assert.assertEquals(mockResponse, viewModel.topTracks.value)
    }
    @Test
    fun testGetUserTopArtists_Success() = runTest {
        // Arrange: Define the term and offset parameters
        val term = "long_term"
        val offset = 0

        // Prepare mock data for Artist
        val mockArtists = listOf(
            Artist(
                external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                href = "https://api.spotify.com/v1/artists/1",
                id = "artist1",
                images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                name = "Artist 1",
                type = "artist",
                uri = "spotify:artist:artist1"
            ),
            // Add more artists as needed
        )

        // Mock response for TopArtistsResponse
        val mockResponse = TopArtistsResponse(
            href = "testHref",
            limit = 50,
            next = "testNext",
            offset = offset,
            previous = "testPrevious",
            total = 100,
            items = mockArtists
        )

        // When getUserTopArtist is called on the repository, return the mock response
        Mockito.`when`(repository.getUserTopArtist("testToken", "long_term", 0)).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel with specified parameters
        viewModel.selectedTerm.value = term
        viewModel.getUserTopArtists()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getUserTopArtist("testToken", "long_term", 0)

        // Assert: Check if the topArtists LiveData is updated as expected
        Assert.assertEquals(mockResponse, viewModel.topArtists.value)
    }
    @Test
    fun testGetUserTopArtists_Failure() = runTest {
        // Arrange: Define the term and offset parameters
        val term = "medium_term"
        val offset = 50

        // When getUserTopArtist is called on the repository with specific parameters, return null to simulate a failure
        Mockito.`when`(repository.getUserTopArtist("testToken", "medium_term", 0)).thenReturn(null)

        // Act: Call the method in the ViewModel
        viewModel.selectedTerm.value = term
        viewModel.getUserTopArtists()

        // Assert: Verify the interaction with the repository with specific parameters
        Mockito.verify(repository).getUserTopArtist("testToken","medium_term", 0)

        // Assert: Check if the topArtists LiveData is not updated or remains null
        Assert.assertNull(viewModel.topArtists.value)
    }
    @Test
    fun testGetSelectedArtist_Success() = runTest {
        // Arrange: Define the artist ID
        val artistId = "artist1"

        // Define mock data for SpotifyArtistInfoResponse
        val mockArtistInfo = SpotifyArtistInfoResponse(
            external_urls = mapOf("spotify" to "https://spotify.com/artist/artist1"),
            followers = Followers(href = "sa",total = 1000),
            genres = listOf("Rock", "Indie"),
            href = "https://api.spotify.com/v1/artists/artist1",
            id = "artist1",
            images = listOf(
                ImageObject(url = "https://imageurl.com/artist1.jpg", height = 640, width = 640),
                ImageObject(url = "https://imageurl.com/artist1_2.jpg", height = 320, width = 320)
            ),
            name = "Artist 1",
            popularity = 80,
            type = "artist",
            uri = "spotify:artist:artist1"
        )

        // When getSelectedArtistInfo is called on the repository, return the mock response
        Mockito.`when`(repository.getSelectedArtistInfo("testToken", "artist1")).thenReturn(mockArtistInfo)

        // Act: Call the method in the ViewModel
        viewModel.saveSelectedArtist("artist1")
        viewModel.getSelectedArtist()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getSelectedArtistInfo("testToken", "artist1")

        // Assert: Check if the selectedArtistInfo LiveData is updated as expected
        Assert.assertEquals(mockArtistInfo, viewModel.selectedArtistInfo.value)
    }
    @Test
    fun testSearch_Success() = runTest {
        // Define the search query
        val searchQuery = "Queen"

        // Mock data for SpotifySearchResponse
        val mockSearchItems = listOf(
            SpotifySearchItem.TrackItem (
                track = Track(
                    album = Album(
                        album_type = "single",
                        total_tracks = 10,
                        available_markets = listOf("US", "UK"),
                        external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                        href = "https://api.spotify.com/v1/albums/1",
                        id = "album1",
                        images = listOf(
                            Image(
                                height = 640,
                                width = 640,
                                url = "https://imageurl.com/album1.jpg"
                            )
                        ),
                        name = "Album 1",
                        release_date = "2022-01-01",
                        release_date_precision = "day",
                        type = "album",
                        uri = "spotify:album:album1",
                        artists = listOf(
                            Artist(
                                external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                                href = "https://api.spotify.com/v1/artists/1",
                                id = "artist1",
                                images = listOf(
                                    Image(
                                        height = 640,
                                        width = 640,
                                        url = "https://imageurl.com/artist1.jpg"
                                    )
                                ),
                                name = "Artist 1",
                                type = "artist",
                                uri = "spotify:artist:artist1"
                            )
                        )
                    ),

                    artists = listOf(
                        Artist(
                            external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                            href = "https://api.spotify.com/v1/artists/1",
                            id = "artist1",
                            images = listOf(
                                Image(
                                    height = 640,
                                    width = 640,
                                    url = "https://imageurl.com/artist1.jpg"
                                )
                            ),
                            name = "Artist 1",
                            type = "artist",
                            uri = "spotify:artist:artist1"
                        )
                    ),
                    available_markets = listOf("US", "CA"),
                    disc_number = 1,
                    duration_ms = 250000,
                    explicit = true,
                    external_ids = mapOf("id2" to "456"),
                    external_urls = mapOf("spotify" to "https://spotify.com/track2"),
                    href = "https://api.spotify.com/v1/tracks/2",
                    id = "track2",
                    name = "Track 2",
                    popularity = 85,
                    preview_url = "https://preview.spotify.com/track2",
                    track_number = 2,
                    type = "track",
                    uri = "spotify:track:track2",
                    is_local = false
                )
            ),
            SpotifySearchItem.AlbumItem(
                album = Album(
                    album_type = "single",
                    total_tracks = 10,
                    available_markets = listOf("US", "UK"),
                    external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                    href = "https://api.spotify.com/v1/albums/1",
                    id = "album1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                    name = "Album 1",
                    release_date = "2022-01-01",
                    release_date_precision = "day",
                    type = "album",
                    uri = "spotify:album:album1",
                    artists = listOf(Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    ))
                )
            ),
            SpotifySearchItem.ArtistItem(
                artist = Artist(
                    external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                    href = "https://api.spotify.com/v1/artists/1",
                    id = "artist1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                    name = "Artist 1",
                    type = "artist",
                    uri = "spotify:artist:artist1"
                )
            )
        )

        val mockResponse = SpotifySearchResponse(
            href = "testHref",
            limit = 20,
            next = "testNext",
            offset = 0,
            previous = "testPrevious",
            total = 60,
            items = mockSearchItems
        )

        // When search is called on the repository, return the mock response
        Mockito.`when`(repository.search("testToken", searchQuery)).thenReturn(mockResponse)

        // Act: Call the search method in the ViewModel
        viewModel.search(searchQuery)

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).search("testToken", searchQuery)

        // Assert: Check if the searchResults LiveData is updated as expected
        Assert.assertEquals(mockResponse, viewModel.searchResults.value)
    }
    @Test
    fun testSearch_Failure() = runTest {
        // Arrange: Define a search query
        val searchQuery = "Beatles"

        // When search is called on the repository, return null to simulate a failure
        Mockito.`when`(repository.search("testToken", searchQuery)).thenReturn(null)

        // Act: Call the search method in the ViewModel
        viewModel.search(searchQuery)

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).search("testToken", searchQuery)

        // Assert: Check if the searchResults LiveData is not updated or remains null
        Assert.assertNull(viewModel.searchResults.value)
    }

    @Test
    fun testGetSelectedArtistTopTracks_Success() = runTest {
        // Arrange
        val artistId = "artistId"
        viewModel.saveSelectedArtist(artistId)
        val mockResponse = SpotifyArtistTopTrackResponse(
            tracks = listOf(
                Track(
                    album = Album(
                        album_type = "single",
                        total_tracks = 10,
                        available_markets = listOf("US", "UK"),
                        external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                        href = "https://api.spotify.com/v1/albums/1",
                        id = "album1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                        name = "Album 1",
                        release_date = "2022-01-01",
                        release_date_precision = "day",
                        type = "album",
                        uri = "spotify:album:album1",
                        artists = listOf(Artist(
                            external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                            href = "https://api.spotify.com/v1/artists/1",
                            id = "artist1",
                            images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                            name = "Artist 1",
                            type = "artist",
                            uri = "spotify:artist:artist1"
                        ))
                    ),

                    artists = listOf(
                        Artist(
                            external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                            href = "https://api.spotify.com/v1/artists/1",
                            id = "artist1",
                            images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                            name = "Artist 1",
                            type = "artist",
                            uri = "spotify:artist:artist1"
                        )
                    ),
                    available_markets = listOf("US", "UK"),
                    disc_number = 1,
                    duration_ms = 210000,
                    explicit = false,
                    external_ids = mapOf("id1" to "123"),
                    external_urls = mapOf("spotify" to "https://spotify.com/track1"),
                    href = "https://api.spotify.com/v1/tracks/1",
                    id = "track1",
                    name = "Track 1",
                    popularity = 80,
                    preview_url = "https://preview.spotify.com/track1",
                    track_number = 1,
                    type = "track",
                    uri = "spotify:track:track1",
                    is_local = false
                ),
                Track(
                    album = Album(
                        album_type = "single",
                        total_tracks = 10,
                        available_markets = listOf("US", "UK"),
                        external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                        href = "https://api.spotify.com/v1/albums/1",
                        id = "album1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                        name = "Album 1",
                        release_date = "2022-01-01",
                        release_date_precision = "day",
                        type = "album",
                        uri = "spotify:album:album1",
                        artists = listOf(Artist(
                            external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                            href = "https://api.spotify.com/v1/artists/1",
                            id = "artist1",
                            images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                            name = "Artist 1",
                            type = "artist",
                            uri = "spotify:artist:artist1"
                        ))
                    ),

                    artists = listOf(
                        Artist(
                            external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                            href = "https://api.spotify.com/v1/artists/1",
                            id = "artist1",
                            images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                            name = "Artist 1",
                            type = "artist",
                            uri = "spotify:artist:artist1"
                        )
                    ),
                    available_markets = listOf("US", "CA"),
                    disc_number = 1,
                    duration_ms = 250000,
                    explicit = true,
                    external_ids = mapOf("id2" to "456"),
                    external_urls = mapOf("spotify" to "https://spotify.com/track2"),
                    href = "https://api.spotify.com/v1/tracks/2",
                    id = "track2",
                    name = "Track 2",
                    popularity = 85,
                    preview_url = "https://preview.spotify.com/track2",
                    track_number = 2,
                    type = "track",
                    uri = "spotify:track:track2",
                    is_local = false
                )
            )
        )

        // Mock the repository call
        Mockito.`when`(repository.getSelectedArtistTopTracks("testToken", artistId)).thenReturn(mockResponse)

        // Act
        viewModel.getSelectedArtistTopTracks()

        // Assert
        Assert.assertEquals(mockResponse, viewModel.selectedArtistTopTracks.value)
    }

    @Test
    fun testGetSelectedArtistAlbums_Success()  = runTest {
        // Arrange =
        val artistId = "artistId"
        viewModel.saveSelectedArtist(artistId)


        val mockResponse = SpotifyArtistAlbumsResponse(
            href = "testHref",
            limit = 20,
            next = "testNext",
            offset = 0,
            previous = "testPrevious",
            total = 60,
            items = listOf(Album(
                album_type = "single",
                total_tracks = 10,
                available_markets = listOf("US", "UK"),
                external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                href = "https://api.spotify.com/v1/albums/1",
                id = "album1",
                images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                name = "Album 1",
                release_date = "2022-01-01",
                release_date_precision = "day",
                type = "album",
                uri = "spotify:album:album1",
                artists = listOf(Artist(
                    external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                    href = "https://api.spotify.com/v1/artists/1",
                    id = "artist1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                    name = "Artist 1",
                    type = "artist",
                    uri = "spotify:artist:artist1"
                ))
            ))
        )

        // Mock the repository call
        Mockito.`when`(repository.getSelectedArtistAlbums("testToken", artistId)).thenReturn(mockResponse)

        // Act
        viewModel.getSelectedArtistAlbums()

        // Assert
        Assert.assertEquals(mockResponse, viewModel.selectedArtistAlbums.value)
    }

    @Test
    fun testGetAlbumTracks_Success()  = runTest {
        // Arrange
        val albumId = "albumId"
        viewModel.saveSelectedAlbum(albumId)

        val mockResponse = SpotifyAlbumTracksResponse(href = "testHref",
            limit = 20,
            next = "testNext",
            offset = 0,
            previous = "testPrevious",
            total = 60,
            items = listOf(SimpleTrack(
                artists = listOf(
                    Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    )
                ),
                available_markets = listOf("US", "UK"),
                disc_number = 1,
                duration_ms = 200000, // duration in milliseconds (e.g., 200000 ms is 3 minutes and 20 seconds)
                explicit = false,
                external_urls = mapOf("spotify" to "https://spotify.com/track1"),
                href = "https://api.spotify.com/v1/tracks/1",
                id = "track1",
                name = "Track 1",
                preview_url = "https://preview.spotify.com/track1",
                track_number = 1,
                type = "track",
                uri = "spotify:track:track1",
                is_local = false
            ))
        )

        // Mock the repository call
        Mockito.`when`(repository.getAlbumTracks("testToken", albumId)).thenReturn(mockResponse)

        // Act
        viewModel.getAlbumTracks()

        // Assert
        Assert.assertEquals(mockResponse, viewModel.albumTracks.value)
    }

    @Test
    fun testGetSeveralTracks_Success()  = runTest {
        // Arrange
        val trackIds = "trackId1,trackId2"
        val mockResponse = SpotifyGetSeveralTracksResponse(listOf(
            Track(
                album = Album(
                    album_type = "single",
                    total_tracks = 10,
                    available_markets = listOf("US", "UK"),
                    external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                    href = "https://api.spotify.com/v1/albums/1",
                    id = "album1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                    name = "Album 1",
                    release_date = "2022-01-01",
                    release_date_precision = "day",
                    type = "album",
                    uri = "spotify:album:album1",
                    artists = listOf(Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    ))
                ),

                artists = listOf(
                    Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    )
                ),
                available_markets = listOf("US", "UK"),
                disc_number = 1,
                duration_ms = 210000,
                explicit = false,
                external_ids = mapOf("id1" to "123"),
                external_urls = mapOf("spotify" to "https://spotify.com/track1"),
                href = "https://api.spotify.com/v1/tracks/1",
                id = "track1",
                name = "Track 1",
                popularity = 80,
                preview_url = "https://preview.spotify.com/track1",
                track_number = 1,
                type = "track",
                uri = "spotify:track:track1",
                is_local = false
            ),
            Track(
                album = Album(
                    album_type = "single",
                    total_tracks = 10,
                    available_markets = listOf("US", "UK"),
                    external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                    href = "https://api.spotify.com/v1/albums/1",
                    id = "album1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                    name = "Album 1",
                    release_date = "2022-01-01",
                    release_date_precision = "day",
                    type = "album",
                    uri = "spotify:album:album1",
                    artists = listOf(Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    ))
                ),

                artists = listOf(
                    Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                        href = "https://api.spotify.com/v1/artists/1",
                        id = "artist1",
                        images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                        name = "Artist 1",
                        type = "artist",
                        uri = "spotify:artist:artist1"
                    )
                ),
                available_markets = listOf("US", "CA"),
                disc_number = 1,
                duration_ms = 250000,
                explicit = true,
                external_ids = mapOf("id2" to "456"),
                external_urls = mapOf("spotify" to "https://spotify.com/track2"),
                href = "https://api.spotify.com/v1/tracks/2",
                id = "track2",
                name = "Track 2",
                popularity = 85,
                preview_url = "https://preview.spotify.com/track2",
                track_number = 2,
                type = "track",
                uri = "spotify:track:track2",
                is_local = false
                )
            )
        )

        // Mock the repository call
        Mockito.`when`(repository.getSeveralTracks("testToken", trackIds)).thenReturn(mockResponse)

        // Act
        viewModel.getSeveralTracks("testToken" , trackIds)

        // Assert
        Assert.assertEquals(mockResponse, viewModel.severalTracks.value)
    }

    @Test
    fun testGetSelectedTrackInfo_Success()  = runTest {
        // Arrange
        val trackId = "trackId"
        viewModel.saveSelectedTrack(trackId)
        val mockResponse = Track(
            album = Album(
                album_type = "single",
                total_tracks = 10,
                available_markets = listOf("US", "UK"),
                external_urls = mapOf("spotify" to "https://spotify.com/album1"),
                href = "https://api.spotify.com/v1/albums/1",
                id = "album1",
                images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
                name = "Album 1",
                release_date = "2022-01-01",
                release_date_precision = "day",
                type = "album",
                uri = "spotify:album:album1",
                artists = listOf(Artist(
                    external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                    href = "https://api.spotify.com/v1/artists/1",
                    id = "artist1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                    name = "Artist 1",
                    type = "artist",
                    uri = "spotify:artist:artist1"
                ))
            ),

            artists = listOf(
                Artist(
                    external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                    href = "https://api.spotify.com/v1/artists/1",
                    id = "artist1",
                    images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                    name = "Artist 1",
                    type = "artist",
                    uri = "spotify:artist:artist1"
                )
            ),
            available_markets = listOf("US", "CA"),
            disc_number = 1,
            duration_ms = 250000,
            explicit = true,
            external_ids = mapOf("id2" to "456"),
            external_urls = mapOf("spotify" to "https://spotify.com/track2"),
            href = "https://api.spotify.com/v1/tracks/2",
            id = "track2",
            name = "Track 2",
            popularity = 85,
            preview_url = "https://preview.spotify.com/track2",
            track_number = 2,
            type = "track",
            uri = "spotify:track:track2",
            is_local = false
        )


        // Mock the repository call
        Mockito.`when`(repository.getSelectedTrackInfo("testToken", trackId)).thenReturn(mockResponse)

        // Act
        viewModel.getSelectedTrack()

        // Assert
        Assert.assertEquals(mockResponse, viewModel.selectedTrackInfo.value)
    }

    @Test
    fun testGetSelectedAlbumInfo_Success()  = runTest {
        // Arrange
        val albumId = "albumId"
        viewModel.saveSelectedAlbum(albumId)
        val mockResponse = Album(
            album_type = "single",
            total_tracks = 10,
            available_markets = listOf("US", "UK"),
            external_urls = mapOf("spotify" to "https://spotify.com/album1"),
            href = "https://api.spotify.com/v1/albums/1",
            id = "album1",
            images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/album1.jpg")),
            name = "Album 1",
            release_date = "2022-01-01",
            release_date_precision = "day",
            type = "album",
            uri = "spotify:album:album1",
            artists = listOf(Artist(
                external_urls = mapOf("spotify" to "https://spotify.com/artist1"),
                href = "https://api.spotify.com/v1/artists/1",
                id = "artist1",
                images = listOf(Image(height = 640, width = 640, url = "https://imageurl.com/artist1.jpg")),
                name = "Artist 1",
                type = "artist",
                uri = "spotify:artist:artist1"
            ))
        )

        // Mock the repository call
        Mockito.`when`(repository.getSelectedAlbumInfo("testToken", albumId)).thenReturn(mockResponse)

        // Act
        viewModel.getSelectedAlbum()

        // Assert
        Assert.assertEquals(mockResponse, viewModel.selectedAlbumInfo.value)
    }
    @Test
    fun testGetSelectedArtistTopTracks_Failure()  = runTest{
        // Arrange
        val artistId = "artistId"
        viewModel.saveSelectedArtist(artistId)
        Mockito.`when`(repository.getSelectedArtistTopTracks("testToken", artistId)).thenReturn(null)

        // Act
        viewModel.getSelectedArtistTopTracks()

        // Assert
        Assert.assertNull(viewModel.selectedArtistTopTracks.value)
    }

    @Test
    fun testGetSelectedArtistAlbums_Failure()  = runTest{
        // Arrange
        val artistId = "artistId"
        viewModel.saveSelectedArtist(artistId)

        Mockito.`when`(repository.getSelectedArtistAlbums("testToken", artistId)).thenReturn(null)

        // Act
        viewModel.getSelectedArtistAlbums()

        // Assert
        Assert.assertNull(viewModel.selectedArtistAlbums.value)
    }

    @Test
    fun testGetAlbumTracks_Failure()  = runTest{
        // Arrange
        val albumId = "albumId"
        viewModel.saveSelectedAlbum(albumId)
        Mockito.`when`(repository.getAlbumTracks("testToken", albumId)).thenReturn(null)

        // Act
        viewModel.getAlbumTracks()

        // Assert
        Assert.assertNull(viewModel.albumTracks.value)
    }

    @Test
    fun testGetSeveralTracks_Failure()  = runTest{
        // Arrange
        val trackIds = "trackId1,trackId2"
        viewModel.saveSelectedTrack("trackId")
        Mockito.`when`(repository.getSeveralTracks("testToken", trackIds)).thenReturn(null)

        // Act
        viewModel.getSeveralTracks("testToken",trackIds)

        // Assert
        Assert.assertNull(viewModel.severalTracks.value)
    }

    @Test
    fun testGetSelectedTrackInfo_Failure()  = runTest {
        // Arrange
        val trackId = "trackId"
        viewModel.saveSelectedTrack(trackId)
        Mockito.`when`(repository.getSelectedTrackInfo("testToken", trackId)).thenReturn(null)

        // Act
        viewModel.getSelectedTrack()

        // Assert
        Assert.assertNull(viewModel.selectedTrackInfo.value)
    }

    @Test
    fun testGetSelectedAlbumInfo_Failure()  = runTest{
        // Arrange
        val albumId = "albumId"
        viewModel.saveSelectedAlbum(albumId)
        Mockito.`when`(repository.getSelectedAlbumInfo("testToken", albumId)).thenReturn(null)

        // Act
        viewModel.getSelectedAlbum()

        // Assert
        Assert.assertNull(viewModel.selectedAlbumInfo.value)
    }






    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
