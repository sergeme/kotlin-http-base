package me.sersch.http.helpers

import io.ktor.http.HttpStatusCode

class Helper {
    //Used to wrap http statuscodes and messages/content
    class Response(
        var responseCode: HttpStatusCode,
        var responseObject: Any
    )
}

