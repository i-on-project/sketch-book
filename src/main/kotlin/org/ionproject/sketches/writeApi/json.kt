package org.ionproject.sketches.writeApi

const val invalid_insert_events_req_json = """
{
    "school": {
        "name": "INSTITUTO SUPERIOR DE ENGENHARIA DE LISBOA",
        "acr": "ISEL",
        "additionalProperties": 1
    },
    "programme": {
    },
    "calendarTerm": "1718v",
    "calendarSection": "LI11D",
    "courses": [
        {
            "acr": "ALGA[I]",
            "events": [
                {
                    "description": "This event is lacking the title property",
                    "location": [ "E.1.31" ],
                    "beginTime": "14:00:00",
                    "duration": "1:30:00",
                    "weekday": [ "MO" ]
                }
            ]
        },
        {
            "label": {
              "acr": "M2[I]",
              "name": "Matemática II"
            },
            "events": [
                {
                    "title": "Aula de M2",
                    "location": [ "G.2.30" ],
                    "beginTime": "14:00:00",
                    "duration": "1:30:00",
                    "weekday": [ "TU" ]
                },
                {
                    "title": "Aula de M2",
                    "location": "G.2.30",
                    "beginTime": "14:00:00",
                    "duration": "1:30:00",
                    "weekday": [ "FR" ]
                }
            ]
        }
    ]
}
  """

const val valid_insert_events_req_json = """
{
    "school": {
        "name": "INSTITUTO SUPERIOR DE ENGENHARIA DE LISBOA",
        "acr": "ISEL"
    },
    "programme": {
        "name": "Licenciatura Engenharia Informática e Computadores"
    },
    "calendarTerm": "1718v",
    "calendarSection": "LI11D",
    "courses": [
        {
            "label": {
              "acr": "ALGA[I]"
            },
            "events": [
                {
                    "title": "Aula de ALGA",
                    "description": "Aulas teoricas de ALGA",
                    "location": [ "E.1.31" ],
                    "beginTime": "14:00:00",
                    "duration": "1:30:00",
                    "weekday": [ "MO" ]
                }
            ]
        },
        {
            "label": {
              "acr": "M2[I]",
              "name": "Matemática II"
            },
            "events": [
                {
                    "title": "Aula de M2",
                    "location": [ "G.2.30" ],
                    "beginTime": "14:00:00",
                    "duration": "1:30:00",
                    "weekday": [ "TU" ]
                },
                {
                    "title": "Aula de M2",
                    "location": [ "G.2.30" ],
                    "beginTime": "14:00:00",
                    "duration": "1:30:00",
                    "weekday": [ "FR" ]
                }
            ]
        }
    ]
}
  """

const val insert_events_req_schema =
  """
    {
  "definitions": {
    "academicObject": {
      "${'$'}id": "#academicObject",
      "type": "object",
      "properties": {
        "name": {
          "type": "string",
          "minLength": 2,
          "maxLength": 50
        },
        "acr": {
          "type": "string",
          "minLength": 2,
          "maxLength": 10
        }
      },
      "anyOf": [
        { "required": [ "name" ] },
        { "required": [ "acr" ] }
      ],
      "additionalProperties": false
    },

    "event": {
      "${'$'}id": "#event",
      "type": "object",
      "properties": {
        "title": { "type": "string" },
        "description": { "type": "string" },
        "location": {
          "type": "array",
          "items": { "type": "string" }
        },
        "beginTime": { "type": "string" },
        "duration": { "type": "string" },
        "weekday": {
          "type": "array",
          "items": {
            "type": "string",
            "enum": [ "MO", "TU", "WE", "TH", "FR", "SA", "SU" ]
          }
        }
      },
      "required": [ "title", "beginTime", "duration" ],
      "additionalProperties": false
    },

    "course": {
      "${'$'}id": "#course",
      "properties": {
        "label": { "${'$'}ref": "#academicObject" },
        "events": {
          "type": "array",
          "items": { "${'$'}ref": "#event" }
        }
      },
      "required": [ "label", "events" ],
      "additionalProperties": false
    }
  },

  "${'$'}id": "#root",
  "type": "object",
  "properties": {
    "school": { "${'$'}ref": "#academicObject" },
    "programme": { "${'$'}ref": "#academicObject" },
    "calendarSection": { "type": "string" },
    "calendarTerm": { "type": "string" },
    "courses": {
      "type": "array",
      "items": { "${'$'}ref": "#course" }
    }
  },
  "required": [ "school", "programme", "courses", "calendarTerm" ],
  "additionalProperties": false
} 
"""
