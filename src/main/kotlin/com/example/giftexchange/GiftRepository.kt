package com.example.giftexchange

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface GiftRepository : CrudRepository<GiftExchange, String> {

    @Query("select * from giftexchange")
    fun findGiftExchange(): List<GiftExchange>
}