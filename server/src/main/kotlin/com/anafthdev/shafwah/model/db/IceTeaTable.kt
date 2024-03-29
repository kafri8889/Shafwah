package com.anafthdev.shafwah.model.db

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object IceTeaTable: LongIdTable(), Product {
    override val createdAt: Column<Long> = long("createdAt")
    override val price: Column<Double> = double("price")
    val variant: Column<Int> = integer("variant")
}