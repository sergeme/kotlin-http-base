package me.sersch.http.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun initDB() {
    val hikariConfig = HikariConfig("/hikari.properties")
    hikariConfig.schema = "public"
    val dataSource = HikariDataSource(hikariConfig)
    Database.connect(dataSource)
}