package me.sersch.http.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    val ds = HikariDataSource(config)
    Database.connect(ds)

}