package com.example.ca1giftcardwr

import com.example.ca1giftcardwr.models.GiftCardModel
import org.junit.Test
import org.junit.Assert.*

class GiftCardModelTest {

    @Test
    fun testDefaultValues() {
        val card = GiftCardModel()
        assertEquals(0L, card.id)
        assertEquals("", card.storeName)
        assertEquals(0.0, card.balance, 0.01)
        assertEquals("", card.cardNumber)
        assertEquals("", card.expiryDate)
        assertEquals("", card.notes)
    }

    @Test
    fun testModelCreationWithValues() {
        val card = GiftCardModel(
            id = 1,
            storeName = "Dunnes Stores",
            balance = 50.0,
            cardNumber = "1234-5678-9012-3456",
            expiryDate = "12/25",
            notes = "Birthday gift"
        )

        assertEquals(1L, card.id)
        assertEquals("Dunnes Stores", card.storeName)
        assertEquals(50.0, card.balance, 0.01)
        assertEquals("1234-5678-9012-3456", card.cardNumber)
        assertEquals("12/25", card.expiryDate)
        assertEquals("Birthday gift", card.notes)
    }

    @Test
    fun testDataClassCopy() {
        val original = GiftCardModel(
            id = 1,
            storeName = "Tesco",
            balance = 50.0,
            cardNumber = "1234"
        )
        val copy = original.copy(balance = 75.0)

        assertEquals(1L, copy.id)
        assertEquals("Tesco", copy.storeName)
        assertEquals(75.0, copy.balance, 0.01)
        assertEquals("1234", copy.cardNumber)
        assertNotSame(original, copy)
    }

    @Test
    fun testEquality() {
        val card1 = GiftCardModel(id = 1, storeName = "SuperValu", balance = 50.0)
        val card2 = GiftCardModel(id = 1, storeName = "SuperValu", balance = 50.0)
        val card3 = GiftCardModel(id = 2, storeName = "Penneys", balance = 100.0)

        assertEquals(card1, card2)
        assertNotEquals(card1, card3)
    }

    @Test
    fun testBalanceEdgeCases() {
        val card = GiftCardModel()

        card.balance = 0.0
        assertEquals(0.0, card.balance, 0.01)

        card.balance = 99.99
        assertEquals(99.99, card.balance, 0.01)

        card.balance = 9999.99
        assertEquals(9999.99, card.balance, 0.01)

        card.balance = -10.0
        assertEquals(-10.0, card.balance, 0.01)
    }

    @Test
    fun testFieldMutation() {
        val card = GiftCardModel()

        card.storeName = "Penneys"
        card.balance = 150.75
        card.cardNumber = "PN123456"
        card.expiryDate = "01/2026"
        card.notes = "Shopping voucher"

        assertEquals("Penneys", card.storeName)
        assertEquals(150.75, card.balance, 0.01)
        assertEquals("PN123456", card.cardNumber)
        assertEquals("01/2026", card.expiryDate)
        assertEquals("Shopping voucher", card.notes)
    }

    @Test
    fun testEmptyStrings() {
        val card = GiftCardModel(
            storeName = "",
            balance = 0.0,
            cardNumber = "",
            expiryDate = "",
            notes = ""
        )

        assertTrue(card.storeName.isEmpty())
        assertTrue(card.cardNumber.isEmpty())
        assertTrue(card.expiryDate.isEmpty())
        assertTrue(card.notes.isEmpty())
    }

    @Test
    fun testSpecialCharactersInStoreName() {
        val card = GiftCardModel(storeName = "Dunnes & Co", balance = 25.0)
        assertEquals("Dunnes & Co", card.storeName)
    }

    @Test
    fun testLongStrings() {
        val longString = "A".repeat(1000)
        val card = GiftCardModel(storeName = longString, notes = longString)

        assertEquals(1000, card.storeName.length)
        assertEquals(1000, card.notes.length)
    }

    @Test
    fun testToStringContainsKeyInfo() {
        val card = GiftCardModel(id = 42L, storeName = "Tesco", balance = 200.0)
        val str = card.toString()

        assertTrue(str.contains("42"))
        assertTrue(str.contains("Tesco"))
        assertTrue(str.contains("200"))
    }
}