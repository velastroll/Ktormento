package com.example

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.externalModule(){
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
    get("/externalModule") {
        call.respond("Hey! you're loading an external module :D")
    }
}
