package com.drew.takemymeds.model

import java.util.*

data class Medication(var id: Int = 1,
                      var type: String,
                      var name: String,
                      var ailmentName: String,
                      var stock: Double) {

}