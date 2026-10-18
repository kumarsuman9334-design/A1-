package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
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
import com.example.data.model.StatusEntity
import com.example.ui.components.AvatarView
import com.example.ui.components.StatusRingType
import com.example.ui.theme.WhatsAppAccentGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UpdatesTab(
  statusUpdates: List<StatusEntity>,
  onStatusClick: (String) -> Unit,
  onAddStatusClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val myStatus = statusUpdates.find { it.isMyStatus }
  val recentUpdates = statusUpdates.filter { !it.isMyStatus && !it.isViewed }
  val viewedUpdates = statusUpdates.filter { !it.isMyStatus && it.isViewed }

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 96.dp)
    ) {
      // Status Section Header
      item {
        Text(
          text = "Status",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )
      }

      // My Status Row
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onAddStatusClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box {
            AvatarView(
              name = "My Status",
              size = 54.dp,
              statusRingState = if (myStatus != null) StatusRingType.UNVIEWED else StatusRingType.NONE
            )
            // Add '+' badge icon if no status or quick add
            Box(
              modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(WhatsAppAccentGreen)
                .align(Alignment.BottomEnd),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add status",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
            }
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column {
            Text(
              text = "My Status",
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (myStatus != null) "Tap to view update" else "Tap to add status update",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // Recent Updates Header
      if (recentUpdates.isNotEmpty()) {
        item {
          Text(
            text = "Recent updates",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 6.dp)
          )
        }

        items(recentUpdates, key = { it.id }) { status ->
          StatusItemRow(
            status = status,
            isViewed = false,
            onClick = { onStatusClick(status.id) }
          )
        }
      }

      // Viewed Updates Header
      if (viewedUpdates.isNotEmpty()) {
        item {
          Text(
            text = "Viewed updates",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 6.dp)
          )
        }

        items(viewedUpdates, key = { it.id }) { status ->
          StatusItemRow(
            status = status,
            isViewed = true,
            onClick = { onStatusClick(status.id) }
          )
        }
      }

      // Privacy info note
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "End-to-end encrypted",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Your status updates are end-to-end encrypted",
            fontSize = 11.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
          )
        }
      }
    }

    // Dual FABs for Status updates
    Column(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      SmallFloatingActionButton(
        onClick = onAddStatusClick,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Edit,
          contentDescription = "Text status",
          modifier = Modifier.size(18.dp)
        )
      }

      FloatingActionButton(
        onClick = onAddStatusClick,
        containerColor = WhatsAppAccentGreen,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.testTag("status_camera_fab")
      ) {
        Icon(
          imageVector = Icons.Default.CameraAlt,
          contentDescription = "Camera status",
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}

@Composable
private fun StatusItemRow(
  status: StatusEntity,
  isViewed: Boolean,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    AvatarView(
      name = status.contactName,
      size = 54.dp,
      statusRingState = if (isViewed) StatusRingType.VIEWED else StatusRingType.UNVIEWED
    )

    Spacer(modifier = Modifier.width(16.dp))

    Column {
      Text(
        text = status.contactName,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(2.dp))
      val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
      Text(
        text = timeFormat.format(Date(status.timestamp)),
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
