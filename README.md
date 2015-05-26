# Android-Bootcamp
CodePath Android Bootcamp

Attending CodePath Android Bootcamp. This repository contains all the projects that I did in the bootcamp. See [Codepath](http://courses.codepath.com/courses/intro_to_android) for details.

## CodePath Android Bootcamp Week 3 - Twitter Client

Twitter Client is an android app that allows a user to view his Twitter timeline and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: 20 hours spent in total

## User Stories

The following **required** functionality is completed:

* [x]	User can **sign in to Twitter** using OAuth login
* [x]	User can **view tweets from their home timeline**
  * [x] User is displayed the username, name, and body for each tweet
  * [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
  * [x] User can view more tweets as they scroll with [infinite pagination](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews). Number of tweets is unlimited.
    However there are [Twitter Api Rate Limits](https://dev.twitter.com/rest/public/rate-limiting) in place.
* [x] User can **compose and post a new tweet**
  * [x] User can click a “Compose” icon in the Action Bar on the top right
  * [x] User can then enter a new tweet and post this to twitter
  * [x] User is taken back to home timeline with **new tweet visible** in timeline

The following **optional** features are implemented:

* [x] User can **see a counter with total number of characters left for tweet** on compose tweet page
* [x] User can **click a link within a tweet body** on tweet details view. The click will launch the web browser with relevant page opened.
* [x] User can **pull down to refresh tweets timeline**
* [x] User can **open the twitter app offline and see last loaded tweets**. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in offline mode.
* [ ] User can tap a tweet to **open a detailed tweet view**
* [x] User can **select "reply" from detail view to respond to a tweet**
* [x] Improve the user interface and theme the app to feel "twitter branded"

The following **bonus** features are implemented:

* [x] User can see embedded image media within the tweet detail view
* [ ] Compose tweet functionality is build using modal overlay

The following **additional** features are implemented:

* [x] Add animations between different activities
* [x] Media is embedded in timeline
* [x] Use custom actionbar to make app look like official twitter app
* [x] User can follow people from timeline itself
* [x] User can reply from timeline itself
* [x] User can retweet from timeline itself
* [x] User can mark tweet as favorite from timeline itself

## Video Walkthrough 

Here's a walkthrough of implemented user stories:
![demo](https://github.com/varungu/Android-Bootcamp/blob/master/SimpleTwitterClient/DEMO.gif?raw=true)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android

-------------------------------------------------------------------------------------------------------------------------------

## Android Bootcamp Week 2 - Google Image search application

This is an android demo application for searching images using google image search API. See [Codepath Week 2](http://courses.codepath.com/courses/intro_to_android/week/2#!assignment) for details.

Total time spent: 12 hrs

Completed stories:

1. User can enter a search query that will display a grid of image results from the Google Image API.
2. User can click on "settings" which allows selection of advanced search options to filter results
3. User can configure advanced search filters such as Size, Color filter, Type and Site
4. User can tap on any image in results to see the image full-screen
5. User can scroll down “infinitely” to continue loading more image results (up to 8 pages)
6. Advanced: Use the ActionBar SearchView or custom layout as the query box instead of an EditText
7. Advanced: User can share an image to their friends or email it to themselves
8. Advanced: Replace Filter Settings Activity with a lightweight modal overlay
9. Advanced: Improve the user interface and experiment with image assets and/or styling and coloring
10. Bonus: Use the StaggeredGridView to display improve the grid of image results
11. Bonus: User can zoom or pan images displayed in full-screen detail view
12. Extra: Allow user to open image web page in browser

Walkthrough of all user Stories:

![demo](https://github.com/varungu/Android-Bootcamp/blob/master/GoogleImageSearch/DEMO.gif)

-------------------------------------------------------------------------------------------------------------------------------


## Android Bootcamp Week 1 - Instagram popular photos application

This is an android demo application for displaying the popular photos from Instagram. See [Codepath Week 1](http://courses.codepath.com/courses/intro_to_android/week/1#!assignment) for details.

Total time spent: 16 hrs

Completed Stories:

1. User can scroll through current popular photos from Instagram
2. For each photo displayed, user can see the Graphic, Caption, Username, relative timestamp, like count, user profile image
3. Advanced: Add pull-to-refresh for popular stream with SwipeRefreshLayout
4. Advanced: Show latest comment for each photo (bonus: show last 2 comments)
5. Advanced: Display each photo with the same style and proportions as the real Instagram 6. Advanced: Display each user profile image using a RoundedImageView
7. Advanced: Display a nice default placeholder graphic for each image during loading 
8. Advanced: Improve the user interface through styling and coloring
9. Bonus: Allow user to view all comments for an image within a separate activity or dialog fragment
10. Bonus: Allow video posts to be played in full-screen using the VideoView
11. Extra: Infinite Scroll
12. Extra: Allow opening images in seperate activity and add zoom etc.
13. Extra: Allow sharing images from Photo Activity.

Walkthrough of all user Stories:

![demo](https://cloud.githubusercontent.com/assets/2263278/7558818/8c9c2eb4-f761-11e4-9d9f-1bd32630a83b.gif)

## License

    Copyright 2015 - Varun Gupta

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
