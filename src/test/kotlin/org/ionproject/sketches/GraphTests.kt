package org.ionproject.sketches

import junit.framework.Assert.*
import org.junit.Test
import java.time.DayOfWeek

class GraphTests {

    @Test
    fun tinkering() {

        // Just set of timetables to test
        val timetables = listOf(
            // the timetable for LI61D
            ClassTimetable(
                "s1920v", "LEIC", "LI61D", listOf(
                    ClassTimetableSlot("CN_", WeeklyInstant(DayOfWeek.MONDAY, TimeOfDay(9, 30)), 90.minutes()),
                    ClassTimetableSlot("DAW", WeeklyInstant(DayOfWeek.MONDAY, TimeOfDay(11, 0)), 90.minutes()),
                    ClassTimetableSlot("DAW", WeeklyInstant(DayOfWeek.THURSDAY, TimeOfDay(11, 0)), 120.minutes()),
                    ClassTimetableSlot("CN_", WeeklyInstant(DayOfWeek.FRIDAY, TimeOfDay(9, 30)), 120.minutes())
                )
            ),
            // the timetable for LI61D where "CN_" is replaced with the correct name "CN"
            ClassTimetable(
                "s1920v", "LEIC", "LI61D", listOf(
                    ClassTimetableSlot("CN", WeeklyInstant(DayOfWeek.MONDAY, TimeOfDay(9, 30)), 90.minutes()),
                    ClassTimetableSlot("DAW", WeeklyInstant(DayOfWeek.MONDAY, TimeOfDay(11, 0)), 90.minutes()),
                    ClassTimetableSlot("DAW", WeeklyInstant(DayOfWeek.THURSDAY, TimeOfDay(11, 0)), 120.minutes()),
                    ClassTimetableSlot("CN", WeeklyInstant(DayOfWeek.FRIDAY, TimeOfDay(9, 30)), 120.minutes())
                )
            )
        )

        processTimetables(timetables)

        assertEquals(3, Db.courses.count())
        assertTrue(Db.courses.any {it.name == "DAW"})
        assertTrue(Db.courses.any {it.name == "CN"})
        assertTrue(Db.courses.any {it.name == "CN_"})

        val aliveCourses = transitiveClosure(Db.classSections.asSequence())
            .filterIsInstance<Course>()
            .toList()

        assertEquals(2, aliveCourses.count())
        assertTrue(aliveCourses.any {it.name == "CN"})
        assertFalse(aliveCourses.any {it.name == "CN_"})

        val zombieCourses = Db.courses - aliveCourses
        assertEquals(1, zombieCourses.count())
        assertFalse(zombieCourses.any {it.name == "CN"})
        assertTrue(zombieCourses.any {it.name == "CN_"})

    }
}