package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContactEntity
import com.example.ui.components.AvatarView
import com.example.ui.theme.WhatsAppAccentGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatBottomSheet(
  contacts: List<ContactEntity>,
  onSelectContact: (ContactEntity) -> Unit,
  onAddNewContact: (name: String, phone: String, bio: String) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var searchQuery by remember { mutableStateOf("") }
  var showAddContactDialog by remember { mutableStateOf(false) }

  val filteredContacts = contacts.filter {
    searchQuery.isBlank() ||
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.phone.contains(searchQuery)
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Select contact",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${contacts.size} contacts",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search name or number") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = WhatsAppAccentGreen)
        },
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp)
      )

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .height(340.dp)
      ) {
        // Quick Action 1: New group
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                // start group with first contact
                contacts.firstOrNull()?.let { onSelectContact(it) }
              }
              .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.GroupAdd,
              contentDescription = "New group",
              tint = WhatsAppAccentGreen,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
              text = "New group",
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        // Quick Action 2: New contact
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showAddContactDialog = true }
              .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.PersonAdd,
              contentDescription = "New contact",
              tint = WhatsAppAccentGreen,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
              text = "New contact",
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
          )
        }

        // Contact Items
        items(filteredContacts, key = { it.id }) { contact ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelectContact(contact) }
              .padding(vertical = 10.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            AvatarView(name = contact.name, size = 44.dp, isOnline = contact.isOnline)

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = contact.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = contact.statusBio,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
              )
            }
          }
        }
      }
    }
  }

  // Dialog to Add a New Contact
  if (showAddContactDialog) {
    var newName by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newBio by remember { mutableStateOf("Hey there! I am using WhatsApp.") }

    AlertDialog(
      onDismissRequest = { showAddContactDialog = false },
      title = { Text("New Contact") },
      text = {
        Column {
          OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("First and last name") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("new_contact_name_input")
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = newPhone,
            onValueChange = { newPhone = it },
            label = { Text("Phone number") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("new_contact_phone_input")
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = newBio,
            onValueChange = { newBio = it },
            label = { Text("About / Bio") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newName.isNotBlank() && newPhone.isNotBlank()) {
              onAddNewContact(newName, newPhone, newBio)
              showAddContactDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = WhatsAppAccentGreen),
          modifier = Modifier.testTag("save_new_contact_button")
        ) {
          Text("Save & Chat", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddContactDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
