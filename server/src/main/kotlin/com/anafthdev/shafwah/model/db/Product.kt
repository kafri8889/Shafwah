package com.anafthdev.shafwah.model.db

import org.jetbrains.exposed.sql.Column

interface Product {

    val createdAt: Column<Long>
    val price: Column<Double>

}