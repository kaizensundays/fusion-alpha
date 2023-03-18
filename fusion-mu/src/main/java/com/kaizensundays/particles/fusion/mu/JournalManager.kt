package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.dao.JournalDao
import com.kaizensundays.particles.fusion.mu.messages.Journal
import com.kaizensundays.particles.fusion.mu.messages.JournalState
import java.util.*

/**
 * Created: Sunday 3/12/2023, 1:33 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class JournalManager(private val journalDao: JournalDao) {

    lateinit var messageQueue: Queue<Journal>

    fun commit(journal: Journal): Int {
        return journalDao.updateStateByUUID(journal.uuid, JournalState.COMMITTED)
    }

}