<p align="center"><img src="logo/1024px.png" alt="OneShoot" height="200px"></p>


[![CircleCI](https://circleci.com/gh/SmashKs/OneShoot/tree/master.svg?style=svg)](https://circleci.com/gh/SmashKs/OneShoot/tree/master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/df651d8c66c64905b606d5627223b8e0)](https://www.codacy.com/app/pokk/OneShoot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=SmashKs/OneShoot&amp;utm_campaign=Badge_Grade)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)

# OneShoot

This is an application for analyzing a picture you took or are selected from the photo. If you give
app a scenic photo then app might give you the information of the **place name**, the **similar
place photo**, ... etc. In the near future, we'll add more function for anaylzing the picture.

# Show Case

# Architecture

We're using Clean Architecture to build this App. There're few layers as well-known
**Presentation**, **Domain**, **Data** layer. Also, I add an extra universal module *ext* for making
extension library in Kotlin.


### Presentation

As long as you know Clean Architecture, this layer is responsible in showing the data from the
**Data layer** and handle thread to UI thread for changing the UI state and here is including
Android SDK.

We're using the **coroutine** not **RxJava** here because the coroutines are lighter than thread,

### Domain

Here's totally pure Kotlin library without Android SDK.

<img src="https://user-images.githubusercontent.com/5198104/40908231-1baef130-6821-11e8-91c7-ca7031987428.png" width="800" height="400" />

### Data

We're still using **Retrofit2 + RxJava** here. This layer is including the Android SDK in order to
using the database and Firebase service.

<img src="https://user-images.githubusercontent.com/5198104/40910615-8ec6dd6c-6827-11e8-8265-28511082f95e.png" width="600" height="600" />

## Whole Architecture


# Used libraries

- Coroutine
- Anko
- RxKotlin
- Retrofit 2
- Gson
- Live Data
- Firebase
- DBFlow
- Kodein
- Glide

We'll focus on using **Android Architecture Component**, near future they will be added one by one.

# License

```
Copyright (C) 2018 SmashKs

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

