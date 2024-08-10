package com.chub.officemanager.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

const val DEFAULT_MAX_LINES = 1

@Composable
fun InputText(text: String, onTextChanged: (String) -> Unit, labelRes: Int, maxLines: Int = DEFAULT_MAX_LINES) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = onTextChanged,
        label = { Text(stringResource(id = labelRes)) },
        maxLines = maxLines
    )
}