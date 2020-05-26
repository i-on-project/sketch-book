package org.ionproject.sketches.writeApi

import junit.framework.Assert.*
import org.junit.Test

class InsertEventsTest {

  @Test
  fun valid_request() {
    val req = HttpRequest(valid_insert_events_req_json)
    val resp = http_insert_events(req)
    println("Valid request: $resp")

    assertEquals(200, resp.status)
  }

  @Test
  fun invalid_request() {
    val req = HttpRequest(invalid_insert_events_req_json)
    val resp = http_insert_events(req)
    println("Invalid request: $resp")

    assertEquals(400, resp.status)
  }

}
