package edu.rochester.avarela.komics.`class`

import java.time.LocalDateTime

data class Assignment(val dueDate: LocalDateTime,
                      val activity: String,
                      var progress: Float)