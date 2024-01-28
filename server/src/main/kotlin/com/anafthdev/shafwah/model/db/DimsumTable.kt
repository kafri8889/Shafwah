package com.anafthdev.shafwah.model.db

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object DimsumTable: LongIdTable(), Product {
    override val createdAt: Column<Long> = long("createdAt")
    override val price: Column<Double> = double("price")
    val unitPrice: Column<Double> = double("unitPrice")
    val amount: Column<Int> = integer("amount")
}