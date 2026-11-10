# Restful-Booker API Automation Framework

REST API test automation framework in **Java**, **Rest Assured** and **TestNG**, covering the authentication and full CRUD lifecycle of the public [Restful-Booker](https://restful-booker.herokuapp.com/apidoc/index.html) API.

## Tech stack
Java 17 · Rest Assured · TestNG · Maven · Jackson (POJOs) · JSON Schema Validator · Allure Reports · GitHub Actions

## Features
- Page-object style **endpoint classes** (`BookingApi`, `AuthApi`) so tests contain no HTTP details
- **POJO models** with Jackson for request and response bodies
- Reusable **RequestSpecification** (base URI, headers, auth cookie, Allure filter)
- Token fetched once per suite in a base class
- **Data-driven tests** using TestNG `@DataProvider`
- **Negative tests**: invalid id, missing fields, missing or invalid token, wrong content type
- **JSON schema validation** of responses
- **Allure report** and Surefire HTML report
- **CI** with GitHub Actions on every push and pull request

## Scenarios covered
| Area | Scenarios |
|------|-----------|
| Auth | valid login returns token, invalid login rejected |
| CRUD | create + read, PUT update, PATCH partial update, delete + verify 404, list ids, search by name |
| Data-driven | 4 booking data sets created and verified |
| Negative | 404 on bad id, 500 on missing fields, 403 without or with invalid token, 405 on missing booking update, wrong content type |
| Schema | booking response matches JSON schema |

## Project structure
```
src/main/java/com/sandhya/api
  config/      ConfigReader
  models/      Booking, BookingDates, BookingResponse, AuthRequest
  utils/       RequestSpecFactory, TestDataBuilder
  endpoints/   AuthApi, BookingApi
src/test/java/com/sandhya/api
  base/        BaseTest
  tests/       AuthTests, BookingCrudTests, DataDrivenBookingTests, NegativeTests, SchemaValidationTests
```

## How to run
Prerequisites: JDK 17+ and Maven 3.8+

```bash
git clone <your-repo-url>
cd restful-booker-api-tests
mvn clean test
```

Override config if needed with a system property, for example
`mvn test -Dbase.url=https://restful-booker.herokuapp.com`, or with a
`RESTFUL_BOOKER_*` environment variable such as `RESTFUL_BOOKER_BASE_URL`.

## Reports
- Surefire / TestNG HTML: `target/surefire-reports/index.html`
- Allure:
  ```bash
  allure serve target/allure-results
  ```
  (install with `brew install allure` or `scoop install allure`)

## Report screenshot
_Add a screenshot of your Allure report here._

## Notes
Restful-Booker is a shared public sandbox, so data can be reset at any time and responses can occasionally be slow. Re-run a failed test if the service is cold-starting.
