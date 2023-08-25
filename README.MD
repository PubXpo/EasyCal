# EasyCal Mobile App

## Overview

EasyCal is a mobile application designed to improve accessibility for students with visual impairments. It addresses the challenge of accessing and interpreting academic schedules in PDF format, which can be problematic for those who rely on screen readers or have difficulty distinguishing between colors. The app provides an accessible alternative to traditional academic calendars and offers features to enhance the academic experience of visually impaired students.

## Key Features

- **Academic Calendar:** EasyCal displays the academic calendar in an accessible format with detailed descriptions of important events and dates.

- **Bank Holiday Integration:** Users can view UK bank holidays, helping them identify when the campus is closed.

- **Accessibility Customization:** The app allows users to customize the color scheme to accommodate specific impairments, ensuring a tailored user experience.

- **Screen Reader Compatibility:** All app content is designed to be compatible with screen readers, providing text descriptions for visually impaired users.

- **External Resource Access:** EasyCal provides a link to the school's disability support services for easy access to additional resources and assistance.

## Getting Started

There is no sign-in required required to use EasyCal, and access all of its features. The app has been tested on various Android devices, ensuring compatibility.

## Styling

- EasyCal conforms to Android 11 or later platform requirements, ensuring optimal performance.

- The app features both light and dark modes to accommodate user preferences.

- Text is formatted for readability, using clear fonts and appropriate sizes.

- The app responds appropriately to changes in orientation and different screen sizes.

## Data Handling

- EasyCal allows users to customize their experience, with settings such as color schemes, semester options, and light/dark mode.

- The app reads data from an external API to provide up-to-date information about UK bank holidays.

- Calendar images for the current academic year are stored locally, and users can navigate through them using navigation buttons.

- User settings are stored in SharedPreferences, allowing for a personalized experience.

## Demonstrations

- EasyCal offers a well-designed form with different field types, including radio buttons, drop-down spinners, and toggle switches.

- The app utilizes device information to determine the current month when launched.

- In the absence of network connectivity, EasyCal provides alternative content, such as stored API data.

- Data is passed between screens, allowing users to customize their academic experience.

