package com.cutieprogramteam.calendar


import androidx.compose.material3.MaterialTheme
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.ui.platform.ComposeView
import java.time.Month


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
fun Day(day: CalendarDay, isSelected: Boolean, eventsForDay: List<Event>?, onClick: (CalendarDay) -> Unit) {
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = day.date.dayOfMonth.toString())
            if (!eventsForDay.isNullOrEmpty()) {
                Box(
                    Modifier
                        .background(Color.Red, shape = CircleShape)
                        .size(6.dp)
                        .padding(top = 2.dp)
                )
            }
        }
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

    val eventsByDate = events.groupBy { it.date }

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
                        eventsForDay = eventsByDate[day.date],
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
        selectedDate.value?.let { date ->
            val activities = eventsByDate[date] ?: emptyList()
            Column {
                Text("Activities for $date", fontWeight = FontWeight.Bold)
                activities.forEach { event -> Text("- ${event.description}") }
            }
        }

    }
}
data class Event(val date: LocalDate, val description: String)

val events = listOf(
    Event(LocalDate.of(2025, Month.AUGUST, 7), "FRESHIES ORIENTATION (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2025, Month.AUGUST, 15), "KILIT ANAY (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2025, Month.AUGUST, 21), "[translate:NINOY AQUINO DAY (HOLIDAY)] S"),
    Event(LocalDate.of(2025, Month.AUGUST, 25), "[translate:NATIONAL HERO DAY (HOLIDAY)] R"),

    Event(LocalDate.of(2025, Month.SEPTEMBER, 10), "PRELIM EXAM"),
    Event(LocalDate.of(2025, Month.SEPTEMBER, 11), "PRELIM EXAM"),
    Event(LocalDate.of(2025, Month.SEPTEMBER, 12), "PRELIM EXAM"),

    Event(LocalDate.of(2025, Month.OCTOBER, 8), "MIDTERM EXAM"),
    Event(LocalDate.of(2025, Month.OCTOBER, 9), "MIDTERM EXAM"),
    Event(LocalDate.of(2025, Month.OCTOBER, 10), "MIDTERM EXAM"),
    Event(LocalDate.of(2025, Month.OCTOBER, 16), "ACSO, AKWE, FRESHIES (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2025, Month.OCTOBER, 31), "[translate:ALL SAINT'S DAY (HOLIDAY)] S"),

    Event(LocalDate.of(2025, Month.NOVEMBER, 1), "[translate:ALL SAINT'S DAY (HOLIDAY)] S"),
    Event(LocalDate.of(2025, Month.NOVEMBER, 5), "[translate:NEGROS DAY (HOLIDAY)]"),
    Event(LocalDate.of(2025, Month.NOVEMBER, 10), "ENDTERM EXAM"),
    Event(LocalDate.of(2025, Month.NOVEMBER, 11), "ENDTERM EXAM"),
    Event(LocalDate.of(2025, Month.NOVEMBER, 12), "ENDTERM EXAM"),
    Event(LocalDate.of(2025, Month.NOVEMBER, 20), "CULTURE AND ART FEST (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2025, Month.NOVEMBER, 21), "CULTURE AND ART FEST (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2025, Month.NOVEMBER, 25), "START OF 18 DAY CAMPAIGN (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2025, Month.NOVEMBER, 30), "[translate:BONIFACIO DAY (HOLIDAY)] R"),

    Event(LocalDate.of(2025, Month.DECEMBER, 8), "[translate:FEAST IMMACULATE MARY (HOLIDAY)] S"),
    Event(LocalDate.of(2025, Month.DECEMBER, 12), "END OF 18 DAY CAMPAIGN (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2025, Month.DECEMBER, 16), "YEAR END CELEBRATION (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2025, Month.DECEMBER, 24), "[translate:CHRISTMAS EVE (HOLIDAY)] S"),
    Event(LocalDate.of(2025, Month.DECEMBER, 25), "[translate:CHRISTMAS DAY (HOLIDAY)] R"),
    Event(LocalDate.of(2025, Month.DECEMBER, 30), "[translate:RIZAL DAY (HOLIDAY)] R"),
    Event(LocalDate.of(2025, Month.DECEMBER, 31), "[translate:NEW YEAR (HOLIDAY)] S"),

    Event(LocalDate.of(2026, Month.JANUARY, 1), "[translate:NEW YEAR (HOLIDAY)] R"),
    Event(LocalDate.of(2026, Month.JANUARY, 7), "PRELIM EXAM"),
    Event(LocalDate.of(2026, Month.JANUARY, 8), "PRELIM EXAM"),
    Event(LocalDate.of(2026, Month.JANUARY, 9), "PRELIM EXAM"),

    Event(LocalDate.of(2026, Month.FEBRUARY, 4), "MIDTERM EXAM"),
    Event(LocalDate.of(2026, Month.FEBRUARY, 5), "MIDTERM EXAM"),
    Event(LocalDate.of(2026, Month.FEBRUARY, 6), "MIDTERM EXAM"),
    Event(LocalDate.of(2026, Month.FEBRUARY, 11), "[translate:TALISAY CHARTER DAY (HOLIDAY)]"),
    Event(LocalDate.of(2026, Month.FEBRUARY, 13), "PRE VALENTINE CELEBRATION (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2026, Month.FEBRUARY, 17), "[translate:CHINESE NEW YEAR (HOLIDAY)] S"),
    Event(LocalDate.of(2026, Month.FEBRUARY, 25), "[translate:PEOPLE POWER (HOLIDAY)] S"),

    Event(LocalDate.of(2026, Month.MARCH, 4), "TECHNO FEST (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2026, Month.MARCH, 5), "UNIVERSITY DAYS (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2026, Month.MARCH, 6), "UNIVERSITY DAYS (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2026, Month.MARCH, 9), "ENDTERM EXAM"),
    Event(LocalDate.of(2026, Month.MARCH, 10), "ENDTERM EXAM"),
    Event(LocalDate.of(2026, Month.MARCH, 11), "ENDTERM EXAM"),
    Event(LocalDate.of(2026, Month.MARCH, 23), "INTRAMS (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2026, Month.MARCH, 24), "INTRAMS (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2026, Month.MARCH, 25), "INTRAMS (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2026, Month.MARCH, 26), "INTRAMS (STUDENT ACTIVITY)"),
    Event(LocalDate.of(2026, Month.MARCH, 27), "INTRAMS (STUDENT ACTIVITY)"),

    Event(LocalDate.of(2026, Month.APRIL, 2), "[translate:HOLY WEEK (HOLIDAY)]"),
    Event(LocalDate.of(2026, Month.APRIL, 3), "[translate:HOLY WEEK (HOLIDAY)]"),
    Event(LocalDate.of(2026, Month.APRIL, 4), "[translate:HOLY WEEK (HOLIDAY)]"),
    Event(LocalDate.of(2026, Month.APRIL, 5), "[translate:HOLY WEEK (HOLIDAY)]"),
    Event(LocalDate.of(2026, Month.APRIL, 9), "[translate:ARAW NG KAGITINGAN (HOLIDAY)] R"),

    Event(LocalDate.of(2026, Month.APRIL, 22), "PRELIM EXAM"),
    Event(LocalDate.of(2026, Month.APRIL, 23), "PRELIM EXAM"),
    Event(LocalDate.of(2026, Month.APRIL, 24), "PRELIM EXAM"),

    Event(LocalDate.of(2026, Month.MAY, 1), "[translate:LABOR DAY (HOLIDAY)]"),
    Event(LocalDate.of(2026, Month.MAY, 20), "MIDTERM EXAM"),
    Event(LocalDate.of(2026, Month.MAY, 21), "MIDTERM EXAM"),
    Event(LocalDate.of(2026, Month.MAY, 22), "MIDTERM EXAM"),

    Event(LocalDate.of(2026, Month.JUNE, 12), "[translate:INDEPENDENCE DAY (HOLIDAY)] R"),
    Event(LocalDate.of(2026, Month.JUNE, 22), "ENDTERM EXAM"),
    Event(LocalDate.of(2026, Month.JUNE, 23), "ENDTERM EXAM"),
    Event(LocalDate.of(2026, Month.JUNE, 24), "ENDTERM EXAM"),

    Event(LocalDate.of(2026, Month.AUGUST, 21), "[translate:NINOY AQUINO (HOLIDAY)]"),
    Event(LocalDate.of(2026, Month.AUGUST, 31), "[translate:NATIONAL HERO DAY (HOLIDAY)]")
)