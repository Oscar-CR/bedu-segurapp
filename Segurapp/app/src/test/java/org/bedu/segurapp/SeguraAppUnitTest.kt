package org.bedu.segurapp

import org.bedu.segurapp.helpers.confirmPasswordMatcher
import org.bedu.segurapp.helpers.formatTelephone
import org.junit.Test

import org.junit.Assert.*

class SeguraAppUnitTest {
    @Test
    fun confirmPasswordMatcher_returnsTrue() {
        assertEquals(true, confirmPasswordMatcher("HolaMundo", "HolaMundo"))
    }

    @Test
    fun confirmPasswordMatcher_returnsFalse() {
        assertEquals(false, confirmPasswordMatcher("holaMundo", "HolaMundo"))
    }

    @Test
    fun formatTelephone13Digits_returnsString(){
        assertEquals("4774955345", formatTelephone("+524774955345"))
    }

    @Test
    fun formatTelephone14Digits_returnsString(){
        assertEquals("4774955345", formatTelephone("+5214774955345"))
    }

    @Test
    fun formatTelephone13Digits_returnsError(){
        assertEquals("Another Length", formatTelephone("+52477495534524"))
    }
}