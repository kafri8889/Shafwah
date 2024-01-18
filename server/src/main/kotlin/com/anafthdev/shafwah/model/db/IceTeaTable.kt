package com.anafthdev.shafwah.model.db

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object IceTeaTable: LongIdTable() {
    val variant: Column<Int> = integer("variant")
    val createdAt: Column<Long> = long("createdAt")
    val price: Column<Double> = double("price")
}