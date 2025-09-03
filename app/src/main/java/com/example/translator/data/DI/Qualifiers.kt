package com.example.translator.data.DI

import javax.inject.Qualifier

class Qualifiers {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Meanings
}