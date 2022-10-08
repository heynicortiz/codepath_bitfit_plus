# Android Project 5 - *BitFit*

Submitted by: **Nicolas Ortiz**

**BitFit** is a health metrics app that allows users to track `body fat` and `body weight` in a Android native application.

Time spent: **14** hours spent in total

## Required Features

The following **required** functionality is completed:

- [X] **At least one health metric is tracked (based on user input)**
    - Chosen metric(s): `body fat percentage`, `body weight`
- [X] **There is a "create entry" UI that prompts users to make their daily entry**
- [X] **New entries are saved in a database and then updated in the RecyclerView**
- [X] **On application restart, previously entered entries are preserved (i.e., are *persistent*)**

The following **optional** features are implemented:

- [X] **Create a UI for tracking averages and trends in metrics**
- [X] **Improve and customize the user interface through styling and coloring**
- [X] **Implement orientation responsivity**
- [ ] **Add a daily photo feature**

The following **additional** features are implemented:

- [X] Quick welcome message is shown for new users (those with no check ins stored in the database)
- [X] Welcome message gets replaced by user statistics
- [X] Checkins are shown from the database in chronological order for date provided, not created. These are also sorted further by morning or evening session.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [Kap](https://getkap.co/) for macOS

## Notes

There is an issue related to the RecyclerView/Adapter when enough entries are made that results in multiple entries displaying the same for some reason. I was not able to trace this down yet, but the underlying data and the data shown in the list used to populate the adapter is correct. When the user restarts the app, all data is presented properly again.

## License

    Copyright [2022] [Nicolas Ortiz]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.