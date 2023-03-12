package com.kaizensundays.particles.fusion.mu

import com.kaizensundays.particles.fusion.mu.dao.JournalDao
import com.kaizensundays.particles.fusion.mu.messages.Journal
import com.kaizensundays.particles.fusion.mu.messages.JournalState

/**
 * Created: Sunday 3/12/2023, 1:33 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class JournalManager(private val journalDao: JournalDao) {

    fun commit(journal: Journal) {
        journalDao.updateStateById(journal.id, JournalState.COMMITTED)
    }

}