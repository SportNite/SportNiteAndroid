# SportNiteAndroid

## About
SportNite Android app written in Kotlin and Jetpack Compose. Connects with GraphQL Server, uses Firebase for Authentication (with Oauth 2)

## Specification
- written in Kotlin and Jetpack Compose
- data is fetched from GraphQL API and cached in memory
- apk is published on Google Play for testers
- Qodana code quality report: https://qodana.cloud/projects/3wwJW/reports/AYKR2

## Architecture
App is based on MVI pattern. It consists of 3 layers:
- Presentation (ViewModels implementing Orbit MVI containers, UI in Jetpack Compose) - handles interactions with user on particular screens and gets data from repository by use cases
- Domain (Repository) - responsible for business logic
- Data - responsible for accessing data from api or from local storage (in our project memory or SharedPreferences) 

## Code organization
#### `SportNiteAndroid/app/src/main` - package with production code
##### `/graphql` - package with graphql schema (.json extension) and mutations and queries (.graphql extensions)
##### `/res` - package with app resources like icons and images, translated texts
##### `/java/com/pawlowski/sportnite` - package with kotlin code of the app
- `MainActivity.kt` - starting point of the app. Its responsible for displaying navigation composable
- `/di` - package with dependency injection related classes
- `/presentation/view_models_related/{screen}` - package with view models, ui state, side effects for particular screen
- `/presentation/use_cases` - package with use cases
- `/presentation/ui` - package with ui composables
- `/presentation/models` - package with presentation data models
- `/presentation/mappers` - package with functions mapping presentation models to domain models
- `/presentation/domain` - package with repository 

- `Dto` - module containing `DTO`s for GraphQL queries and mutations (`DTO` stands for `Data Transfer Object`) 
- `Assets` - module containing data for seeding database
- `Data` - module containing `Query` and `Mutation` classes for GraphQL schema
- `Exceptions` - module containing custom exceptions and exception handlers

#### `SportNiteAndroid/app/src/test` - package with unit tests


### Interfaces

- `Database/DatabaseContext.cs` - database context for Entity Framework Core
- `Services/AuthService.cs` - service for handling authentication
- `Services/OfferService.cs` - service for handling offers CRUD
- `Services/PlaceService.cs` - service for handling places CRUD and seeding from file
- `Services/WeatherService.cs` - service for requesting weather forecast from https://open-meteo.com/ 
- `Services/ResponseService.cs` - service for handling responses CRUD, accepting or declining it
- `Services/UserService.cs` - service for handling users management
- `Services/Utils.cs` - contains utility functions

## GraphQL API
In order to explore GraphQL API you can use GraphQL Playground. "Docs" tab (on the right side) shows interactive API explorer.
Some queries/mutations have additional descriptions for better understanding by the developer.

![graphql_playground.png](screenshots/graphql_playground.png)

## Building and running

Easiest way to launch service is to utilize docker-compose setup:
```bash
git clone https://github.com/SportNite/SportNiteServer.git
cd SportNiteServer
docker-compose up -d --build
```
After starting up both services (database and server) you can access GraphQL Playground at http://localhost:7150/playground.

## Testing

Assuming dotnet core toolchain is installed and MySQL database is up and running (with empty database 'sportnite' created):

```bash
git clone https://github.com/SportNite/SportNiteServer.git
cd SportNiteServer
dotnet test
```

If you want to change database for testing, create `SportNiteServer/.env` file with following content:

```env
MYSQL_CONNECTION="server=somehost ; port=3306 ; database=somedatabase ; user=someuser ; password=somepassword"
```

## Code quality report
With `qodana` installed:
```bash
qodana scan --show-report
```

![qodana.png](screenshots/qodana.png)

## License
Project is licensed under the BSD 3-clause license. See [LICENSE](LICENSE) for more details.
Dependencies licenses are listed in [dependencies-licenses](dependencies-licenses.txt) file.
![deps.png](screenshots/deps.png)
