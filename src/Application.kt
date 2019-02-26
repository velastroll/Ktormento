package com.example

import com.example.Model.Employee
import com.example.dao.DAOFacadeDatabase
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.basic
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import io.ktor.locations.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.features.*
import io.ktor.gson.*
import org.jetbrains.exposed.sql.*

/**
 * Classes used for the locations feature to build urls and register router
 */

@Location("/external")
class externalModule()

/**
 * DAO
 */
val dao = DAOFacadeDatabase(Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver"))


/**
 * Application entry
 */
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    dao.init()
    install(Locations)
    install(ContentNegotiation) {
        gson {}
    }
    install(Authentication) {

        /**
         * Basic authentication: access if name = password
         */

        basic(name = "myauth1") {
            realm = "Ktor Server"
            validate { credentials ->
                // Change to check credentials with DB values
                if (credentials.name == credentials.password) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

    val client = HttpClient(Apache) {
    }

    routing {
        externalModule()

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }

        authenticate("myauth1") {
            get("/styles.css") {

            }
        }


        route("/employees"){
            get {
                call.respond(dao.getAllEmployees())
            }
            post {
                val emp = call.receive<Employee>()
                dao.createEmployee(emp.name, emp.email, emp.city)
            }
            put {
                val emp = call.receive<Employee>()
                dao.updateEmployee(emp.id, emp.name, emp.email, emp.city)
            }
            delete("/{id}") {
                val id = call.parameters["id"]
                if(id != null)
                    dao.deleteEmployee(id.toInt())
            }
        }
    }
}
