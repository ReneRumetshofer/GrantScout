ALTER TABLE parsed_call
    ADD COLUMN parsed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW();

CREATE INDEX idx_parsed_call_json_data ON parsed_call USING GIN (json_data);

COMMENT ON COLUMN parsed_call.json_data IS 'Contains JSON data of the following structure:
{
  "name": "String",
  "topic": "String",
  "shortDescription": "String",
  "longDescription": "String",
  "organization": "String",
  "grantCallSum": "String",
  "applicationDeadlines": {
    "from": "String (DD.MM.YYYY HH:MM)",
    "to": "String (DD.MM.YYYY HH:MM)"
  },
  "regions": "String",
  "targetGroup": "String",
  "website": "String",
  "contact": [
    {
      "contactPerson": "String",
      "email": "String",
      "telephone": "String"
    }
  ],
  "optional": {
    "status": "String",
    "frequency": "String",
    "preconditions": "String",
    "neededDocuments": "String",
    "evaluationCriteria": "String",
    "applicationPlatform": "String",
    "costsOfApplication": "String",
    "grantType": "String",
    "grantDuration": "String",
    "grantQuota": "String",
    "possibleActivities": "String"
  }
}';