package io.github.mainyf.mc_manager.internal

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.http.HttpStatus

internal class HttpStatusDeserializer : JsonDeserializer<HttpStatus>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): HttpStatus? {
        return HttpStatus.values().find {
            it.value() == parser.valueAsInt
        }
    }
}

internal class HttpStatusSerializer : JsonSerializer<HttpStatus>() {
    override fun serialize(value: HttpStatus?, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeRawValue(value?.value().toString())
    }

}