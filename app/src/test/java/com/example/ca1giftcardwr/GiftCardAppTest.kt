package com.example.ca1giftcardwr

import com.example.ca1giftcardwr.models.GiftCardModel
import org.junit.Assert.*
import org.junit.Test

class GiftCardAppTest {

    @Test
    fun createsWithAllFields() {
        val c = GiftCardModel(
            id = 1L,
            storeName = "Amazon",
            balance = 50.0,
            cardNumber = "1234567890",
            expiryDate = "12/2025",
            notes = "Birthday gift"
        )
        assertEquals(1L, c.id)
        assertEquals("Amazon", c.storeName)
        assertEquals(50.0, c.balance, 0.01)
        assertEquals("1234567890", c.cardNumber)
        assertEquals("12/2025", c.expiryDate)
        assertEquals("Birthday gift", c.notes)
    }

    @Test
    fun defaultConstructor() {
        val c = GiftCardModel()
        assertEquals(0L, c.id)
        assertEquals("", c.storeName)
        assertEquals(0.0, c.balance, 0.01)
        assertEquals("", c.cardNumber)
        assertEquals("", c.expiryDate)
        assertEquals("", c.notes)
    }

    @Test
    fun copyOverridesBalance() {
        val original = GiftCardModel(
            id = 1L,
            storeName = "Target",
            balance = 100.0,
            cardNumber = "9999",
            expiryDate = "06/2024",
            notes = "Shopping"
        )
        val copy = original.copy(balance = 75.0)
        assertEquals(original.id, copy.id)
        assertEquals(original.storeName, copy.storeName)
        assertEquals(75.0, copy.balance, 0.01)
        assertEquals(original.cardNumber, copy.cardNumber)
        assertEquals(original.expiryDate, copy.expiryDate)
        assertEquals(original.notes, copy.notes)
        assertNotSame(original, copy)
    }

    @Test
    fun balanceEdgeCases() {
        val c = GiftCardModel()
        c.balance = 0.0;     assertEquals(0.0, c.balance, 0.01)
        c.balance = 99.99;   assertEquals(99.99, c.balance, 0.01)
        c.balance = 9999.99; assertEquals(9999.99, c.balance, 0.01)
        c.balance = -10.0;   assertEquals(-10.0, c.balance, 0.01)
    }

    @Test
    fun supportsSpecialCharacters() {
        val c = GiftCardModel(storeName = "Barnes & Noble @ Mall", balance = 25.0)
        assertEquals("Barnes & Noble @ Mall", c.storeName)
    }

    @Test
    fun equalityByValue() {
        val a = GiftCardModel(id = 1L, storeName = "Starbucks", balance = 50.0)
        val b = GiftCardModel(id = 1L, storeName = "Starbucks", balance = 50.0)
        val c = GiftCardModel(id = 2L, storeName = "Starbucks", balance = 50.0)
        assertEquals(a, b)
        assertNotEquals(a, c)
    }

    @Test
    fun canMutateFields() {
        val c = GiftCardModel()
        c.storeName = "Walmart"
        c.balance = 150.75
        c.cardNumber = "WM123456"
        c.expiryDate = "01/2026"
        c.notes = "Grocery shopping"
        assertEquals("Walmart", c.storeName)
        assertEquals(150.75, c.balance, 0.01)
        assertEquals("WM123456", c.cardNumber)
        assertEquals("01/2026", c.expiryDate)
        assertEquals("Grocery shopping", c.notes)
    }

    @Test
    fun acceptsEmptyStrings() {
        val c = GiftCardModel(storeName = "", balance = 0.0, cardNumber = "", expiryDate = "", notes = "")
        assertTrue(c.storeName.isEmpty())
        assertTrue(c.cardNumber.isEmpty())
        assertTrue(c.expiryDate.isEmpty())
        assertTrue(c.notes.isEmpty())
    }

    @Test
    fun handlesLongStrings() {
        val s = "A".repeat(1000)
        val c = GiftCardModel(storeName = s, notes = s)
        assertEquals(1000, c.storeName.length)
        assertEquals(1000, c.notes.length)
    }

    @Test
    fun toStringContainsKeyInfo() {
        val c = GiftCardModel(id = 42L, storeName = "Apple Store", balance = 200.0)
        val str = c.toString()
        assertTrue(str.contains("42"))
        assertTrue(str.contains("Apple Store"))
        assertTrue(str.contains("200"))
    }
}
