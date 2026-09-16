package com.example.pemesanantiket

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pemesanantiket.ui.theme.PemesananTiketTheme
import java.text.NumberFormat
import java.util.Locale

// ========================
// Konstanta harga tiket
// ========================
const val HARGA_PER_TIKET = 25_000L
const val NOMOR_CS = "08123456789"

// ========================
// Fungsi format Rupiah
// ========================
fun formatRupiah(nominal: Long): String {
    val format = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return "Rp${format.format(nominal)}"
}

// ========================
// Composable utama screen
// ========================
@Composable
fun PemesananTiketScreen() {
    val context = LocalContext.current

    // -------------------------------------------------------------------
    // STATE - menggunakan remember + mutableStateOf dengan property delegation (by)
    // Ketika jumlahTiket berubah, Compose melakukan recomposition otomatis
    // -------------------------------------------------------------------
    var jumlahTiket by remember { mutableStateOf(1) }
    val total = jumlahTiket * HARGA_PER_TIKET

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        // ---- Header ----
        HeaderSection()

        Spacer(modifier = Modifier.height(16.dp))

        // ---- Card konten ----
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Harga tiket
            HargaTiketCard()

            // Jumlah tiket dengan tombol + dan -
            JumlahTiketCard(
                jumlah = jumlahTiket,
                onTambah = {
                    // EVENT: onClick pada tombol "+"
                    // Mengubah STATE jumlahTiket -> Compose recompose otomatis
                    jumlahTiket++
                },
                onKurang = {
                    // EVENT: onClick pada tombol "-"
                    // State tidak boleh negatif
                    if (jumlahTiket > 1) jumlahTiket--
                }
            )

            // Total pembayaran
            TotalCard(total = total)

            // Tombol Reset
            // EVENT: onClick -> mengubah state jumlahTiket kembali ke 1
            Button(
                onClick = { jumlahTiket = 1 },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "RESET",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ---- Section Intent Implisit ----
            ImplisitIntentSection(context = context, jumlahTiket = jumlahTiket, total = total)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ========================
// Header biru dengan gradient
// ========================
@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1565C0), Color(0xFF1E88E5))
                )
            )
            .padding(top = 48.dp, bottom = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ConfirmationNumber,
                    contentDescription = "Tiket",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Pemesanan Tiket",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pesan tiket dengan mudah!",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )
        }
    }
}

// ========================
// Card harga tiket (statis)
// ========================
@Composable
fun HargaTiketCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Harga Tiket",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF424242)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = formatRupiah(HARGA_PER_TIKET),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            )
            Text(
                text = "per tiket",
                fontSize = 13.sp,
                color = Color(0xFF9E9E9E)
            )
        }
    }
}

// ========================
// Card jumlah tiket + tombol + / -
// ========================
@Composable
fun JumlahTiketCard(
    jumlah: Int,
    onTambah: () -> Unit,
    onKurang: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Jumlah Tiket",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF424242)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onKurang,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (jumlah > 1) Color(0xFF1E88E5) else Color(0xFFBDBDBD)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Kurang",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$jumlah",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                IconButton(
                    onClick = onTambah,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E88E5))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

// ========================
// Card total pembayaran
// ========================
@Composable
fun TotalCard(total: Long) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Total",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF424242)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = formatRupiah(total),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF43A047)
            )
        }
    }
}

// ========================
// Section Implicit Intent
// ========================
@Composable
fun ImplisitIntentSection(context: Context, jumlahTiket: Int, total: Long) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Hubungi & Bagikan",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF424242),
            modifier = Modifier.padding(start = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Implicit Intent: ACTION_DIAL (Telepon)
            IntentButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Phone,
                label = "Hubungi CS",
                containerColor = Color(0xFF1E88E5),
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$NOMOR_CS")
                    }
                    context.startActivity(intent)
                }
            )

            // Implicit Intent: ACTION_VIEW (Browser)
            IntentButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Language,
                label = "Website",
                containerColor = Color(0xFF7B1FA2),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://www.tiket.com")
                    }
                    context.startActivity(intent)
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Implicit Intent: ACTION_SENDTO (Email)
            IntentButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Email,
                label = "Kirim Email",
                containerColor = Color(0xFFD32F2F),
                onClick = {
                    val subject = "Pemesanan $jumlahTiket Tiket"
                    val body = "Halo, saya ingin memesan $jumlahTiket tiket.\n" +
                            "Total pembayaran: ${formatRupiah(total)}.\n" +
                            "Mohon konfirmasinya. Terima kasih."
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("cs@tiket.com"))
                        putExtra(Intent.EXTRA_SUBJECT, subject)
                        putExtra(Intent.EXTRA_TEXT, body)
                    }
                    context.startActivity(intent)
                }
            )

            // Implicit Intent: ACTION_VIEW ke WhatsApp
            IntentButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Share,
                label = "WhatsApp",
                containerColor = Color(0xFF388E3C),
                onClick = {
                    val pesan = "Halo! Saya ingin memesan *$jumlahTiket tiket*.\n" +
                            "Total: *${formatRupiah(total)}*.\n" +
                            "Mohon konfirmasinya."
                    val pesanEncoded = Uri.encode(pesan)
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://wa.me/$NOMOR_CS?text=$pesanEncoded")
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}

// ========================
// Komponen tombol intent reusable
// ========================
@Composable
fun IntentButton(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    containerColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// ========================
// Preview
// ========================
@Preview(showBackground = true)
@Composable
fun PreviewPemesananTiket() {
    PemesananTiketTheme {
        PemesananTiketScreen()
    }
}