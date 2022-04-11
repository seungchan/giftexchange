package com.example.giftexchange

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface MemberRepository : CrudRepository<FamilyMember, String> {

    @Query("select * from familymember")
    fun findMembers(): List<FamilyMember>
}