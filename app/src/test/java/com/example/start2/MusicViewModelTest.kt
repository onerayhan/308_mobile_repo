package com.example.start2

import android.content.ContentResolver
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.start2.services_and_responses.*
import com.example.start2.viewmodels.IMusicRepository
import com.example.start2.viewmodels.MockMusicRepository
import com.example.start2.viewmodels.Music
import com.example.start2.viewmodels.MusicRepository
import com.example.start2.viewmodels.MusicViewModel
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
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class MusicViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: MockMusicRepository
    @Mock
    private lateinit var userGenrePreferencesObserver: Observer<UserGenrePreferencesResponse>

    private lateinit var viewModel: MusicViewModel
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        @Spy
        @InjectMocks
        viewModel = MusicViewModel("testUser", isTest = true)
        //repository = MockMusicRepository()
        viewModel.setRepository(repository)
        viewModel.userGenrePreferences.observeForever(userGenrePreferencesObserver)
    }
    @Test
    fun testGetUserGenrePreferences_Success() = runTest(UnconfinedTestDispatcher()) {
        // Create a list of GenreDetails
        val mockGenres = listOf(
            GenreDetails(genre = "Rock", count = 10),
            GenreDetails(genre = "Jazz", count = 5)
        )

        val mockResponse = UserGenrePreferencesResponse(genres = mockGenres)
        Mockito.`when`(repository.getUserGenrePreferences("testUser")).thenReturn(mockResponse)

        viewModel.getUserGenrePreferences()

        Mockito.verify(repository).getUserGenrePreferences("testUser")
        Assert.assertEquals(mockResponse, viewModel.userGenrePreferences.value)
    }
    @Test
    fun testGetUserGenrePreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getUserGenrePreferences("testUser")).thenReturn(null) // Simulating failure

        // Act
        viewModel.getUserGenrePreferences()

        // Assert
        Mockito.verify(repository).getUserGenrePreferences("testUser")
        Assert.assertNull(viewModel.userGenrePreferences.value)
    }


    @Test
    fun testGetUserAlbumPreferences_Success() = runTest {
        // Mock data for AlbumDetails
        val mockAlbums = listOf(
            AlbumDetails(album = "Album1", count = 15),
            AlbumDetails(album = "Album2", count = 20)
        )

        // Mock response for UserAlbumPreferencesResponse
        val mockResponse = UserAlbumPreferencesResponse(albums = mockAlbums)

        // When getUserAlbumPreferences is called on the repository, return the mock response
        Mockito.`when`(repository.getUserAlbumPreferences("testUser")).thenReturn(mockResponse)

        // Call the method in the ViewModel
        viewModel.getUserAlbumPreferences()

        // Verify the interaction with the repository
        Mockito.verify(repository).getUserAlbumPreferences("testUser")

        // Assert that the LiveData value is updated as expected
        Assert.assertEquals(mockResponse, viewModel.userAlbumPreferences.value)
    }
    @Test
    fun testGetUserAlbumPreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getUserAlbumPreferences("testUser")).thenReturn(null) // Simulate failure

        // Act
        viewModel.getUserAlbumPreferences()

        // Assert
        Mockito.verify(repository).getUserAlbumPreferences("testUser")
        Assert.assertNull(viewModel.userAlbumPreferences.value)
    }


    @Test
    fun testGetUserPerformerPreferences_Success() = runTest {
        // Arrange: Mock data for UserPerformerPreferencesResponse
        val mockPerformerPrefs = listOf(
            PerformerDetails(performer = "Performer1", count = 15),
            PerformerDetails(performer = "Performer2", count = 25)
        )
        val mockResponse = UserPerformerPreferencesResponse(mockPerformerPrefs)

        // Stub the repository call
        Mockito.`when`(repository.getUserPerformerPreferences("testUser")).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel
        viewModel.getUserPerformerPreferences()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getUserPerformerPreferences("testUser")

        // Assert: Check if the LiveData is updated correctly
        val observer: Observer<UserPerformerPreferencesResponse> = mock()
        viewModel.userPerformerPreferences.observeForever(observer)
        Mockito.verify(observer).onChanged(mockResponse)
    }
    @Test
    fun testGetUserPerformerPreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getUserPerformerPreferences("testUser")).thenReturn(null) // Simulating failure

        // Act
        viewModel.getUserPerformerPreferences()

        // Assert
        Mockito.verify(repository).getUserPerformerPreferences("testUser")
        Assert.assertNull(viewModel.userPerformerPreferences.value)
    }

    @Test
    fun testGetAllGenrePreferences_Success() = runTest(UnconfinedTestDispatcher()) {
        // Create a list of GenreDetails
        val mockGenres = listOf(
            GenreDetails(genre = "Rock", count = 10),
            GenreDetails(genre = "Jazz", count = 5)
        )

        val mockResponse = UserGenrePreferencesResponse(genres = mockGenres)
        Mockito.`when`(repository.getAllGenrePreferences()).thenReturn(mockResponse)

        viewModel.getAllGenrePreferences()

        Mockito.verify(repository).getAllGenrePreferences()
        Assert.assertEquals(mockResponse, viewModel.userGenrePreferences.value)
    }
    @Test
    fun testGetAllGenrePreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getAllGenrePreferences()).thenReturn(null) // Simulating failure

        // Act
        viewModel.getAllGenrePreferences()

        // Assert
        Mockito.verify(repository).getAllGenrePreferences()
        Assert.assertNull(viewModel.userGenrePreferences.value)
    }


    @Test
    fun testGetAllAlbumPreferences_Success() = runTest {
        // Mock data for AlbumDetails
        val mockAlbums = listOf(
            AlbumDetails(album = "Album1", count = 15),
            AlbumDetails(album = "Album2", count = 20)
        )

        // Mock response for UserAlbumPreferencesResponse
        val mockResponse = UserAlbumPreferencesResponse(albums = mockAlbums)

        // When getUserAlbumPreferences is called on the repository, return the mock response
        Mockito.`when`(repository.getAllAlbumPreferences()).thenReturn(mockResponse)

        // Call the method in the ViewModel
        viewModel.getAllAlbumPreferences()

        // Verify the interaction with the repository
        Mockito.verify(repository).getAllAlbumPreferences()

        // Assert that the LiveData value is updated as expected
        Assert.assertEquals(mockResponse, viewModel.userAlbumPreferences.value)
    }
    @Test
    fun testGetAllAlbumPreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getAllAlbumPreferences()).thenReturn(null) // Simulate failure

        // Act
        viewModel.getAllAlbumPreferences()

        // Assert
        Mockito.verify(repository).getAllAlbumPreferences()
        Assert.assertNull(viewModel.userAlbumPreferences.value)
    }


    @Test
    fun testGetAllPerformerPreferences_Success() = runTest {
        // Arrange: Mock data for UserPerformerPreferencesResponse
        val mockPerformerPrefs = listOf(
            PerformerDetails(performer = "Performer1", count = 15),
            PerformerDetails(performer = "Performer2", count = 25)
        )
        val mockResponse = UserPerformerPreferencesResponse(mockPerformerPrefs)

        // Stub the repository call
        Mockito.`when`(repository.getAllPerformerPreferences()).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel
        viewModel.getAllPerformerPreferences()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getAllPerformerPreferences()

        // Assert: Check if the LiveData is updated correctly
        val observer: Observer<UserPerformerPreferencesResponse> = mock()
        viewModel.userPerformerPreferences.observeForever(observer)
        Mockito.verify(observer).onChanged(mockResponse)
    }
    @Test
    fun testGetAllPerformerPreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getAllPerformerPreferences()).thenReturn(null) // Simulating failure

        // Act
        viewModel.getAllPerformerPreferences()

        // Assert
        Mockito.verify(repository).getAllPerformerPreferences()
        Assert.assertNull(viewModel.userPerformerPreferences.value)
    }

    @Test
    fun testGetUserFollowingsGenrePreferences_Success() = runTest(UnconfinedTestDispatcher()) {
        // Create a list of GenreDetails
        val mockGenres = listOf(
            GenreDetails(genre = "Rock", count = 10),
            GenreDetails(genre = "Jazz", count = 5)
        )

        val mockResponse = UserGenrePreferencesResponse(genres = mockGenres)
        Mockito.`when`(repository.getUserFollowingsGenrePreferences("testUser")).thenReturn(mockResponse)

        viewModel.getUserFollowingsGenrePreferences()

        Mockito.verify(repository).getUserFollowingsGenrePreferences("testUser")
        Assert.assertEquals(mockResponse, viewModel.userGenrePreferences.value)
    }
    @Test
    fun testGetUserFollowingsGenrePreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getUserFollowingsGenrePreferences("testUser")).thenReturn(null) // Simulating failure

        // Act
        viewModel.getUserFollowingsGenrePreferences()

        // Assert
        Mockito.verify(repository).getUserFollowingsGenrePreferences("testUser")
        Assert.assertNull(viewModel.userGenrePreferences.value)
    }


    @Test
    fun testGetUserFollowingsAlbumPreferences_Success() = runTest {
        // Mock data for AlbumDetails
        val mockAlbums = listOf(
            AlbumDetails(album = "Album1", count = 15),
            AlbumDetails(album = "Album2", count = 20)
        )

        // Mock response for UserAlbumPreferencesResponse
        val mockResponse = UserAlbumPreferencesResponse(albums = mockAlbums)

        // When getUserAlbumPreferences is called on the repository, return the mock response
        Mockito.`when`(repository.getUserFollowingsAlbumPreferences("testUser")).thenReturn(mockResponse)

        // Call the method in the ViewModel
        viewModel.getUserFollowingsAlbumPreferences()

        // Verify the interaction with the repository
        Mockito.verify(repository).getUserFollowingsAlbumPreferences("testUser")

        // Assert that the LiveData value is updated as expected
        Assert.assertEquals(mockResponse, viewModel.userAlbumPreferences.value)
    }
    @Test
    fun testGetUserFollowingsAlbumPreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getUserFollowingsAlbumPreferences("testUser")).thenReturn(null) // Simulate failure

        // Act
        viewModel.getUserFollowingsAlbumPreferences()

        // Assert
        Mockito.verify(repository).getUserFollowingsAlbumPreferences("testUser")
        Assert.assertNull(viewModel.userAlbumPreferences.value)
    }


    @Test
    fun testGetUserFollowingsPerformerPreferences_Success() = runTest {
        // Arrange: Mock data for UserPerformerPreferencesResponse
        val mockPerformerPrefs = listOf(
            PerformerDetails(performer = "Performer1", count = 15),
            PerformerDetails(performer = "Performer2", count = 25)
        )
        val mockResponse = UserPerformerPreferencesResponse(mockPerformerPrefs)

        // Stub the repository call
        Mockito.`when`(repository.getUserFollowingsPerformerPreferences("testUser")).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel
        viewModel.getUserFollowingsPerformerPreferences()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getUserFollowingsPerformerPreferences("testUser")

        // Assert: Check if the LiveData is updated correctly
        val observer: Observer<UserPerformerPreferencesResponse> = mock()
        viewModel.userPerformerPreferences.observeForever(observer)
        Mockito.verify(observer).onChanged(mockResponse)
    }
    @Test
    fun testGetUserFollowingsPerformerPreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getUserFollowingsPerformerPreferences("testUser")).thenReturn(null) // Simulating failure

        // Act
        viewModel.getUserFollowingsPerformerPreferences()

        // Assert
        Mockito.verify(repository).getUserFollowingsPerformerPreferences("testUser")
        Assert.assertNull(viewModel.userPerformerPreferences.value)
    }
    @Test
    fun testGetGroupGenrePreferences_Success() = runTest(UnconfinedTestDispatcher()) {
        // Create a list of GenreDetails
        val mockGenres = listOf(
            GenreDetails(genre = "Rock", count = 10),
            GenreDetails(genre = "Jazz", count = 5)
        )

        val mockResponse = UserGenrePreferencesResponse(genres = mockGenres)
        Mockito.`when`(repository.getGroupGenrePreferences("testUser")).thenReturn(mockResponse)

        viewModel.getGroupGenrePreferences()

        Mockito.verify(repository).getGroupGenrePreferences("testUser")
        Assert.assertEquals(mockResponse, viewModel.userGenrePreferences.value)
    }
    @Test
    fun testGetGroupGenrePreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getGroupGenrePreferences("testUser")).thenReturn(null) // Simulating failure

        // Act
        viewModel.getGroupGenrePreferences()

        // Assert
        Mockito.verify(repository).getGroupGenrePreferences("testUser")
        Assert.assertNull(viewModel.userGenrePreferences.value)
    }


    @Test
    fun testGetGroupAlbumPreferences_Success() = runTest {
        // Mock data for AlbumDetails
        val mockAlbums = listOf(
            AlbumDetails(album = "Album1", count = 15),
            AlbumDetails(album = "Album2", count = 20)
        )

        // Mock response for UserAlbumPreferencesResponse
        val mockResponse = UserAlbumPreferencesResponse(albums = mockAlbums)

        // When getUserAlbumPreferences is called on the repository, return the mock response
        Mockito.`when`(repository.getGroupAlbumPreferences("testUser")).thenReturn(mockResponse)

        // Call the method in the ViewModel
        viewModel.getGroupAlbumPreferences()

        // Verify the interaction with the repository
        Mockito.verify(repository).getGroupAlbumPreferences("testUser")

        // Assert that the LiveData value is updated as expected
        Assert.assertEquals(mockResponse, viewModel.userAlbumPreferences.value)
    }
    @Test
    fun testGetGroupAlbumPreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getGroupAlbumPreferences("testUser")).thenReturn(null) // Simulate failure

        // Act
        viewModel.getGroupAlbumPreferences()

        // Assert
        Mockito.verify(repository).getGroupAlbumPreferences("testUser")
        Assert.assertNull(viewModel.userAlbumPreferences.value)
    }


    @Test
    fun testGetGroupPerformerPreferences_Success() = runTest {
        // Arrange: Mock data for UserPerformerPreferencesResponse
        val mockPerformerPrefs = listOf(
            PerformerDetails(performer = "Performer1", count = 15),
            PerformerDetails(performer = "Performer2", count = 25)
        )
        val mockResponse = UserPerformerPreferencesResponse(mockPerformerPrefs)

        // Stub the repository call
        Mockito.`when`(repository.getGroupPerformerPreferences("testUser")).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel
        viewModel.getGroupPerformerPreferences()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getGroupPerformerPreferences("testUser")

        // Assert: Check if the LiveData is updated correctly
        val observer: Observer<UserPerformerPreferencesResponse> = mock()
        viewModel.userPerformerPreferences.observeForever(observer)
        Mockito.verify(observer).onChanged(mockResponse)
    }
    @Test
    fun testGetGroupPerformerPreferences_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getGroupPerformerPreferences("testUser")).thenReturn(null) // Simulating failure

        // Act
        viewModel.getGroupPerformerPreferences()

        // Assert
        Mockito.verify(repository).getGroupPerformerPreferences("testUser")
        Assert.assertNull(viewModel.userPerformerPreferences.value)
    }

    @Test
    fun testPostTracks() = runTest {
            // Mock data for Music objects
            val mockTracks = listOf(
                Music(
                    song_name = "Song1", length = "3:30", tempo = 120,
                    recording_type = "Studio", listens = 100,
                    release_year = "2020", added_timestamp = "2020-01-01",
                    album_name = "Album1", performer_name = "Artist1",
                    genre = "Rock", mood = "Energetic", instrument = "Guitar"
                ),
                Music(
                    song_name = "Song2", length = "4:00", tempo = 130,
                    recording_type = "Live", listens = 150,
                    release_year = "2021", added_timestamp = "2021-01-01",
                    album_name = "Album2", performer_name = "Artist2",
                    genre = "Jazz", mood = "Relaxed", instrument = "Saxophone"
                )
            )

            val mockResponse = AddSongsBatchResponse(
                results = listOf(
                    ResultMessage(message = "Track 1 successfully added"),
                    ResultMessage(message = "Track 2 successfully added")
                )
            )
            // When postTracks is called on the repository, return the mock response
            Mockito.`when`(repository.postTracks("testUser", mockTracks)).thenReturn(mockResponse)

            // Call the method in the ViewModel
            viewModel.postTracks(mockTracks)

            // Verify the interaction with the repository
            Mockito.verify(repository).postTracks("testUser", mockTracks)

            // Verify that batchResult LiveData is updated as expected
            // Replace 'batchResult' with the correct LiveData object name if different
            val observer: Observer<Boolean> = mock()
            viewModel.batchResult.observeForever(observer)
            Mockito.verify(observer).onChanged(true)
    }
    @Test
    fun testGetRecommendation() = runTest {
        val mockRecommendations = listOf(
            RecommendationsResponseItem(
                album = "Album1",
                genre = "Rock",
                performer = "Artist1",
                songId = 101,
                songsName = "Song1",
                username = "user1"
            ),
            RecommendationsResponseItem(
                album = "Album2",
                genre = "Jazz",
                performer = "Artist2",
                songId = 102,
                songsName = "Song2",
                username = "user2"
            )
        )

        val mockResponse = mockRecommendations
        // Stub the repository call
        Mockito.`when`(repository.getRecommendationsFromDB(token = "testUser", criteriaString = "genre", targetAudience = "all")).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel
        viewModel.getRecommendation("genre", "all")

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getRecommendationsFromDB("testUser", "genre", "all")

        // Assert: Check if the LiveData is updated correctly
        val observer: Observer<RecommendationsResponse> = mock()
        viewModel.recommendationResults.observeForever(observer)
        Mockito.verify(observer).onChanged(mockResponse)
    }



    @Test
    fun testGetUserSongRatings_Success() = runTest {
        // Mock data for UserGetSongRatingsResponse
        val mockSongRatings = listOf(
            UserSongRating(song_id = "song1", rating = 5, rating_timestamp = "2022-01-01T12:00:00"),
            UserSongRating(song_id = "song2", rating = 4, rating_timestamp = "2022-01-02T13:00:00")
        )
        val mockResponse = UserGetSongRatingsResponse(mockSongRatings)
        // Stub the repository call
        Mockito.`when`(repository.getUserSongRatings("testUser")).thenReturn(mockResponse)
        // Act: Call the method in the ViewModel
        viewModel.getUserSongRatings()
        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getUserSongRatings("testUser")
        // Assert: Check if the LiveData is updated correctly
        val observer: Observer<UserGetSongRatingsResponse> = mock()
        viewModel.userSongRatings.observeForever(observer)
        Mockito.verify(observer).onChanged(mockResponse)
    }
    @Test
    fun testGetUserSongRatings_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getUserSongRatings("testUser")).thenReturn(null) // Simulate failure

        // Act
        viewModel.getUserSongRatings()

        // Assert
        Mockito.verify(repository).getUserSongRatings("testUser")
        Assert.assertNull(viewModel.userSongRatings.value)
    }

    @Test
    fun testGetUserAlbumRatings_Success() = runTest {
        // Arrange: Mock data for UserGetAlbumRatingsResponse
        val mockAlbumRatings = listOf(
            UserAlbumRating(album_id = "album1", rating = 4, rating_timestamp = "2022-01-01T12:00:00"),
            UserAlbumRating(album_id = "album2", rating = 5, rating_timestamp = "2022-01-02T13:00:00")
        )
        val mockResponse = UserGetAlbumRatingsResponse(mockAlbumRatings)

        // Stub the repository call
        Mockito.`when`(repository.getUserAlbumRatings("testUser")).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel
        viewModel.getUserAlbumRatings()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getUserAlbumRatings("testUser")

        // Assert: Check if the LiveData is updated correctly
        val observer: Observer<UserGetAlbumRatingsResponse> = mock()
        viewModel.userAlbumRatings.observeForever(observer)
        Mockito.verify(observer).onChanged(mockResponse)
    }
    @Test
    fun testGetUserAlbumRatings_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getUserAlbumRatings("testUser")).thenReturn(null) // Simulating failure

        // Act
        viewModel.getUserAlbumRatings()

        // Assert
        Mockito.verify(repository).getUserAlbumRatings("testUser")
        Assert.assertNull(viewModel.userAlbumRatings.value)
    }

    @Test
    fun testGetUserPerformerRatings_Success() = runTest {
        // Arrange: Mock data for UserGetPerformerRatingsResponse
        val mockPerformerRatings = listOf(
            UserPerformerRating(performer_id = "performer1", rating = 4, rating_timestamp = "2022-01-01T12:00:00"),
            UserPerformerRating(performer_id = "performer2", rating = 5, rating_timestamp = "2022-01-02T13:00:00")
        )
        val mockResponse = UserGetPerformerRatingsResponse(mockPerformerRatings)

        // Stub the repository call
        Mockito.`when`(repository.getUserPerformerRatings("testUser")).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel
        viewModel.getUserPerformerRatings()

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getUserPerformerRatings("testUser")

        // Assert: Check if the LiveData is updated correctly
        val observer: Observer<UserGetPerformerRatingsResponse> = mock()
        viewModel.userPerformerRatings.observeForever(observer)
        Mockito.verify(observer).onChanged(mockResponse)
    }
    @Test
    fun testGetUserPerformerRatings_Failure() = runTest {
        // Arrange
        Mockito.`when`(repository.getUserPerformerRatings("testUser")).thenReturn(null) // Simulating failure

        // Act
        viewModel.getUserPerformerRatings()

        // Assert
        Mockito.verify(repository).getUserPerformerRatings("testUser")
        Assert.assertNull(viewModel.userPerformerRatings.value)
    }

    @Test
    fun testParseAndSaveMusics() = runTest {
        // Arrange: Mock file content
        val fileContent = """
        Song1,3:30,120,Studio,100,2020,2020-01-01,Album1,Artist1,Rock,Energetic,Guitar
        Song2,4:00,130,Live,150,2021,2021-01-02,Album2,Artist2,Jazz,Relaxed,Saxophone
    """.trimIndent()

        // Expected Music objects after parsing the file content
        val expectedMusics = listOf(
            Music(song_name = "Song1", length = "3:30", tempo = 120, recording_type = "Studio",
                listens = 100, release_year = "2020", added_timestamp = "2020-01-01",
                album_name = "Album1", performer_name = "Artist1", genre = "Rock",
                mood = "Energetic", instrument = "Guitar"),
            Music(song_name = "Song2", length = "4:00", tempo = 130, recording_type = "Live",
                listens = 150, release_year = "2021", added_timestamp = "2021-01-02",
                album_name = "Album2", performer_name = "Artist2", genre = "Jazz",
                mood = "Relaxed", instrument = "Saxophone")
        )

        // Act: Call the method in the ViewModel
        viewModel.parseAndSaveMusics(fileContent)

        // Assert: Verify that parsedMusics LiveData is updated correctly
        val observer: Observer<List<Music>> = mock()
        viewModel.parsedMusics.observeForever(observer)
        Mockito.verify(observer).onChanged(expectedMusics)
    }
    @Test
    fun testOnOptionSelected_SingleOption() = runTest {
        // Arrange: Select only one option
        val selectedOption = listOf("MyGenrePrefs")
        val mockGenres = listOf(
            GenreDetails(genre = "Rock", count = 10),
            GenreDetails(genre = "Jazz", count = 5)
        )
        val mockResponse = UserGenrePreferencesResponse(mockGenres)

        // Stub the repository call for the selected option
        Mockito.`when`(repository.getUserGenrePreferences("testUser")).thenReturn(mockResponse)

        // Act: Call the method in the ViewModel
        viewModel.onOptionSelected(selectedOption)

        // Assert: Verify the interaction with the repository
        Mockito.verify(repository).getUserGenrePreferences("testUser")

        // Assert: Check if the userGenrePreferences LiveData is updated correctly
        val observer: Observer<UserGenrePreferencesResponse> = mock()
        viewModel.userGenrePreferences.observeForever(observer)
        Mockito.verify(observer).onChanged(mockResponse)
    }
    @Test
    fun testOnOptionSelected_MultipleOptions() = runTest {
        // Arrange: Select multiple options
        val selectedOptions = listOf("MyGenrePrefs", "MyAlbumPrefs")
        val mockGenreResponse = UserGenrePreferencesResponse(
            listOf(GenreDetails(genre = "Rock", count = 10))
        )
        val mockAlbumResponse = UserAlbumPreferencesResponse(
            listOf(AlbumDetails(album = "Album1", count = 3))
        )

        // Stub the repository calls for the selected options
        Mockito.`when`(repository.getUserGenrePreferences("testUser")).thenReturn(mockGenreResponse)
        Mockito.`when`(repository.getUserAlbumPreferences("testUser")).thenReturn(mockAlbumResponse)

        // Act: Call the method in the ViewModel
        viewModel.onOptionSelected(selectedOptions)

        // Assert: Verify the interactions with the repository
        Mockito.verify(repository).getUserGenrePreferences("testUser")
        Mockito.verify(repository).getUserAlbumPreferences("testUser")

        // Assert: Check if the LiveData objects are updated correctly
        val genreObserver: Observer<UserGenrePreferencesResponse> = mock()
        val albumObserver: Observer<UserAlbumPreferencesResponse> = mock()

        viewModel.userGenrePreferences.observeForever(genreObserver)
        viewModel.userAlbumPreferences.observeForever(albumObserver)

        Mockito.verify(genreObserver).onChanged(mockGenreResponse)
        Mockito.verify(albumObserver).onChanged(mockAlbumResponse)
    }
    @Test
    fun testOnOptionSelected_MyGroupOptions() = runTest {
        // Arrange: Select three options from the "My" group
        val selectedOptions = listOf("MyGenrePrefs", "MyAlbumPrefs", "MyPerformerPrefs")

        // Assume mock responses for each selected option
        val mockGenreResponse = UserGenrePreferencesResponse(
            listOf(GenreDetails(genre = "Rock", count = 10))
        )
        val mockAlbumResponse = UserAlbumPreferencesResponse(
            listOf(AlbumDetails(album = "Album1", count = 3))
        )
        val mockPerformerResponse = UserPerformerPreferencesResponse(
            listOf(PerformerDetails(performer = "Performer1", count = 4))
        )

        // Stub the repository calls
        Mockito.`when`(repository.getUserGenrePreferences("testUser")).thenReturn(mockGenreResponse)
        Mockito.`when`(repository.getUserAlbumPreferences("testUser")).thenReturn(mockAlbumResponse)
        Mockito.`when`(repository.getUserPerformerPreferences("testUser")).thenReturn(mockPerformerResponse)

        // Act: Call the method in the ViewModel
        viewModel.onOptionSelected(selectedOptions)

        // Assert: Verify interactions with the repository
        Mockito.verify(repository).getUserGenrePreferences("testUser")
        Mockito.verify(repository).getUserAlbumPreferences("testUser")
        Mockito.verify(repository).getUserPerformerPreferences("testUser")

        // Assert: Check if LiveData objects are updated correctly
        Assert.assertEquals(mockGenreResponse, viewModel.userGenrePreferences.value)
        Assert.assertEquals(mockAlbumResponse, viewModel.userAlbumPreferences.value)
        Assert.assertEquals(mockPerformerResponse, viewModel.userPerformerPreferences.value)
    }
    @Test
    fun testOnOptionSelected_GroupGroupOptions() = runTest {
        // Arrange: Select three options from the "Group" group
        val selectedOptions = listOf("GroupGenrePrefs", "GroupAlbumPrefs", "GroupPerformerPrefs")

        val mockGenreResponse = UserGenrePreferencesResponse(
            listOf(GenreDetails(genre = "Rock", count = 10))
        )
        val mockAlbumResponse = UserAlbumPreferencesResponse(
            listOf(AlbumDetails(album = "Album1", count = 3))
        )
        val mockPerformerResponse = UserPerformerPreferencesResponse(
            listOf(PerformerDetails(performer = "Performer1", count = 4))
        )

        // Stub the repository calls
        Mockito.`when`(repository.getGroupGenrePreferences("testUser")).thenReturn(mockGenreResponse)
        Mockito.`when`(repository.getGroupAlbumPreferences("testUser")).thenReturn(mockAlbumResponse)
        Mockito.`when`(repository.getGroupPerformerPreferences("testUser")).thenReturn(mockPerformerResponse)

        // Act: Call the method in the ViewModel
        viewModel.onOptionSelected(selectedOptions)

        // Assert: Verify interactions with the repository
        Mockito.verify(repository).getGroupGenrePreferences("testUser")
        Mockito.verify(repository).getGroupAlbumPreferences("testUser")
        Mockito.verify(repository).getGroupPerformerPreferences("testUser")

        // Assert: Check if LiveData objects are updated correctly
        Assert.assertEquals(mockGenreResponse, viewModel.userGenrePreferences.value)
        Assert.assertEquals(mockAlbumResponse, viewModel.userAlbumPreferences.value)
        Assert.assertEquals(mockPerformerResponse, viewModel.userPerformerPreferences.value)

    }

    @Test
    fun testSaveSelectedMusics() = runTest {
        // Arrange
        val selectedMusics = listOf(
            Music(
                song_name = "Song1",
                length = "3:45",
                tempo = 120,
                recording_type = "Studio",
                listens = 100,
                release_year = "2020",
                added_timestamp = "2020-01-01",
                album_name = "Album1",
                performer_name = "Artist1",
                genre = "Rock",
                mood = "Energetic",
                instrument = "Guitar"
            ),
            Music(
                song_name = "Song2",
                length = "4:00",
                tempo = 130,
                recording_type = "Live",
                listens = 150,
                release_year = "2021",
                added_timestamp = "2021-02-01",
                album_name = "Album2",
                performer_name = "Artist2",
                genre = "Pop",
                mood = "Joyful",
                instrument = "Piano"
            )
        )

        // Act
        viewModel.saveSelectedMusics(selectedMusics)

        // Assert
        Assert.assertEquals(selectedMusics, viewModel.parsedMusics.value)
    }
    @Test
    fun testSaveSelectedMusics_EmptyList() = runTest {
        // Arrange
        val emptyMusicList = emptyList<Music>()

        // Act
        viewModel.saveSelectedMusics(emptyMusicList)

        // Assert
        Assert.assertTrue(viewModel.parsedMusics.value.isNullOrEmpty())
    }
    /*

    @Test
    fun testProcessFileAndPostTracks() = runTest {
        val mockUri = Mockito.mock(Uri::class.java)
        val mockContentResolver = Mockito.mock(ContentResolver::class.java)
        val fileContent = "your file content"
        Mockito.`when`(repository.readContentFromUri(mockContentResolver, mockUri)).thenReturn(fileContent)

        viewModel.processFileAndPostTracks(mockUri, mockContentResolver)

        // Additional verifications and assertions as needed
        // ...
    }*/
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}