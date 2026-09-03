package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import android.Manifest
import android.content.pm.PackageManager
import coil.compose.AsyncImage
import com.example.data.model.PresetData
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.NutriLensViewModel
import com.example.ui.viewmodel.ScanUiState
import com.example.ui.viewmodel.Screen

@Composable
fun FoodScannerScreen(
    viewModel: NutriLensViewModel,
    modifier: Modifier = Modifier
) {
    val scanState by viewModel.scanState.collectAsStateWithLifecycle()
    val realtimeAnalysis by viewModel.realtimeAnalysis.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var isFlashOn by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableStateOf("1x") }

    // Selected viewfinder preview image. Now nullable. If null, we show CameraX preview.
    var selectedPreviewUrl by remember { mutableStateOf<String?>(null) }

    // CameraX Setup
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var hasCameraPermission by remember { 
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) 
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    viewModel.scanImage(bitmap)
                }
            } catch (e: Exception) {
                viewModel.selectPresetFood(PresetData.sampleScanFoods.first())
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            android.widget.Toast.makeText(context, "Camera permission is required to scan food", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Animation for scanning laser beam
    val infiniteTransition = rememberInfiniteTransition(label = "scan_laser")
    val laserYRatio by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pos"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutriBlack)
    ) {
        if (selectedPreviewUrl != null) {
            AsyncImage(
                model = selectedPreviewUrl,
                contentDescription = "Food Viewfinder Preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val executor = ContextCompat.getMainExecutor(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        
                        val imgCapture = ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build()
                        imageCapture = imgCapture
                        
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            
                        imageAnalysis.setAnalyzer(executor) { proxy ->
                            viewModel.analyzeLiveFrame(proxy)
                        }
                        
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imgCapture,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            // Ignore
                        }
                    }, executor)
                    previewView
                },
                onRelease = {
                    try {
                        if (cameraProviderFuture.isDone) {
                            cameraProviderFuture.get().unbindAll()
                        }
                    } catch (e: Exception) {
                        // Ignore
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(Modifier.fillMaxSize().background(Color.Black))
        }

        // Semi-dark vignette
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )

        // Top Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NutriWhite.copy(alpha = 0.9f))
                    .clickable { viewModel.navigateTo(Screen.Dashboard) }
                    .testTag("scanner_back_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NutriBlack,
                    modifier = Modifier.size(20.dp)
                )
            }

            // AI Vision badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(NutriBlack.copy(alpha = 0.7f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Vyntra Vision",
                    color = NutriWhite,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            // Flash button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NutriWhite.copy(alpha = 0.9f))
                    .clickable { isFlashOn = !isFlashOn }
                    .testTag("scanner_flash_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFlashOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                    contentDescription = "Flash",
                    tint = NutriBlack,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Center Viewfinder Target Corner Brackets & Laser Line
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.Center)
                .offset(y = (-40).dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cornerLength = 40.dp.toPx()
                val strokeWidth = 3.5.dp.toPx()
                val color = Color.White
                val cornerRadius = 16.dp.toPx()

                // Top-Left Corner
                drawLine(color, Offset(0f, cornerRadius), Offset(0f, cornerLength), strokeWidth)
                drawLine(color, Offset(cornerRadius, 0f), Offset(cornerLength, 0f), strokeWidth)
                drawArc(color, 180f, 90f, false, Offset.Zero, size = androidx.compose.ui.geometry.Size(cornerRadius * 2, cornerRadius * 2), style = Stroke(strokeWidth))

                // Top-Right Corner
                drawLine(color, Offset(size.width, cornerRadius), Offset(size.width, cornerLength), strokeWidth)
                drawLine(color, Offset(size.width - cornerRadius, 0f), Offset(size.width - cornerLength, 0f), strokeWidth)
                drawArc(color, 270f, 90f, false, Offset(size.width - cornerRadius * 2, 0f), size = androidx.compose.ui.geometry.Size(cornerRadius * 2, cornerRadius * 2), style = Stroke(strokeWidth))

                // Bottom-Left Corner
                drawLine(color, Offset(0f, size.height - cornerRadius), Offset(0f, size.height - cornerLength), strokeWidth)
                drawLine(color, Offset(cornerRadius, size.height), Offset(cornerLength, size.height), strokeWidth)
                drawArc(color, 90f, 90f, false, Offset(0f, size.height - cornerRadius * 2), size = androidx.compose.ui.geometry.Size(cornerRadius * 2, cornerRadius * 2), style = Stroke(strokeWidth))

                // Bottom-Right Corner
                drawLine(color, Offset(size.width, size.height - cornerRadius), Offset(size.width, size.height - cornerLength), strokeWidth)
                drawLine(color, Offset(size.width - cornerRadius, size.height), Offset(size.width - cornerLength, size.height), strokeWidth)
                drawArc(color, 0f, 90f, false, Offset(size.width - cornerRadius * 2, size.height - cornerRadius * 2), size = androidx.compose.ui.geometry.Size(cornerRadius * 2, cornerRadius * 2), style = Stroke(strokeWidth))

                // Mid horizontal reference hairline (matching mockup)
                drawLine(
                    color = Color.White.copy(alpha = 0.5f),
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                )

                // Laser scan line
                val laserY = size.height * laserYRatio
                drawLine(
                    color = Color(0xFFFF6433),
                    start = Offset(10f, laserY),
                    end = Offset(size.width - 10f, laserY),
                    strokeWidth = 2.5.dp.toPx()
                )
            }
        }

        if (realtimeAnalysis != null && scanState !is ScanUiState.Scanning) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-200).dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(NutriBlack.copy(alpha = 0.75f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = realtimeAnalysis!!,
                    color = NutriWhite,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }


        // Bottom Controls Container (white card with zoom slider & shutter button)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(NutriWhite)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Preset meal selection pills for instant testing
                Text(
                    text = "SELECT FOOD OR SNAP PHOTO",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = com.example.ui.theme.NutriDarkGray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PresetData.sampleScanFoods.forEach { food ->
                        val isSelected = selectedPreviewUrl == food.imageUrl
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) NutriBlack else Color(0xFFF2F2F2))
                                .clickable {
                                    selectedPreviewUrl = food.imageUrl
                                    viewModel.selectPresetFood(food)
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("preset_food_${food.name.lowercase().replace(" ", "_")}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = food.name,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) NutriWhite else NutriBlack
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Zoom Indicator / Ticks (0.5x, 1x, 2x)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("0.5x", "1x", "2x").forEach { z ->
                        Text(
                            text = z,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (zoomLevel == z) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (zoomLevel == z) NutriBlack else Color(0xFF9E9E9E),
                            modifier = Modifier
                                .clickable { zoomLevel = z }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Shutter Row: Gallery Photo Picker on left, Shutter in center, Native Camera on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pick from Gallery
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF0F0F0))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("gallery_picker_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PhotoLibrary,
                            contentDescription = "Pick from Gallery",
                            tint = NutriBlack,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Main Circular Shutter Button with Scan Aperture Icon
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5E5E5))
                            .padding(6.dp)
                            .clip(CircleShape)
                            .background(NutriBlack)
                            .clickable {
                                if (selectedPreviewUrl != null) {
                                    // Scan the selected food preset
                                    val food = PresetData.sampleScanFoods.find { it.imageUrl == selectedPreviewUrl }
                                        ?: PresetData.sampleScanFoods.first()
                                    viewModel.selectPresetFood(food)
                                } else {
                                    // Take picture with CameraX
                                    val currentImageCapture = imageCapture ?: return@clickable
                                    currentImageCapture.takePicture(
                                        ContextCompat.getMainExecutor(context),
                                        object : ImageCapture.OnImageCapturedCallback() {
                                            override fun onCaptureSuccess(image: ImageProxy) {
                                                val buffer = image.planes[0].buffer
                                                val bytes = ByteArray(buffer.capacity())
                                                buffer.get(bytes)
                                                var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                                val matrix = Matrix()
                                                matrix.postRotate(image.imageInfo.rotationDegrees.toFloat())
                                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                                                viewModel.scanImage(bitmap)
                                                image.close()
                                            }
                                            override fun onError(exc: ImageCaptureException) {
                                                android.widget.Toast.makeText(context, "Failed to capture image", android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }
                            }
                            .testTag("shutter_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PhotoCamera,
                            contentDescription = "Scan Food",
                            tint = NutriWhite,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Live Camera Switch
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (selectedPreviewUrl == null) NutriBlack else Color(0xFFF0F0F0))
                            .clickable {
                                selectedPreviewUrl = null
                                if (!hasCameraPermission) {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                            .testTag("camera_launcher_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Cameraswitch,
                            contentDescription = "Live Camera",
                            tint = if (selectedPreviewUrl == null) NutriWhite else NutriBlack,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Loading Overlay if AI Scanning is in progress
        if (scanState is ScanUiState.Scanning) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = NutriGreenAccent,
                        modifier = Modifier.size(52.dp)
                    )
                    Text(
                        text = "Analyzing nutrition with Vyntra AI...",
                        style = MaterialTheme.typography.titleMedium,
                        color = NutriWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Estimating calories, macros & minerals",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFCCCCCC)
                    )
                }
            }
        }
    }
}
