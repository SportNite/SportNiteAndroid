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
- `/presentation/data/auth` - package with authentication and classes caching user data
- `/presentation/data/firebase_storage` - package with classes uploading user profile image to Firebase Storage
- `/presentation/data/local` - package with classes caching data
- `/presentation/data/mappers` - package with functions mapping graphql models to domain/presentation models
- `/presentation/data/remote` - package with GraphQLService - class responsible with comunication with GraphQL server


#### `SportNiteAndroid/app/src/test` - package with unit tests


## Building and running

Easiest way to launch service is to utilize docker-compose setup:
```bash
git clone https://github.com/SportNite/SportNiteServer.git
cd SportNiteServer
```
After starting up both services (database and server) you can access GraphQL Playground at http://localhost:7150/playground.

## Testing

Assuming dotnet core toolchain is installed and MySQL database is up and running (with empty database 'sportnite' created):

```bash
git clone https://github.com/SportNite/SportNiteAndroid.git
cd SportNiteServer

```

If you want to change database for testing, create `SportNiteServer/.env` file with following content:


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
