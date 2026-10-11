package com.afzalhassan.quotequest.quiz

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.afzalhassan.quotequest.components.ActivityIndicator
import com.afzalhassan.quotequest.components.AppCard
import com.afzalhassan.quotequest.components.ErrorState
import com.afzalhassan.quotequest.components.PrimaryButton
import com.afzalhassan.quotequest.components.SecondaryButton
import com.afzalhassan.quotequest.components.TabPage
import com.afzalhassan.quotequest.data.AnswerResponse
import com.afzalhassan.quotequest.data.QuizMode
import com.afzalhassan.quotequest.theme.AppColors
import com.afzalhassan.quotequest.theme.AppSpacing
import com.afzalhassan.quotequest.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.action_ok
import quotequest.shared.generated.resources.quiz_binary_prompt
import quotequest.shared.generated.resources.quiz_correct
import quotequest.shared.generated.resources.quiz_multiple_prompt
import quotequest.shared.generated.resources.quiz_no
import quotequest.shared.generated.resources.quiz_progress
import quotequest.shared.generated.resources.quiz_right_answer
import quotequest.shared.generated.resources.quiz_score
import quotequest.shared.generated.resources.quiz_wrong
import quotequest.shared.generated.resources.quiz_yes
import quotequest.shared.generated.resources.results_correct
import quotequest.shared.generated.resources.results_score
import quotequest.shared.generated.resources.results_start_again
import quotequest.shared.generated.resources.results_title
import quotequest.shared.generated.resources.results_wrong
import quotequest.shared.generated.resources.tab_quiz

@Composable
fun QuizScreen(viewModel: QuizViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    QuizContent(state = state, onAnswer = viewModel::answer, onNext = viewModel::next, onRestart = viewModel::restart)
}

@Composable
private fun QuizContent(
    state: QuizUiState,
    onAnswer: (Answer) -> Unit,
    onNext: () -> Unit,
    onRestart: () -> Unit,
) {
    TabPage(title = stringResource(Res.string.tab_quiz), isLoading = state is QuizUiState.Loading) {
        when (state) {
            QuizUiState.Loading -> Unit
            is QuizUiState.Error -> ErrorState(message = state.message, onRetry = onRestart)
            is QuizUiState.Playing -> {
                Progress(index = state.index, total = state.questions.size, score = state.score)
                Question(state)
                state.error?.let {
                    Text(
                        text = stringResource(it),
                        style = AppTheme.textStyles.label.copy(color = AppColors.danger),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                AnswerButtons(state, onAnswer)
                state.feedback?.let { FeedbackDialog(it, onOk = onNext) }
            }
            is QuizUiState.Finished -> Results(state, onRestart)
        }
    }
}

@Composable
private fun Progress(index: Int, total: Int, score: Int) {
    val progress by animateFloatAsState((index + 1f) / total, label = "quizProgress")
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(Res.string.quiz_progress, index + 1, total), style = AppTheme.textStyles.label)
            Text(stringResource(Res.string.quiz_score, score), style = AppTheme.textStyles.label)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(AppSpacing.xs)
                .clip(CircleShape)
                .background(AppColors.surfaceField),
        ) {
            Box(Modifier.fillMaxWidth(progress).fillMaxHeight().background(AppColors.primary))
        }
    }
}

@Composable
private fun Question(playing: QuizUiState.Playing) {
    AppCard {
        Text("“${playing.question.text}”", style = AppTheme.textStyles.quote)
        Text(
            text = when (playing.mode) {
                QuizMode.BINARY -> stringResource(Res.string.quiz_binary_prompt, playing.question.authors.first())
                QuizMode.MULTIPLE -> stringResource(Res.string.quiz_multiple_prompt)
            },
            style = AppTheme.textStyles.sectionHeading,
        )
    }
}

@Composable
private fun AnswerButtons(playing: QuizUiState.Playing, onAnswer: (Answer) -> Unit) {
    when (playing.mode) {
        QuizMode.BINARY -> {
            val author = playing.question.authors.first()
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                AnswerButton(stringResource(Res.string.quiz_yes), Answer(author, true), playing.pendingAnswer, onAnswer, Modifier.weight(1f))
                AnswerButton(stringResource(Res.string.quiz_no), Answer(author, false), playing.pendingAnswer, onAnswer, Modifier.weight(1f))
            }
        }
        QuizMode.MULTIPLE -> Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
            playing.question.authors.forEach { author ->
                AnswerButton(author, Answer(author, true), playing.pendingAnswer, onAnswer, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun AnswerButton(text: String, answer: Answer, pending: Answer?, onAnswer: (Answer) -> Unit, modifier: Modifier) {
    SecondaryButton(
        text = text,
        onClick = { onAnswer(answer) },
        modifier = modifier,
        enabled = pending == null,
        isLoading = pending == answer,
    )
}

@Composable
private fun FeedbackDialog(feedback: AnswerResponse, onOk: () -> Unit) {
    AlertDialog(
        onDismissRequest = onOk,
        properties = DialogProperties(dismissOnClickOutside = false),
        title = {
            Text(
                text = stringResource(if (feedback.correct) Res.string.quiz_correct else Res.string.quiz_wrong),
                color = if (feedback.correct) AppColors.success else AppColors.danger,
            )
        },
        text = { Text(stringResource(Res.string.quiz_right_answer, feedback.rightAnswer)) },
        confirmButton = {
            TextButton(onClick = onOk) { Text(stringResource(Res.string.action_ok)) }
        },
    )
}

@Composable
private fun Results(finished: QuizUiState.Finished, onRestart: () -> Unit) {
    AppCard(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(Res.string.results_title), style = AppTheme.textStyles.sectionHeading)
        Text(stringResource(Res.string.results_score, finished.score, finished.total), style = AppTheme.textStyles.display)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Stat(stringResource(Res.string.results_correct), finished.score, AppColors.success)
            Stat(stringResource(Res.string.results_wrong), finished.total - finished.score, AppColors.danger)
        }
    }
    PrimaryButton(
        text = stringResource(Res.string.results_start_again),
        onClick = onRestart,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Stat(label: String, value: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value.toString(), style = AppTheme.textStyles.screenTitle.copy(color = color))
        Text(label, style = AppTheme.textStyles.label)
    }
}