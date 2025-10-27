package com.cutieprogramteam.calendar

import android.content.Intent
import androidx.compose.material3.MaterialTheme
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import androidx.compose.runtime.remember
import java.time.YearMonth
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.CalendarDay
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableStateOf
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat

class CalendarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.calendarlayout)

        val composeView: ComposeView = findViewById(R.id.composeCalendar)

        composeView.setContent {
            MaterialTheme {
                Calendar()
            }
        }
    }
}
@Composable
fun Day(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = when {
                    isSelected -> Color(0xFF00FF00)
                    else -> Color.Transparent
                },
                shape = CircleShape
            )
            .clickable(enabled = day.position == DayPosition.MonthDate) { onClick(day) }
            .aspectRatio(1f),

        contentAlignment = Alignment.Center
    ) {
        Text(text = day.date.dayOfMonth.toString())
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .shadow(6.dp)
        .background(Color(0xFFB0AFAD))

    ){
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}


@Preview
@Composable
fun Calendar() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) }
    val endMonth = remember { currentMonth.plusMonths(12) }
    val daysOfWeek = remember { daysOfWeek() }
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()

    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F0))

    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DaysOfWeekTitle(daysOfWeek = daysOfWeek)}

        VerticalCalendar(
            state = state,
            dayContent = { day ->
                if (day.position == DayPosition.MonthDate) {
                    Day(
                        day = day,
                        isSelected = selectedDate.value == day.date,
                        onClick = {
                            selectedDate.value =
                                if (selectedDate.value == it.date) null else it.date
                        }
                    )
                } else{
                    Box(modifier = Modifier.aspectRatio(1f))
                }
            },
            monthHeader = { month ->
                Text(
                    text = "${
                        month.yearMonth.month.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        )
                    } ${month.yearMonth.year}",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
                )
            },

            )

    }
}