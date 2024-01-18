package com.anafthdev.shafwah.service

import com.anafthdev.shafwah.common.dbQuery
import com.anafthdev.shafwah.model.db.IceTeaTable
import data.IceTeaVariant
import model.IceTea
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class IceTeaService(private val database: Database) {

    private fun ResultRow.toIceTea(): IceTea {
        return IceTea(
            id = this[IceTeaTable.id].value,
            variant = IceTeaVariant.entries[this[IceTeaTable.variant]],
            createdAt = this[IceTeaTable.createdAt],
            price = this[IceTeaTable.price]
        )
    }

    init {
        transaction(database) {
            SchemaUtils.create(IceTeaTable)
        }
    }

    suspend fun getAll(): List<IceTea> {
        return dbQuery {
            IceTeaTable.selectAll()
                .map { it.toIceTea() }
        }
    }

    /**
     * Get ice tea by id
     *
     * @return ice tea or null
     */
    suspend fun getById(id: Long): IceTea? {
        return dbQuery {
            IceTeaTable.select(IceTeaTable.id eq id)
                .map { it.toIceTea() }
                .singleOrNull()
        }
    }

    /**
     * Get ice tea by date range
     *
     * @param from start date in millis
     * @param to end date in millis
     * @return ice tea list
     */
    suspend fun getByDateRange(from: Long, to: Long): List<IceTea> {
        return dbQuery {
            IceTeaTable.select {
                // createdAt >= from && createdAt <= to
                // from <= createdAt <= to
                IceTeaTable.createdAt.between(from, to)
            }.map { it.toIceTea() }
        }
    }

    /**
     * Get ice tea by price range
     *
     * @param from start price in Rupiah e.g: 5000.0, 3000.0
     * @param to end price in Rupiah e.g: 5000.0, 3000.0
     */
    suspend fun getByPriceRange(from: Double, to: Double): List<IceTea> {
        return dbQuery {
            IceTeaTable.select {
                IceTeaTable.price.between(from, to)
            }.map { it.toIceTea() }
        }
    }

    /**
     * Get ice tea by price range and date range
     *
     * @param dateFrom start date in millis
     * @param dateTo end date in millis
     * @param priceFrom start price in Rupiah e.g: 5000.0, 3000.0
     * @param priceTo end price in Rupiah e.g: 5000.0, 3000.0
     * @return ice tea list
     */
    suspend fun getByPriceRangeAndDateRange(
        dateFrom: Long,
        dateTo: Long,
        priceFrom: Double,
        priceTo: Double,
    ): List<IceTea> {
        return dbQuery {
            IceTeaTable.select {
                IceTeaTable.createdAt.between(dateFrom, dateTo) and
                IceTeaTable.price.between(priceFrom, priceTo)
            }.map { it.toIceTea() }
        }
    }

    /**
     * Get ice tea by variant
     *
     * @param variant Ice tea variant
     * @return ice tea with variant [variant]
     */
    suspend fun getByVariant(variant: IceTeaVariant): List<IceTea> {
        return dbQuery {
            IceTeaTable.select {
                IceTeaTable.variant eq variant.ordinal
            }.map { it.toIceTea() }
        }
    }

    /**
     * Delete all record
     *
     * @return number of deleted rows
     */
    suspend fun deleteAll(): Int {
        return dbQuery {
            IceTeaTable.deleteAll()
        }
    }

    /**
     * Delete record by ID
     *
     * @return number of deleted rows
     */
    suspend fun deleteById(id: Long): Int {
        return dbQuery {
            IceTeaTable.deleteWhere { IceTeaTable.id eq id }
        }
    }

    /**
     * Delete record by date range
     *
     * @param from start date in millis
     * @param to end date in millis
     *
     * @return number of deleted rows
     */
    suspend fun deleteByDateRange(from: Long, to: Long): Int {
        return dbQuery {
            IceTeaTable.deleteWhere {
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
    suspend fun update(tea: IceTea): Int {
        return dbQuery {
            IceTeaTable.update({ IceTeaTable.id eq tea.id }) { statement ->
                statement[variant] = tea.variant.ordinal
                statement[createdAt] = tea.createdAt
                statement[price] = tea.price
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
        tea: IceTea
    ): Int {
        return dbQuery {
            IceTeaTable.update(
                where = {
                    // createdAt >= from && createdAt <= to
                    // from <= createdAt <= to
                    IceTeaTable.createdAt.between(from, to)
                }
            ) { statement ->
                statement[variant] = tea.variant.ordinal
                statement[createdAt] = tea.createdAt
                statement[price] = tea.price
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
        tea: IceTea
    ): Int {
        return dbQuery {
            IceTeaTable.update({ IceTeaTable.price.between(from, to) }) { statement ->
                statement[variant] = tea.variant.ordinal
                statement[createdAt] = tea.createdAt
                statement[price] = tea.price
            }
        }
    }

    /**
     * Insert new record
     *
     * @return record ID
     */
    suspend fun insert(tea: IceTea): Long = dbQuery {
        IceTeaTable.insertAndGetId { statement ->
            statement[variant] = tea.variant.ordinal
            statement[createdAt] = tea.createdAt
            statement[price] = tea.price
        }.value
    }

}