# Android Project 6 - *BitFit Plus*

Submitted by: **Nicolas Ortiz**

**BitFit Plus** is a health metrics app that allows users to track `body fat` and `body weight` in a Android native application.

Time spent: **7** hours spent in total

## Required Features

The following **required** functionality is completed:

- [X] **Use at least 2 Fragments**
- [X] **Create a new dashboard fragment where users can see a summary of their entered data**
- [X] **Use one of the Navigation UI Views (BottomNavigation, Drawer Layout, Top Bar) to move between the fragments**

The following **optional** features are implemented:

- [ ] **Add a more advanced UI (e.g: Graphing) for tracking trends in metrics**
- [ ] **Implement daily notifications to prompt users to fill in their data**

The following **additional** features are implemented:

- [X] Due to a glitch I have not resolved with adding new checkins, I implemented a workaround that reloads the log fragment upon each new value added. This completely masks the  issue from the user perspective!
- [X] Truncated metric average calculations to 2 decimal places.
- [X] Welcome/placeholder messages until user has at least one check-in provided.
- [X] Centered floating action button to launch the input dialog.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/heynicortiz/codepath_bitfit_plus/blob/master/Stories.gif?raw=true' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LICEcap](https://www.cockos.com/licecap/) for Windows

## Notes

Transferring/sharing data between the main activity and the different fragments led me to do some creative refactoring of my code to implement the split fragment idea.

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