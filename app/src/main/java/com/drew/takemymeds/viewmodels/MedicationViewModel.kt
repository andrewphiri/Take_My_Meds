package com.drew.takemymeds.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drew.takemymeds.model.Medication
import com.drew.takemymeds.model.MyMeds
import java.time.LocalDate
import java.time.LocalTime

class MedicationViewModel : ViewModel() {

    private var _medTimes = MutableLiveData<List<LocalTime>>()
    val medTimes: LiveData<List<LocalTime>> get() = _medTimes

    private var _startDate = MutableLiveData<LocalDate>()
    val startDate: LiveData<LocalDate> get() = _startDate

    private var _endDate = MutableLiveData<LocalDate>()
    val endDate: LiveData<LocalDate> get() = _endDate

    private var _myMeds = MutableLiveData<List<MyMeds>>()
    val myMeds: LiveData<List<MyMeds>> get() = _myMeds

    private var _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private var _type = MutableLiveData<String>()
    val type: LiveData<String> get() = _type

    private var _ailmentName = MutableLiveData<String>()
    private val ailmentName: LiveData<String> get() = _ailmentName

    private var _dosage = MutableLiveData<List<Double>>()
    val dosage: LiveData<List<Double>> get() = _dosage

    private var _stock = MutableLiveData<Double>()
    val stock: LiveData<Double> get() = _stock

    private var _frequency = MutableLiveData<Int>()
    val frequency: LiveData<Int> get() = _frequency

    private var _entryValid = MutableLiveData<Boolean>()
    val entryValid: LiveData<Boolean> get() = _entryValid


    fun getMedication() : MutableList<MyMeds> {
        val meds = mutableListOf<MyMeds>()
        return mutableListOf(MyMeds(
            id = 1001,
            startDate = startDate.value!!,
            dosage = dosage.value,
            endDate = endDate.value!!,
            startTime = medTimes.value!!,
            frequency = frequency.value!!,
            myMeds = newMedication()
        ))
    }

    fun setName(name: String){
        _name.value = name
    }

    fun setType(type: String){
        _type.value = type
    }

    fun setAilmentName(ailmentName: String){
        _ailmentName.value = ailmentName
    }

    fun setStock(stock: Double){
        _stock.value = stock
    }
    fun setFrequency(frequency: Int){
        _frequency.value = frequency
    }


   private fun newMedication() : MutableList<Medication> {
      return mutableListOf(Medication(
          name = name.value!!,
          type = type.value!!,
          ailmentName = ailmentName.value!!,
          stock = stock.value!!)
      )
   }

    private fun scheduleInformation(
        startDate: LocalDate,
        endDate: LocalDate,
        medicationTime: List<LocalTime>,
        dosage: List<Double>,
    ) {
     _startDate.value = startDate
     _endDate.value = endDate
     _dosage.value = dosage
    _medTimes.value = medicationTime
    }

    fun setStartDate(date: LocalDate){
        _startDate.value = date
    }

    fun setEndDate(date: LocalDate){
        _endDate.value = date
    }

    fun setDosage(dosage: List<Double>){
        _dosage.value = dosage
    }

    fun setMedicationTime(medTimes: List<LocalTime>) {
        _medTimes.value = medTimes
    }
    fun getMedicationTimes() : List<LocalTime>? {
        return medTimes.value
    }

    /**
     * Function to display list of times medication is taken per day
     * @param frequency is the number of times the user selects when scheduling new medication
     */
    fun sampleTimes(frequency: Int) : MutableList<LocalTime> {
        val sampleList:MutableList<LocalTime> = mutableListOf(LocalTime.of(6,0),
            LocalTime.of(8,0), LocalTime.of(10,0),
            LocalTime.of(12,0), LocalTime.of(14,0), LocalTime.of(16,0),
            LocalTime.of(18,0), LocalTime.of(20,0), LocalTime.of(22,0),
            LocalTime.of(0,0), LocalTime.of(2,0), LocalTime.of(4,0))

        //return selected number of dosages per day and save the list
        sampleDosages(frequency)

        val selectedList: MutableList<LocalTime>
        when(frequency) {
            1 -> {
                sampleList[0] = sampleList[3]
                selectedList = sampleList.take(frequency).toMutableList()
                return selectedList
            }
            2 -> {
                repeat(frequency) {
                    if(it > 0) {
                        sampleList[1] = sampleList[0].plusHours(8)
                    }
                }
                selectedList = sampleList.take(frequency).toMutableList()
                return selectedList
            }

            in 3..5 -> {
                repeat(frequency) {
                    if(it > 0) {
                        sampleList[it] = sampleList[it - 1].plusHours(4)
                    }
                }
                selectedList = sampleList.take(frequency).toMutableList()
                return selectedList
            }
            6 -> {
                repeat(frequency) {
                    if(it > 0) {
                        sampleList[it] = sampleList[it - 1].plusHours(3)
                    }
                }
                selectedList = sampleList.take(frequency).toMutableList()
                return selectedList
            }

            in 7..9 -> {
                repeat(frequency) {
                    if(it > 0) {
                        sampleList[it] = sampleList[it - 1].plusHours(2)
                    }
                }
                selectedList = sampleList.take(frequency).toMutableList()
                return selectedList
            }
            in 10..12 -> {
                repeat(frequency) {
                    if(it > 0) {
                        sampleList[it] = sampleList[it - 1].plusHours(1)
                    }
                }
                selectedList = sampleList.take(frequency).toMutableList()
                return selectedList
            }
            else -> return sampleList
        }
    }

    fun sampleDosages(frequency: Int) : MutableList<Double> {
        val sampleList: MutableList<Double> = mutableListOf(
            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0
        )
        val selectedNumberOfDosagesList: MutableList<Double>

        selectedNumberOfDosagesList = sampleList.take(frequency).toMutableList()
        setDosage(selectedNumberOfDosagesList)
        return selectedNumberOfDosagesList
    }

    fun isEntryValid(medName: String, medType: String, ailmentName: String, quantity: String) : Boolean {
        if (medName.isBlank() || medType.isBlank() || ailmentName.isBlank() || quantity.isBlank()) {
            _entryValid.value = false
            return false
        }
        _entryValid.value = true
        return true
    }

    fun setError(field : String) : String? {
        if (field.isBlank()) {
            return "This field cannot be blank!"
        }
        return null
    }

}