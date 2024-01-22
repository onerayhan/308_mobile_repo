package com.example.start2.home.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.start2.home.Profile.ProfileViewModel
import com.example.start2.home.Profile.ProfileViewModelFactory
import com.example.start2.home.Profile.UserPreferences

/*
@Composable
fun UserGroupScreen() {
    var groupName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userPreferences= remember{UserPreferences(context)}
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    val userGroupLiveData =  profileViewModel.userGroupInfo
    val userGroup = userGroupLiveData.observeAsState(initial = emptyList())
    // Observe the user group information from the ViewModel
     profileViewModel.userGroupInfo.observeAsState()

    LaunchedEffect(Unit) {
        // Trigger fetching user group information when the composable is launched
        profileViewModel.displayUserGroup()
    }

    LazyColumn(
        modifier = Modifier
            .padding(top=30.dp)
            .fillMaxWidth()
    ) {
        item {
            CreateGroupSection()
        }
        item {
            AddUserToGroupSection()
        }
        item {
            RemoveUserFromGroupSection()
        }
        item{
            UserGroupInfoSection()
        }
    }
}
*/

@Composable
fun UserGroupScreen() {
    var isCreateGroupDialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    val userGroupLiveData = profileViewModel.userGroupInfo
    val userGroup by userGroupLiveData.observeAsState(initial = emptyList())
    profileViewModel.displayUserGroup()
    var groupName by remember { mutableStateOf("") }
    var groupMembers by remember { mutableStateOf("") }
    var joinGroupId by remember { mutableStateOf(0) }
    var joinUsername by remember { mutableStateOf("") }
    var isAddUserGroupDialogVisible by remember { mutableStateOf(false) }
    var isMoveUserGroupDialogVisible by remember { mutableStateOf(false) }


    LazyColumn(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
    ) {
        items(userGroup) { group ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        if (userGroup.indexOf(group) % 2 == 0) Color(0xff1d1d20) else Color.Transparent
                    )
            ) {
                Text(
                    text = group.groupId.toString(), // Convert to String
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .weight(2f)
                        .padding(8.dp)
                )
                Text(
                    text = group.groupName,
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .weight(2f)
                        .padding(8.dp)
                )
                Text(
                    text = group.groupMembers.joinToString(), // Convert List<String> to String
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )
            }
        }

        item {
            Button(
                onClick = {
                    // Handle the creation of a new group
                    // You may want to show a dialog or navigate to a new screen for creating a group
                    // For simplicity, let's assume you have a function createGroup in your ViewModel
                    isCreateGroupDialogVisible = true


                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Create Group")
            }
        }
        item {
            Button(
                onClick = {
                    // Handle the creation of a new group
                    // You may want to show a dialog or navigate to a new screen for creating a group
                    // For simplicity, let's assume you have a function createGroup in your ViewModel
                    isAddUserGroupDialogVisible = true


                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Add User To Group")
            }
        }
        item {
            Button(
                onClick = {
                    // Handle the creation of a new group
                    // You may want to show a dialog or navigate to a new screen for creating a group
                    // For simplicity, let's assume you have a function createGroup in your ViewModel
                    isMoveUserGroupDialogVisible = true


                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Remove User From Group")
            }
        }
    }
    if (isCreateGroupDialogVisible) {

        CreateGroupDialog(
            onDismiss = { isCreateGroupDialogVisible = false },
            onConfirm = {
                // Call the createGroup function in your ViewModel
                isCreateGroupDialogVisible = false
            },
            groupName = groupName,
            onGroupNameChange = { groupName = it },
            groupMembers = groupMembers,
            onGroupMembersChange = { groupMembers = it }
        )
    }
    if (isAddUserGroupDialogVisible) {

        addUserGroupDialog(
            onDismiss = { isAddUserGroupDialogVisible= false },
            onConfirm = {
                // Call the createGroup function in your ViewModel
                isAddUserGroupDialogVisible = false
            },
            groupName = groupName,
            onGroupNameChange = { groupName = it },
            groupMembers = groupMembers,
            onGroupMembersChange = { groupMembers = it }
        )
    }
    if (isMoveUserGroupDialogVisible) {

        addMoveGroupDialog(
            onDismiss = { isMoveUserGroupDialogVisible= false },
            onConfirm = {
                // Call the createGroup function in your ViewModel
                isMoveUserGroupDialogVisible  = false
            },
            groupName = groupName,
            onGroupNameChange = { groupName = it },
            groupMembers = groupMembers,
            onGroupMembersChange = { groupMembers = it }
        )
    }
}

@Composable
fun addUserGroupDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    groupName: String,
    onGroupNameChange: (String) -> Unit,
    groupMembers: String,
    onGroupMembersChange: (String) -> Unit
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Add User to Group") },
        text = {
            Column {
                TextField(
                    value = groupName,
                    onValueChange = { onGroupNameChange(it) },
                    label = { Text(text = "Group Id") }
                )
                TextField(
                    value = groupMembers,
                    onValueChange = { onGroupMembersChange(it) },
                    label = { Text(text = "Group Member") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    profileViewModel.addUserToGroup(groupMembers,groupName.toInt())
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun addMoveGroupDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    groupName: String,
    onGroupNameChange: (String) -> Unit,
    groupMembers: String,
    onGroupMembersChange: (String) -> Unit
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Add User to Group") },
        text = {
            Column {
                TextField(
                    value = groupName,
                    onValueChange = { onGroupNameChange(it) },
                    label = { Text(text = "Group Id") }
                )
                TextField(
                    value = groupMembers,
                    onValueChange = { onGroupMembersChange(it) },
                    label = { Text(text = "Group Member") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    profileViewModel.removeUserFromGroup(groupMembers,groupName.toInt())
                    onConfirm()
                }
            ) {
                Text("Remove")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}





@Composable
fun CreateGroupDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    groupName: String,
    onGroupNameChange: (String) -> Unit,
    groupMembers: String,
    onGroupMembersChange: (String) -> Unit
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Create Group") },
        text = {
            Column {
                TextField(
                    value = groupName,
                    onValueChange = { onGroupNameChange(it) },
                    label = { Text(text = "Group Name") }
                )
                TextField(
                    value = groupMembers,
                    onValueChange = { onGroupMembersChange(it) },
                    label = { Text(text = "Group Members (comma-separated)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val groupMembersList = groupMembers.split(",").map { it.trim() }
                    profileViewModel.formGroup( groupMembersList, groupName)


                    onConfirm()
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}





@Composable
fun CreateGroupSection() {
    val context = LocalContext.current
    var groupName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Group Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                // Add logic to create a group

                // For demonstration purposes, print a message
                println("Group $groupName created")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Create Group")
        }
    }
}

@Composable
fun AddUserToGroupSection() {
    var userName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("User Name") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            IconButton(
                onClick = {
                    // Add logic to add user to group
                    // For demonstration purposes, print a message
                    println("Added $userName to group")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    }
}

@Composable
fun RemoveUserFromGroupSection() {
    var userName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("User Name") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            IconButton(
                onClick = {
                    // Add logic to remove user from group
                    // For demonstration purposes, print a message
                    println("Removed $userName from group")
                }
            ) {
                Icon(Icons.Default.Remove, contentDescription = null)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FollowersScreenPreview() {
    androidx.compose.material.Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        UserGroupScreen()
    }
}




