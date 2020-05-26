package org.ionproject.sketches.writeApi

import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion

data class HttpRequest(
  val payload: String,
  val headers: Map<String, String> = mapOf()
)

class HttpResponse(
  val payload: String,
  val status: Int,
  val headers: Map<String, String> = mapOf()) {

  override fun toString(): String {
    return """Status: $status
Headers: ${headers.entries}

Payload:
${payload.prependIndent("\t")}
"""
  }
}

/**
 * Retrieves ValidationMessage from Schema->JSON validation.
 * @return true - if JSON is a valid object for the Schema
 */
fun validate_json(schema: JsonSchema, json: String): List<String> {
  val node = ObjectMapper()
    .readTree(json)

  return schema
    .validate(node)
    .map { it.message }
}

fun http_insert_events(req: HttpRequest): HttpResponse {
  val schema = JsonSchemaFactory
    .getInstance(SpecVersion.VersionFlag.V201909)
    .getSchema(insert_events_req_schema)

  val messages = validate_json(schema, req.payload)
  if (messages.isEmpty()) {
    return HttpResponse("OK", 200)
  }
  return HttpResponse(messages.reduceRight { s, acc -> acc + "\n" + s }, 400)
}

