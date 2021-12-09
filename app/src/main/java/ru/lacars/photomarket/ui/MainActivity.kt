package ru.lacars.photomarket.ui

import android.Manifest
import android.content.ContentResolver
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import ru.lacars.photomarket.BuildConfig
import ru.lacars.photomarket.R
import ru.lacars.photomarket.base.BaseActivity
import ru.lacars.photomarket.databinding.MainActivityBinding
import java.io.File

class MainActivity : BaseActivity<MainViewModel>() {
    lateinit var binding: MainActivityBinding
    lateinit var navController: NavController
    lateinit var mainViewModel: MainViewModel
    private var imageUri: Uri? = null
    private var filePath: File? = null

    private val camera = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            // Log.d("TEST", "getCameraPhotoActivityResultLauncher: Success | imageUri = $imageUri")
            lifecycleScope.launchWhenStarted {
                imageUri?.let {
                    val stream = this@MainActivity.contentResolver?.openInputStream(it)
                    mainViewModel.uploadPicture(stream)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this, viewModelProvider).get(MainViewModel::class.java)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavView.background = null
        binding.bottomNavView.menu.getItem(1).isEnabled = false

        setupView()
        setupListener()
    }

    override fun getViewModel(): MainViewModel = ViewModelProvider(this, viewModelProvider)[MainViewModel::class.java]

    fun setupView() {
        // Nav
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostMain) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navHostFragment.navController)

        // var appBarConfiguration = AppBarConfiguration(navHostFragment.navController.graph)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboardFragment,
                R.id.galleryFragment,
            )
        )
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }

    private fun setupListener() {
        binding.fab.setOnClickListener {
            // Toast.makeText(this,"click",Toast.LENGTH_SHORT).show()
            requestCamera()
        }
    }

    private fun requestCamera() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Основной функционал отключён", Toast.LENGTH_SHORT).show()
        } else {
            singlePermissionCamera.launch(Manifest.permission.CAMERA)
        }
    }

    val singlePermissionCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when {
            granted -> {
                // доступ к камере разрешен, открываем камеру
                // Create a storage location whose fileName is timestamped in milliseconds.
                val fileName = "item_${System.currentTimeMillis()}.jpeg"
                filePath = File(externalMediaDirs[0], fileName)
                filePath.also {
                    imageUri = FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        it!!
                    )
                    camera.launch(imageUri)
                }
                // camera.launch(imageUri)
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // user denied permission and set Don't ask again.
            }
            else -> {
                // доступ к камере запрещен, пользователь отклонил запрос
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
            R.id.action_day_night_mode -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val RATIONALE_KEY = "rationale_tag"
        const val SETTINGS_KEY = "settings_tag"
        const val RESULT_KEY = "camera_result_key"
    }
}
