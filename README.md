# SportNiteAndroid
<div>
<img src="https://cdn.freebiesupply.com/logos/large/2x/kotlin-1-logo-png-transparent.png" height="100"/>
<img src="https://2.bp.blogspot.com/--asT-h3qn_s/X0aLtRWOesI/AAAAAAAAPkY/iOkd702WAts7_4dIXlzQhyiJWGaL5f9CgCLcBGAsYHQ/s1600/JetpackCompose_logo.png" height="100"/>
<img src="https://i2.wp.com/programmingwithmosh.com/wp-content/uploads/2019/07/GraphQL_Logo.svg.png?fit=2000%2C2000&ssl=1" height="100"/>

</div>

## About
SportNite Android app written in Kotlin and Jetpack Compose. Connects with GraphQL Server, uses Firebase for Authentication (with Oauth 2)


<div>

[![Latest Release](https://img.shields.io/github/v/release/SportNite/SportNiteAndroid.svg?logo=github&style=for-the-badge)](https://github.com/SportNite/SportNiteAndroid/releases/latest)
[![Language](https://img.shields.io/github/languages/top/SportNite/SportNiteAndroid?style=for-the-badge)](https://github.com/SportNite/SportNiteAndroid)
[![License: BSD-3](https://img.shields.io/badge/License-BSD3-blue.svg?style=for-the-badge)](https://github.com/SportNite/SportNiteAndroid/blob/master/LICENSE.md)

  
</div>

## Specification
- written in Kotlin and Jetpack Compose
- data is fetched from GraphQL API and cached in memory
- apk is published on Google Play for testers
- Qodana code quality report: https://qodana.cloud/projects/3wwJW/reports/AYKR2

## Libraries used
- Jetpack Compose
- Kotlin Coroutines
- Coroutines Flow
- Orbit MVI
- Apollo GraphQL
- Firebase Auth
- Firebase Storage
- Store
- Hilt
- Coil
- Google Acompanist
- Lottie
- Image Cropper

## Architecture
App is based on MVI pattern. It consists of 3 layers:
- Presentation (ViewModels implementing Orbit MVI containers, UI in Jetpack Compose) - handles interactions with user on particular screens and gets data from repository by use cases
- Domain (Repository) - responsible for business logic
- Data - responsible for accessing data from api or from local storage (in our project memory or SharedPreferences) 

## Code organization
App is divided into modules:
### :app
Module with android app and navigation between features
### :feature
...
### :data
#### :offers
#### :meetings
#### :responses
#### :players
#### :user
...
### :core
#### :network
Module with GraphqlService. It's responsible for all calls to GraphQL API
#### :utils
#### :models
#### :commonui

...


## User documentation
User can create offers for sport meetings. Other users than can explore existing offers and accept offer. 
### Create an offer (1)
- ensure you are signed in, if not Login (2)
- go to "Moje spotkania" tab and press "+" button on bottom right corner
- enter date and time
- select sport
- select city
- optionally enter additional informations
- press "Dodaj ofertę"

### Login (2) - if you are not signed yet
- enter phone number
- at next screen, enter confirmation code from SMS and tap "Potwierdź"
- select avatar, type your name and birth date. Select sex and time availability
- next, select in what sports are you interested in
- confirm, and select level of advance in chosen sports 

### View incoming meetings (3)
- tap "Home" tab
- now you can view incoming meetings

### Search for people (4)
- tap "Szukaj ludzi tab"
- use filter to show people only interested in given sport
- after tapping user, you can see his level of advance, phone number and time availability

### Accept offer response (5)
- tap "Moje spotkania"
- select offer
- tap "Akceptuj ofertę" 

### Remove offer (6)
- tap "Moje spotkania"
- select offer
- tap "Usuń ofertę"


## Running
Easiest way to run SportNite Android app is to download apk from Github Releases

## Code quality report
With `qodana` installed:
```bash
qodana scan --show-report
```

<img width="1552" alt="qodana" src="https://user-images.githubusercontent.com/21008961/212775703-4be8b068-4871-4111-9b1f-49f7eaf578b9.png">


## Screenshots

| Home Screen | My Meetings Screen | Sign In Screen |
| :-------- | :------- | :------- |
| <img src="screenshots/ss_home.jpg" width="250"/> | <img src="screenshots/ss_my_meetings.jpg" width="250"/> | <img src="screenshots/ss_sign_in.jpg" width="250"/> |

| Choose Sports Screen | Choose Advance Level Screen | Account Details Screen |
| :-------- | :------- | :------- |
| <img src="screenshots/ss_choose_sports.jpg" width="250"/> | <img src="screenshots/ss_choose_level.jpg" width="250"/> | <img src="screenshots/ss_edit_profile.jpg" width="250"/> |

| Find Players Screen | Sport Screen | Add Offer Screen |
| :-------- | :------- | :------- |
| <img src="screenshots/ss_find_players.jpg" width="250"/> | <img src="screenshots/ss_sport_screen.jpg" width="250"/> | <img src="screenshots/ss_add_offer.jpg" width="250"/> |




## License
Project is licensed under the BSD 3-clause license. See [LICENSE](https://github.com/SportNite/SportNiteAndroid/blob/master/LICENSE.md) for more details.
