package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.NutriBg
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriBurnRed
import com.example.ui.theme.NutriDarkGray
import com.example.ui.theme.NutriGray
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriWaterBlue
import com.example.ui.theme.NutriWhite

private const val GITHUB_REPO_URL = "https://github.com/skituspanda/Vyntra"
private const val GITHUB_RELEASE_DOWNLOAD_URL = "https://github.com/skituspanda/Vyntra/releases/latest/download/Vyntra.apk"
private const val GITHUB_RELEASES_PAGE_URL = "https://github.com/skituspanda/Vyntra/releases"
private const val GITHUB_ACTIONS_URL = "https://github.com/skituspanda/Vyntra/actions"
private const val GITHUB_ACTIONS_WORKFLOW_URL = "https://github.com/skituspanda/Vyntra/actions/workflows/release.yml"
private const val GITHUB_PACKAGES_URL = "https://github.com/skituspanda/Vyntra/pkgs/container/vyntra-apk"
private const val GH_CLI_TRIGGER_CMD = "gh workflow run release.yml -f version_name=v1.0.0"
private const val DOCKER_PULL_CMD = "docker pull ghcr.io/skituspanda/vyntra/vyntra-apk:latest"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubDownloadSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = NutriWhite,
        dragHandle = null,
        modifier = Modifier.testTag("github_download_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_vyntra_logo),
                        contentDescription = "Vyntra Logo",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Download Vyntra",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            ),
                            color = NutriBlack
                        )
                        Text(
                            text = "GitHub Releases • Packages • Actions",
                            style = MaterialTheme.typography.bodySmall,
                            color = NutriGray
                        )
                    }
                }

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NutriBg)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NutriBlack,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Method 1: GitHub Releases (Direct APK)
            DownloadMethodCard(
                badgeText = "PRIMARY METHOD",
                badgeBg = NutriGreenAccent,
                badgeTextColor = NutriBlack,
                title = "GitHub Releases",
                subtitle = "Download pre-compiled Vyntra.apk directly to your phone",
                icon = Icons.Default.Download
            ) {
                Button(
                    onClick = {
                        try {
                            uriHandler.openUri(GITHUB_RELEASE_DOWNLOAD_URL)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("download_apk_release_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NutriBlack,
                        contentColor = NutriWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Download Vyntra.apk",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            try {
                                uriHandler.openUri(GITHUB_RELEASES_PAGE_URL)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, NutriBorder)
                    ) {
                        Text(
                            text = "Browse Releases",
                            color = NutriBlack,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = NutriDarkGray
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(GITHUB_RELEASE_DOWNLOAD_URL))
                            Toast.makeText(context, "Download link copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, NutriBorder)
                    ) {
                        Text(
                            text = "Copy Link",
                            color = NutriBlack,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = NutriDarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Method 2: Trigger GitHub Actions & Download Artifacts
            DownloadMethodCard(
                badgeText = "CLOUD BUILD & ARTIFACTS",
                badgeBg = Color(0xFFFF9800),
                badgeTextColor = NutriWhite,
                title = "Trigger GitHub Actions",
                subtitle = "Trigger automated workflow dispatch & download freshly compiled artifacts",
                icon = Icons.Default.PlayArrow
            ) {
                Text(
                    text = "GitHub Actions compiles the APK automatically. You can manually trigger a build on GitHub or download the APK artifact directly from the Actions run tab.",
                    style = MaterialTheme.typography.bodySmall,
                    color = NutriDarkGray,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            try {
                                uriHandler.openUri(GITHUB_ACTIONS_WORKFLOW_URL)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("trigger_actions_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NutriBlack,
                            contentColor = NutriWhite
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = NutriGreenAccent
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Run Workflow",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            try {
                                uriHandler.openUri(GITHUB_ACTIONS_URL)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, NutriBorder)
                    ) {
                        Text(
                            text = "View Artifacts",
                            color = NutriBlack,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // CLI command box
                CodeSnippetBox(
                    label = "GitHub CLI Trigger Command",
                    code = GH_CLI_TRIGGER_CMD,
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(GH_CLI_TRIGGER_CMD))
                        Toast.makeText(context, "CLI command copied!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Method 3: GitHub Packages (GHCR)
            DownloadMethodCard(
                badgeText = "CONTAINER & PACKAGES",
                badgeBg = NutriWaterBlue,
                badgeTextColor = NutriWhite,
                title = "GitHub Packages (GHCR)",
                subtitle = "OCI Container Package hosting Vyntra APK",
                icon = Icons.Default.Layers
            ) {
                Text(
                    text = "Vyntra APK is published as a GitHub Packages container artifact (ghcr.io). Pull the container or extract the APK using Docker or the GitHub CLI.",
                    style = MaterialTheme.typography.bodySmall,
                    color = NutriDarkGray,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        try {
                            uriHandler.openUri(GITHUB_PACKAGES_URL)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, NutriBorder)
                ) {
                    Text(
                        text = "Open GitHub Packages",
                        color = NutriBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = NutriBlack
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                CodeSnippetBox(
                    label = "Docker Pull Command",
                    code = DOCKER_PULL_CMD,
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(DOCKER_PULL_CMD))
                        Toast.makeText(context, "Docker command copied!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Installation Steps Accordion
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = NutriBg,
                border = BorderStroke(1.dp, NutriBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📲 3-Step Phone Installation",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NutriBlack
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InstallStep(number = "1", text = "Tap 'Download Vyntra.apk' above to get the APK.")
                    InstallStep(number = "2", text = "If Android warns 'Unknown Source', tap Settings > Allow from this source.")
                    InstallStep(number = "3", text = "Tap Install, open Vyntra, and enjoy!")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun DownloadMethodCard(
    badgeText: String,
    badgeBg: Color,
    badgeTextColor: Color,
    title: String,
    subtitle: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = NutriWhite,
        border = BorderStroke(1.dp, NutriBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = badgeTextColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = NutriBlack,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = NutriBlack
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = NutriGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
private fun CodeSnippetBox(
    label: String,
    code: String,
    onCopy: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NutriBlack)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = NutriGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onCopy() }
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = NutriGreenAccent,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "COPY",
                    color = NutriGreenAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = code,
            color = NutriWhite,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun InstallStep(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(NutriBlack),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = NutriWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = NutriDarkGray
        )
    }
}
