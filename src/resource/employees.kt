package com.example.resource

import com.example.dao.DAOEmployee
import com.example.model.Employee
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.employees(dao: DAOEmployee){
    /**
     * Now we can define the type of available operations:
     * get<External> {
     *          ...
     *          call.redirect(ADondeQuieras())
     *          ...
     *          call.respond(FreeMarkerContent("my-template.ftl", mapOf("user" to user, "date" to date, "code" to code), user.userId))
     *      }
     * post <External> {
     *          ...
     *          val post = call.receive<Parameters>()
     *          val x = post["x"]?.toLongOrNull() ?: return@post call.redirect(it
     *          val y = post["y"] ?: return@post call.redirect(it}
     *          ...
     *          }
     * delete<External> {...}
     * put<External>{...}
     */

    route("/employees") {
        get("/{id}") {
            val id = call.parameters["id"]
            try {
                if (id != null)
                    call.respond(dao.getEmployee(id.toInt())!!)
            } catch (e: KotlinNullPointerException) {
                call.respond(HttpStatusCode.NotFound)
            }
        }

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
            if (id != null)
                dao.deleteEmployee(id.toInt())
        }
    }
}
