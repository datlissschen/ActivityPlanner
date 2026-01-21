# Project Activity Planner README

A full-stack activity management system designed to act as a digital scrapbook. This project is optimized for a Raspberry Pi deployment, accessible via a secure VPN, and features a hand-drawn, "messy" scrapbook aesthetic that provides a tactile feel on laptop and touchscreen browsers.

---

## Design Philosophy

The UI is built to mimic a physical scrapbook, featuring:

* Hand-Drawn Aesthetics: Utilizing the 'Patrick Hand' cursive font and irregular border-radials to create an organic, "blob-like" paper feel.
* Paper Textures: A fixed parchment background that persists across all pages to reinforce the book metaphor.
* Sticker Elements: UI components like weather icons and action buttons are designed to look like physical stickers and washi tape.
* Laptop and Pi Optimized: Compact scaling and reduced paddings ensure a "no-scroll" experience on 1920x1080 screens, ideal for a dedicated Raspberry Pi dashboard.

---

## Key Features

### 1. The Dashboard (Daily Timeline)

* Horizontal Polaroid Slider: Activities (Ideas) are displayed as Polaroid cards (with photos) or compact notes (text-only) in a scrollable grid.
* Native Scroll with Snap: Optimized for touchpad/mousepad use with scroll-snap-type: x proximity for perfect card alignment.
* Hand-Drawn Scrollbar: A custom-styled "Travel Path" scrollbar featuring a dashed track and a blue "Washi Tape" thumb.
* "Now" Progress Line: A vertical red line that moves across the timeline to indicate current progress through the day (08:00 - 22:00).

### 2. Active Adventure Management

* Dynamic Weather Sticker: A circular floating sticker on the banner that cycles through multiple selected weather types every 3 seconds using a custom JavaScript interval.
* Expedition Banner: A central hub showing the current "Active Adventure," linking directly to the overview.

### 3. Google Calendar Integration

* Automated Sync Bot: Seamlessly synchronizes activity "Ideas" with Google Calendar.
* Last Synced Tracking: Displays the last successful synchronization time on the idea edit page.
* Cloud Connectivity: Allows for planning on the go via mobile devices while keeping the central Scrapbook updated.

---

## Technical Stack

### Backend

* Java (Spring Boot): The core engine handling business logic, database interactions, and the Google Calendar API.
* Thymeleaf: Server-side template engine for rendering dynamic HTML fragments.
* Spring Data JPA: For managing persistent entities like Expedition and Idea.

### Frontend

* CSS Architecture: Uses custom properties, Flexbox, and clamp() for responsive, laptop-friendly layouts.
* Vanilla JavaScript: Handles the kinetic drag scrolling, weather cycling, and real-time timeline updates.
* Layout Dialect: Uses layout:decorate to maintain a consistent base template across all pages.

### Infrastructure and DevOps

* Raspberry Pi: Target deployment hardware for a dedicated home dashboard.
* VPN and Certificates: Secure remote access via a VPN tunnel, utilizing certificates for encrypted communication.
* File Uploads: Local storage for user-uploaded activity photos, served via the /uploads/ path.

---

## Data Models

### Expedition

Represents a timeframe (e.g., a vacation).

* typicalWeather: A Set of WeatherType enums (Sunny, Rainy, etc.).
* getWeatherEmojisString(): Custom helper method to provide a comma-separated string for JavaScript cycling.

### Idea

Represents a specific activity.

* photoPath: Path to the uploaded Polaroid image.
* startTime / endTime: Temporal data used for timeline placement and Google Calendar syncing.
* expeditions: A Many-to-Many relationship linking ideas to specific adventures.

---

## Setup and Installation

1. Database: Configure your relational database in application.properties.
2. Google API:
* Set up a project in the Google Cloud Console.
* Enable the Calendar API.
* Place your credentials.json in the project root.


3. Deployment:
* Ensure the Raspberry Pi has Java 17+ installed.
* Configure the VPN and install the necessary certificates on the client device.


4. Static Assets: Place paper-bg.png in src/main/resources/static/images/.

---

## Maintenance

* Laptop Spacing: If the footer buttons require scrolling, adjust the margin-bottom of the .summer-header and the padding of the main tag in main.css.
* Script Execution: Always ensure the Weather Switcher logic is placed at the top of the DOMContentLoaded block to prevent it being blocked by conditional returns in the slider logic.
