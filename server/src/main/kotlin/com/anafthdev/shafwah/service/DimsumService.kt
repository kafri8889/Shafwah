package com.anafthdev.shafwah.service

import DIMSUM_PER_PIECE
import com.anafthdev.shafwah.common.dbQuery
import com.anafthdev.shafwah.model.db.DimsumTable
import data.model.Dimsum
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DimsumService(database: Database) {

    init {
        transaction(database) {
            SchemaUtils.create(DimsumTable)
        }
    }

    private fun ResultRow.toDimsum(): Dimsum {
        return Dimsum(
            id = this[DimsumTable.id].value,
            name = "Dimsum",
            amount = this[DimsumTable.amount],
            createdAt = this[DimsumTable.createdAt],
            price = this[DimsumTable.price],
            unitPrice = DIMSUM_PER_PIECE
        )
    }

    suspend fun getAll(): List<Dimsum> {
        return dbQuery {
            DimsumTable.selectAll()
                .map { it.toDimsum() }
        }
    }

    /**
     * Get dimsum by amount
     */
    suspend fun getByAmount(amount: Int): List<Dimsum> {
        return dbQuery {
            DimsumTable.select { DimsumTable.amount eq amount }
                .map { it.toDimsum() }
        }
    }

    suspend fun getById(id: Long): Dimsum? {
        return dbQuery {
            DimsumTable.select { DimsumTable.id eq id }
                .map { it.toDimsum() }
                .singleOrNull()
        }
    }

    /**
     * Get dimsum by date range
     *
     * @param from start date in millis
     * @param to end date in millis
     * @return ice tea list
     */
    suspend fun getByDateRange(from: Long, to: Long): List<Dimsum> {
        return dbQuery {
            DimsumTable.select {
                // createdAt >= from && createdAt <= to
                // from <= createdAt <= to
                DimsumTable.createdAt.between(from, to)
            }.map { it.toDimsum() }
        }
    }

    /**
     * Get dimsum by price range
     *
     * @param from start price in Rupiah e.g: 5000.0, 3000.0. inclusive
     * @param to end price in Rupiah e.g: 5000.0, 3000.0. inclusive
     */
    suspend fun getByPriceRange(from: Double, to: Double): List<Dimsum> {
        return dbQuery {
            DimsumTable.select {
                DimsumTable.price.between(from, to)
            }.map { it.toDimsum() }
        }
    }

    /**
     * Get dimsum by price range and date range
     *
     * @param dateFrom start date in millis
     * @param dateTo end date in millis
     * @param priceFrom start price in Rupiah e.g: 5000.0, 3000.0. inclusive
     * @param priceTo end price in Rupiah e.g: 5000.0, 3000.0. inclusive
     * @return dimsum list
     */
    suspend fun getByPriceRangeAndDateRange(
        dateFrom: Long,
        dateTo: Long,
        priceFrom: Double,
        priceTo: Double,
    ): List<Dimsum> {
        return dbQuery {
            DimsumTable.select {
                DimsumTable.createdAt.between(dateFrom, dateTo) and
                        DimsumTable.price.between(priceFrom, priceTo)
            }.map { it.toDimsum() }
        }
    }

    /**
     * Delete all record
     *
     * @return number of deleted rows
     */
    suspend fun deleteAll(): Int {
        return dbQuery {
            DimsumTable.deleteAll()
        }
    }

    /**
     * Delete record by ID
     *
     * @return number of deleted rows
     */
    suspend fun deleteById(id: Long): Int {
        return dbQuery {
            DimsumTable.deleteWhere { DimsumTable.id eq id }
        }
    }

    /**
     * Delete record by date range
     *
     * @param from start date in millis (inclusive)
     * @param to end date in millis (inclusive)
     *
     * @return number of deleted rows
     */
    suspend fun deleteByDateRange(from: Long, to: Long): Int {
        return dbQuery {
            DimsumTable.deleteWhere {
                // createdAt >= from && createdAt <= to
                // from <= createdAt <= to
                createdAt.between(from, to)
            }
        }
    }

    /**
     * Update record
     *
     * @return number of updated rows
     */
    suspend fun update(dimsum: Dimsum): Int {
        return dbQuery {
            DimsumTable.update({ DimsumTable.id eq dimsum.id }) { statement ->
                statement[createdAt] = dimsum.createdAt
                statement[price] = dimsum.price
                statement[unitPrice] = dimsum.unitPrice
                statement[amount] = dimsum.amount
            }
        }
    }

    /**
     * Update record by date range
     *
     * @param from start date in millis
     * @param to end date in millis
     *
     * @return number of updated rows
     */
    suspend fun updateByDateRange(
        from: Long,
        to: Long,
        dimsum: Dimsum
    ): Int {
        return dbQuery {
            DimsumTable.update(
                where = {
                    // createdAt >= from && createdAt <= to
                    // from <= createdAt <= to
                    DimsumTable.createdAt.between(from, to)
                }
            ) { statement ->
                statement[createdAt] = dimsum.createdAt
                statement[price] = dimsum.price
                statement[unitPrice] = dimsum.unitPrice
                statement[amount] = dimsum.amount
            }
        }
    }

    /**
     * Update record by date range
     *
     * @param from start price in Rupiah e.g: 5000.0, 3000.0
     * @param to end price in Rupiah e.g: 5000.0, 3000.0
     * @return number of updated rows
     */
    suspend fun updateByPriceRange(
        from: Double,
        to: Double,
        dimsum: Dimsum
    ): Int {
        return dbQuery {
            DimsumTable.update({ DimsumTable.price.between(from, to) }) { statement ->
                statement[createdAt] = dimsum.createdAt
                statement[price] = dimsum.price
                statement[unitPrice] = dimsum.unitPrice
                statement[amount] = dimsum.amount
            }
        }
    }

    /**
     * Insert new record
     *
     * @return record ID
     */
    suspend fun insert(dimsum: Dimsum): Long = dbQuery {
        DimsumTable.insertAndGetId { statement ->
            statement[createdAt] = dimsum.createdAt
            statement[price] = dimsum.price
            statement[unitPrice] = dimsum.unitPrice
            statement[amount] = dimsum.amount
        }.value
    }

}