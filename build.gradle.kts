plugins {
    alias(libs.plugins.algomate)
}

val id = "h12"
version = file("version").readLines().first()

exercise {
    assignmentId.set(id)
}

submission {
    // ACHTUNG!
    // Setzen Sie im folgenden Bereich Ihre TU-ID (NICHT Ihre Matrikelnummer!), Ihren Nachnamen und Ihren Vornamen
    // in Anführungszeichen (z.B. "ab12cdef" für Ihre TU-ID) ein!
    studentId = "ab12cdef"
    firstName = "sol_first"
    lastName = "sol_last"

    // Optionally require own tests for mainBuildSubmission task. Default is false
    requireTests = true
}

jagr {
    graders {
        val graderPublic by getting {
            val name = "Public"
            graderName.set("${id.uppercase()}-$name")
            rubricProviderName.set("$id.${id.uppercase()}_RubricProvider$name")
        }
        val graderPrivate by creating {
            parent(graderPublic)
            val name = "Private"
            graderName.set("${id.uppercase()}-$name")
            rubricProviderName.set("$id.${id.uppercase()}_RubricProvider$name")
        }
    }
}
