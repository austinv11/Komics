package edu.rochester.avarela.komics.store

import java.time.LocalDateTime

data class Manifest(var plugins: Array<ManifestEntry>,
                    var last_updated: LocalDateTime)

data class ManifestEntry(var name: String,
                         var file: String,
                         var last_updated: LocalDateTime)