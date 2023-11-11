### Installation
`npm install -g artillery`

### Generate test data
Before running the tests, make sure to generate random test data
`npm i`
`npm run generate-test-data`

### Run tests
Replace target base url in config:
```
config:
  target: https://scc-backend-erikg.azurewebsites.net/rest
```

- `artillery run house-load-test.yml`
house-load-test.yml is used for a high load scenario, testing the impact of caching.

- `artillery run all-endpoints.yml`
all-endpoints.yml is used as and end to end test, testing all endpoints for moderate load.
Can be used for comparing the latencies for different endpoints.
