/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.max

@Composable
fun Counter() {
    var running by remember { mutableStateOf(false) }
    var totalSeconds by remember { mutableStateOf(60) }
    var secondsRemaining by remember { mutableStateOf(60) }

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val formatted = "%d:%02d".format(minutes, seconds)

    if (running) {
        LaunchedEffect("tick") {
            while (isActive) {
                delay(1000)
                if (isActive) {
                    secondsRemaining--
                    if (secondsRemaining == 0) {
                        running = false
                    }
                }
            }
        }
    }

    val target = when {
        !running && totalSeconds != secondsRemaining -> 0f
        !running -> 1f
        else -> max(secondsRemaining.toFloat() - 1, 0f) / totalSeconds
    }

    val progress by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.aspectRatio(1f), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    Text(text = formatted, style = MaterialTheme.typography.h2)
                }
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Button(
                        onClick = {
                            if (totalSeconds > 1) {
                                totalSeconds -= 1
                                secondsRemaining = totalSeconds
                            }
                        },
                        enabled = !running
                    ) {
                        Text(text = "-")
                    }
                    Text(
                        text = "SECONDS",
                        modifier = Modifier
                            .defaultMinSize(minWidth = 96.dp)
                            .padding(8.dp)
                    )
                    Button(
                        onClick = {
                            totalSeconds += 1
                            secondsRemaining = totalSeconds
                        },
                        enabled = !running
                    ) {
                        Text(text = "+")
                    }
                }
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Button(
                        onClick = {
                            if (totalSeconds > 60) {
                                totalSeconds -= 60
                                secondsRemaining = totalSeconds
                            } else {
                                totalSeconds = 1
                                secondsRemaining = totalSeconds
                            }
                        },
                        enabled = !running
                    ) {
                        Text(text = "-")
                    }
                    Text(
                        text = "MINUTES",
                        modifier = Modifier
                            .defaultMinSize(minWidth = 96.dp)
                            .padding(8.dp)
                    )
                    Button(
                        onClick = {
                            totalSeconds += 60
                            secondsRemaining = totalSeconds
                        },
                        enabled = !running
                    ) {
                        Text(text = "+")
                    }
                }
                Button(
                    onClick = {
                        if (running) {
                            running = false
                            secondsRemaining = totalSeconds
                        } else {
                            if (secondsRemaining != totalSeconds) {
                                secondsRemaining = totalSeconds
                            } else {
                                running = true
                            }
                        }
                    },
                    modifier = Modifier
                        .defaultMinSize(minWidth = 156.dp)
                        .padding(top = 8.dp)
                ) {
                    Text(text = if (running || secondsRemaining != totalSeconds) "RESET" else "GO")
                }
            }
        }
    }
}
