package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.AddIcCall
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallEntity
import com.example.ui.components.AvatarView
import com.example.ui.theme.WhatsAppAccentGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CallsTab(
  calls: List<CallEntity>,
  onStartCall: (contactName: String, isVideo: Boolean) -> Unit,
  onNewCallClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 88.dp)
    ) {
      // Create call link item
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onStartCall("WhatsApp Call Link", false) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(WhatsAppAccentGreen),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Link,
              contentDescription = "Create call link",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column {
            Text(
              text = "Create call link",
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Share a link for your WhatsApp call",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // Recent Calls Header
      item {
        Text(
          text = "Recent",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )
      }

      items(calls, key = { it.id }) { call ->
        CallItemRow(
          call = call,
          onCallClick = { onStartCall(call.contactName, call.isVideo) }
        )
      }
    }

    // New Call FAB
    FloatingActionButton(
      onClick = onNewCallClick,
      containerColor = WhatsAppAccentGreen,
      contentColor = Color.White,
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
        .testTag("new_call_fab")
    ) {
      Icon(
        imageVector = Icons.Default.AddIcCall,
        contentDescription = "New call",
        modifier = Modifier.size(24.dp)
      )
    }
  }
}

@Composable
private fun CallItemRow(
  call: CallEntity,
  onCallClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onCallClick)
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    AvatarView(name = call.contactName, size = 52.dp)

    Spacer(modifier = Modifier.width(16.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = call.contactName,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = if (call.isMissed) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(3.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        val arrowColor = if (call.isMissed) Color(0xFFE53935) else WhatsAppAccentGreen
        Icon(
          imageVector = if (call.isIncoming) Icons.AutoMirrored.Filled.CallReceived else Icons.AutoMirrored.Filled.CallMade,
          contentDescription = if (call.isIncoming) "Incoming" else "Outgoing",
          tint = arrowColor,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        val timeFormat = SimpleDateFormat("MMMM d, h:mm a", Locale.getDefault())
        Text(
          text = timeFormat.format(Date(call.timestamp)),
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    IconButton(onClick = onCallClick) {
      Icon(
        imageVector = if (call.isVideo) Icons.Default.Videocam else Icons.Default.Call,
        contentDescription = if (call.isVideo) "Video call" else "Audio call",
        tint = WhatsAppAccentGreen,
        modifier = Modifier.size(22.dp)
      )
    }
  }
}
