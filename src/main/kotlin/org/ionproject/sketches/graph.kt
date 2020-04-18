package org.ionproject.sketches

import java.time.DayOfWeek
import java.time.Duration

// Just a simple store
open class Store<T> : Sequence<T> {
    private val entryMap = mutableMapOf<T, T>()

    fun ensure(value: T) = entryMap.computeIfAbsent(value, { value })

    fun set(value: T) = entryMap.set(value, value)

    fun removeIf(predicate: (T) -> Boolean) {
        entryMap.entries
            .filter { predicate(it.value) }
            .toList()
            .forEach { entryMap.remove(it.key) }
    }

    override fun toString() = entryMap.values.toString()
    override fun iterator() = entryMap.values.iterator()
}

data class TimeOfDay(val hour: Int, val minutes: Int)
data class WeeklyInstant(val day: DayOfWeek, val time: TimeOfDay)
data class WeeklyEvent(
    val start: WeeklyInstant,
    val duration: Duration
)

fun Int.minutes(): Duration = Duration.ofMinutes(this.toLong())

typealias TermId = String
typealias ProgramId = String
typealias ClassScheduleId = String
typealias CourseId = String

data class Program(
    val name: String // e.g. "LEIC"
)

data class Term(
    val id: String // e.g. "s1920v"
)

data class Course(
    val program: Program,
    val name: String // e.g. "DAW"
)

// Instance of a course on a term
data class Class(val course: Course, val term: Term)

// Instance of a course on a timetable ("turma")
data class ClassSection(val clazz: Class, val scheduleId: String) {
    // the weekly lectures
    var slots: MutableList<WeeklyEvent> = mutableListOf()
}

object Db {
    val programs = Store<Program>()
    val terms = Store<Term>()
    val courses = Store<Course>()
    val classes = Store<Class>()
    val classSections = Store<ClassSection>()

    override fun toString() = """
        programs = $programs
        terms = $terms
        courses = $courses
        classes = $classes
        classSections = $classSections
    """.trimIndent()
}

data class ClassTimetable(
    val term: TermId,
    val program: ProgramId,
    val id: ClassScheduleId,
    val slots: List<ClassTimetableSlot>
)

data class ClassTimetableSlot(
    val course: CourseId,
    val start: WeeklyInstant,
    val duration: Duration
)


fun processTimetables(timetables: List<ClassTimetable>) {
    timetables.forEach { timetable ->
        val timetableId = timetable.id
        val term = Db.terms.ensure(Term(timetable.term))
        val program = Db.programs.ensure(Program(timetable.program))
        val classSections = Store<ClassSection>()
        // remove any class sections for
        Db.classSections.removeIf {
            // ... the same term
            it.clazz.term == term
                    // ... the same program
                    && it.clazz.course.program == Program(timetable.program)
                    // ... and the same schedule ID (e.g. LI61N)
                    && it.scheduleId == timetable.id
        }
        timetable.slots.forEach { slot ->
            val course = Db.courses.ensure(Course(program, slot.course))
            val clazz = Db.classes.ensure(Class(course, term))
            classSections.ensure(ClassSection(clazz, timetableId)).slots.add(WeeklyEvent(slot.start, slot.duration))
        }
        classSections.forEach {
            Db.classSections.set(it)
        }
    }
}

fun referencesFrom(node: Any): List<Any> = node::class.members
    .filter { it.name.startsWith("component") }
    .mapNotNull { it.call(node) }

fun transitiveClosure(roots: Sequence<Any>): Sequence<Any> {
    val toVisit = roots.toMutableList()
    val visited = mutableSetOf<Any>()

    while (toVisit.isNotEmpty()) {
        val node = toVisit[0]
        toVisit.removeAt(0)
        if (!visited.contains(node)) {
            toVisit.addAll(referencesFrom(node))
            visited.add(node)
        }
    }
    return visited.asSequence()
}


