package word.from.words.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import word.from.words.ui.theme.background
import word.from.words.ui.theme.card
import word.from.words.ui.theme.titleText
import word.from.words.ui.theme.white

@Composable
fun Content(
    modifier: Modifier = Modifier,
    score: Int,
    timer: Int,
    currentWord: String,
    allWords: List<String>,
    message: String,
    statusGame: StatusGame,
    onEvent: (MainEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = background)
            .padding(10.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Score: $score",
                fontSize = 20.sp,
                color = titleText
            )
            Text(
                text = "Time: $timer",
                fontSize = 20.sp,
                color = titleText
            )
        }
        Spacer(modifier = modifier.height(20.dp))
        Text(
            text = "Find: $currentWord",
            fontSize = 30.sp,
            color = titleText
        )
        Spacer(modifier = modifier.height(5.dp))
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxWidth(),
            columns = GridCells.Fixed(3)
        ) {
            items(allWords) {
                Box (
                    modifier = modifier
                        .padding(2.dp)
                        .clip(shape = RoundedCornerShape(5.dp))
                        .background(color = card)
                        .clickable {
                            onEvent(MainEvent.OnSelect(variant = it))
                        }
                        .padding(5.dp)
                ){
                    Text(
                        text = it,
                        fontSize = 20.sp,
                        color = white
                    )
                }
            }
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = card
                ),
                onClick = {
                    onEvent(MainEvent.OnReset)
                },
                enabled = statusGame is StatusGame.Quiz
            ) {
                Text(
                    text = "Reset Quiz",
                    fontSize = 20.sp,
                    color = white
                )
            }
            Spacer(modifier = modifier.width(10.dp))
            Button(
                modifier = modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = card
                ),
                onClick = {
                    onEvent(MainEvent.OnPlay)
                },
                enabled = statusGame is StatusGame.Pause
            ) {
                Text(
                    text = "Play",
                    fontSize = 20.sp,
                    color = white
                )
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = message,
            fontSize = 30.sp,
            color = titleText
        )
    }
}