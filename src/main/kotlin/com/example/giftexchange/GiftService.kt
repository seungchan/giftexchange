package com.example.giftexchange

import org.springframework.stereotype.Service

@Service
class GiftService (val db: GiftRepository) {

    fun findGiftExchange(): List<GiftExchange> = db.findGiftExchange()

    fun post(giftExchange: GiftExchange): GiftExchange {
        return db.save(giftExchange)
    }
}