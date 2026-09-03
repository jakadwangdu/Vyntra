sed -i '/\/\/ Bottom Controls Container/i \
        if (realtimeAnalysis != null \&\& scanState !is ScanUiState.Scanning) {\
            Box(\
                modifier = Modifier\
                    .align(Alignment.BottomCenter)\
                    .offset(y = (-200).dp)\
                    .clip(RoundedCornerShape(16.dp))\
                    .background(NutriBlack.copy(alpha = 0.75f))\
                    .padding(horizontal = 16.dp, vertical = 8.dp)\
            ) {\
                Text(\
                    text = realtimeAnalysis!!,\
                    color = NutriWhite,\
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)\
                )\
            }\
        }\
\
' app/src/main/java/com/example/ui/screens/FoodScannerScreen.kt
