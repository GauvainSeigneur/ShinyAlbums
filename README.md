# Shiny Albums
A mobile application which uses Deezer api to display a list of albums and details page.

## Technologies & pattern
* Kotlin
* AndroidX
* Clean Architecture
* Android Architecture Components - ViewModel, LiveData, Room, Paging
* AnimatedVectorDrawable
* Retrofit/Okhttp - Type-safe HTTP client
* Gson - Serialization & deserialization
* Coroutine - Asynchronous and non-blocking programming
* Mockito - Mock dependencies
* Koin - Dependency injection
* Glide - Image loading

## Architecture choices

### Clean Architerture
This application is based on a semi clean architecture with :
#### DataAdapter
This module includes providers which will call fucntion of remote data service or locale data
service. It also includes Adapters(which are implementation of providers). Adapters have
the responsability to fetch data from remote or locale source.
They handle exceptions(e.g IOException) and transform response into a business model or
throw a dedicated exception.
#### Domain
In this module the useCases are in charge to call the provider and transform the result (a
business model or a dedicated exception) into an outcome<Model, Error> to be handled by
presentation layer.
#### Presentatiobn
Presentation are in charge to provides dedicated LiveDate or Event according to result received
by the domain layer. It tranform business model into Data to be displayed by View layer.
#### View
This module includes Activity, fragment or UI related widgets. It observes data from presentation
layer and can call function of ViewModel.

## State
I have implemented the online mode and add some tests on adapter and useCase. But I didn't have
enough time to implement offline mode, have a better covergae of my existing tests and add some
for the viewModel.

## Licence
```
Copyright 2020 Gauvain Seigneur

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