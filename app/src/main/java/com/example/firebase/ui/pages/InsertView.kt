package com.example.firebase.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebase.ui.viewmodel.FormState
import com.example.firebase.ui.viewmodel.InsertViewModel
import com.example.firebase.ui.viewmodel.PenyediaViewModel
import com.example.firebase.ui.customwidget.TopAppBar
import com.example.firebase.ui.viewmodel.FormErrorState
import com.example.firebase.ui.viewmodel.InsertUiState
import com.example.firebase.ui.viewmodel.MahasiswaEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InsertMhsView (
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory =
    PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState //State utama untuk loading,success,error
    val uiEvent = viewModel.uiEvent //State untuk form dan validasi
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    //Observasi perubahan state untuk snackbar dan navigasi
    LaunchedEffect(uiState) {
        when (uiState) {
            is FormState.Success -> {
                println(
                    "InsertMhsView: uiState is FormState.Success, navigate to home" + uiState.message
                )

                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message) // Tampilkan snackbar
                }
                delay(700)
                //navigasi langsung
                onNavigate()

                viewModel.resetSnackBarMessage() //Reset snackbar state
            }

            is FormState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                judul = "Tambah Mahasiswa",
                showBackButton = true,
                onBack = onBack,
                modifier = modifier
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            InsertBodyMhs(
                uiState = uiEvent,
                homeUiState = uiState,
                onValueChange = { updatedEvent ->
                    viewModel.updateState(updatedEvent)
                },
                onClick = {
                    if (viewModel.validataFields()) {
                        viewModel.insertMhs()
                        //onNavigate()
                    }
                }
            )
        }
    }
}

@Composable
fun InsertBodyMhs (
    modifier: Modifier = Modifier,
    onValueChange: (MahasiswaEvent) -> Unit,
    uiState: InsertUiState,
    onClick: () -> Unit,
    homeUiState : FormState
) {
    Column (
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormMahasiswa (
            mahasiswaEvent = uiState.insertUiEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier
                .fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(),
            enabled = homeUiState !is FormState.Loading,
        ) {
            if (homeUiState is FormState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp)
                )
                Text("Loading...")
            } else {
                Text("Add")
            }
        }
    }
}

@Composable
fun FormMahasiswa(
    mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    onValueChange: (MahasiswaEvent) -> Unit,
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier
) {

    val gender = listOf("Laki-Laki", "Perempuan")
    val kelas = listOf("A","B","C","D")

    Column (
        modifier = modifier,
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nama,
            onValueChange = {
                onValueChange (mahasiswaEvent.copy(nama = it))
            },
            label = {Text("Nama")},
            isError = errorState.nama != null,
            placeholder = { Text("Masukkan nama")},
            singleLine = true
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nim,
            onValueChange = {
                onValueChange (mahasiswaEvent.copy(nim = it))
            },
            label = {Text("NIM")},
            isError = errorState.nim != null,
            placeholder = { Text("Masukkan NIM")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = errorState.nim ?: "",
            color = Color.Red,
        )
        Text(text = "gender")
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            gender.forEach { jk ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                   RadioButton(
                       selected = mahasiswaEvent.gender == jk,
                       onClick = {
                           onValueChange (mahasiswaEvent.copy(gender = jk))
                       },
                   )
                    Text(
                        text = jk,
                    )
                }
            }
        }
        Text(
            text = errorState.gender ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.alamat,
            onValueChange = {
                onValueChange (mahasiswaEvent.copy(alamat = it))
            },
            label = {Text("Alamat")},
            isError = errorState.alamat != null,
            placeholder = { Text("Masukkan Alamat")},
        )
        Text(
            text = errorState.alamat ?: "",
            color = Color.Red,
        )
        Text (text = "Kelas")
        Row {

            kelas.forEach { kelas ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = mahasiswaEvent.kelas == kelas,
                        onClick = {
                            onValueChange (mahasiswaEvent.copy(kelas = kelas))
                        },
                    )
                    Text(text = kelas)
                }
            }
        }
        Text(
            text = errorState.kelas ?: "",
            color = Color.Red,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.angkatan,
            onValueChange = {
                onValueChange (mahasiswaEvent.copy(angkatan = it))
            },
            label = {Text("Angkatan")},
            isError = errorState.angkatan != null,
            placeholder = { Text("Masukkan Angkatan")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = errorState.angkatan ?: "",
            color = Color.Red,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.judulskripsi,
            onValueChange = {
                onValueChange (mahasiswaEvent.copy(judulskripsi = it))
            },
            label = {Text("Judul Skripsi")},
            isError = errorState.judulskripsi != null,
            placeholder = { Text("Masukkan Judul Skripsi")},
        )
        Text(
            text = errorState.judulskripsi ?: "",
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(2.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dospem1,
            onValueChange = {
                onValueChange (mahasiswaEvent.copy(dospem1 = it))
            },
            label = {Text("Dospem1")},
            isError = errorState.dospem1 != null,
            placeholder = { Text("Masukkan Dospem1")},
        )
        Text(
            text = errorState.dospem1 ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dospem2,
            onValueChange = {
                onValueChange (mahasiswaEvent.copy(dospem2 = it))
            },
            label = {Text("Dospem2")},
            isError = errorState.dospem2 != null,
            placeholder = { Text("Masukkan Dospem2")},
        )
        Text(
            text = errorState.dospem2 ?: "",
            color = Color.Red
        )
    }
}



