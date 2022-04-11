package com.example.giftexchange

import org.springframework.stereotype.Service

@Service
class MemberService (val db: MemberRepository) {

    fun findMembers(): List<FamilyMember> = db.findMembers()

    fun post(member: FamilyMember): FamilyMember {
        return db.save(member)
    }
}