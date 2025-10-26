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
            storeName = "Amazon",
            balance = 50.0,
            cardNumber = "1234-5678-9012-3456",
            expiryDate = "12/25",
            notes = "Birthday gift"
        )

        assertEquals(1L, card.id)
        assertEquals("Amazon", card.storeName)
        assertEquals(50.0, card.balance, 0.01)
        assertEquals("1234-5678-9012-3456", card.cardNumber)
        assertEquals("12/25", card.expiryDate)
        assertEquals("Birthday gift", card.notes)
    }

    @Test
    fun testDataClassCopy() {
        val original = GiftCardModel(
            id = 1,
            storeName = "Amazon",
            balance = 50.0,
            cardNumber = "1234"
        )
        val copy = original.copy(balance = 75.0)

        assertEquals(1L, copy.id)
        assertEquals("Amazon", copy.storeName)
        assertEquals(75.0, copy.balance, 0.01)
        assertEquals("1234", copy.cardNumber)
    }

    @Test
    fun testEquality() {
        val card1 = GiftCardModel(id = 1, storeName = "Amazon", balance = 50.0)
        val card2 = GiftCardModel(id = 1, storeName = "Amazon", balance = 50.0)
        val card3 = GiftCardModel(id = 2, storeName = "Target", balance = 100.0)

        assertEquals(card1, card2)
        assertNotEquals(card1, card3)
    }

    @Test
    fun testBalanceModification() {
        val card = GiftCardModel(id = 1, storeName = "Walmart", balance = 100.0)

        card.balance = 75.0
        assertEquals(75.0, card.balance, 0.01)

        card.balance = 0.0
        assertEquals(0.0, card.balance, 0.01)
    }

    @Test
    fun testNegativeBalance() {
        val card = GiftCardModel(id = 1, storeName = "Target", balance = -10.0)
        assertEquals(-10.0, card.balance, 0.01)
    }

    @Test
    fun testEmptyStoreName() {
        val card = GiftCardModel(id = 1, storeName = "", balance = 50.0)
        assertTrue(card.storeName.isEmpty())
    }

    @Test
    fun testLargeBalance() {
        val card = GiftCardModel(id = 1, storeName = "BestBuy", balance = 999999.99)
        assertEquals(999999.99, card.balance, 0.01)
    }

    @Test
    fun testSpecialCharactersInStoreName() {
        val card = GiftCardModel(id = 1, storeName = "H&M", balance = 50.0)
        assertEquals("H&M", card.storeName)
    }

    @Test
    fun testLongNotes() {
        val longNote = "This is a very long note that contains many sentences. " +
                "It tests that the notes field can handle large amounts of text."
        val card = GiftCardModel(id = 1, storeName = "Store", balance = 50.0, notes = longNote)
        assertEquals(longNote, card.notes)
    }
}