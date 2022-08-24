package com.drew.takemymeds.model

import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class MyMeds(val id: Int,
             var startDate: LocalDate,
             var endDate: LocalDate,
             var startTime: List<LocalTime>,
             var dosage: List<Double>?,
             var frequency: Int,
             var myMeds: List<Medication>) {
}