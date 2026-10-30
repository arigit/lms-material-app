<p align="center">
  <img center width="863" height="367" alt="image" src="https://github.com/user-attachments/assets/dc00ab9c-2cbb-4f2a-b6f5-37423f64078a" />
</p>

# Introduction to this fork
This is an Android-app that can act as a controller of a Lyrion Music Server (LMS), allowing you to play audio on any player connected to the LMS server; it can also be setup as a phone-centric music player used to browse and play content in your phone, when used in combination with the [squeezelite](https://github.com/arigit/squeezelite) app that needs to be installed in the phone as well for local (in-phone) playback.

It includes a fully-featured android-auto (in-car) interface, this is the key difference with the excellent [CDrummond/lms-material-app](https://github.com/CDrummond/lms-material-app). It builds upon the great work done by [chrober/lms-material-app](https://github.com/chrober/lms-material-app/tree/feature/android-auto) but changes the focus of the Android Auto support from 
"in-car server" to remote (in-home) server streaming, that would be your Lyrion server at home, with your phone reaching it over cellular + VPN / Tailscale.

**The only purpose of this fork is to add android-auto support to upstream lms-material-app and squeezelite-app. I will do my best to keep up with new upstream releases until a PR for this functionality is accepted** which could take a long time given the amount of changes.

Modifications to the upstream lms-material and squeezelite android apps run a bit deep as I found with them, almost invariably, after connecting the phone to the car and accessing the LMS-material app UI, Playback attempts failed for different reasons, typically related to squeezelite not running, or running but not started, or the wrong player got auto-selected for playback (so the kitchen speaker would start playing music shortly after turning on the car :), or wrong library view etc. Extensive changes done to ensure the primary phone-centric use case "just works" when you get in the car and hit Play or when auto-resume engages.

Changes were needed in squeezelite, the largest is related to buffering: larger buffer options and "next song" buffering are now supported. This is meant to avoid audio drops and stutter due to cellular coverage gaps; I tested this extensively during long commutes, streaming 24/96 and 24/192 original FLAC music with excellent results. It is also possible to setup squeezelite to have server-side mp3 transcoding but I haven't tested that.

The development of this fork has been done entirely with Claude Code. I made all the decisions, all the in-car testing and a lot of debugging, but all of the code development and the changes are entirely AI. 

I hope that the changes can be merged back to the original repo ([CDrummond/lms-material-app](https://github.com/CDrummond/lms-material-app) and this repo eventually shelved. 


## Requirements
Two apps are required, pick the latest releases of both:
- https://github.com/arigit/squeezelite/releases
- https://github.com/arigit/lms-material-app/releases

The APKs need a recent android platform and need to be side-loaded.
These apps can be installed in parallel with the upstream versions.

## Key New Features
This fork builds on the work done by upstream adding:

- optimized for **in-car playback of a remote music collection**: your music stays on your Lyrion Music Server at home, and the phone streams it to the car through Android Auto over cellular
<p align="center">
<img width="863" height="367" alt="image" src="https://github.com/user-attachments/assets/bef58d6a-b661-4a83-9649-3c617b2390d3" />
</p>

- tries to provide a 'just works' solution for LMS users: with the right settings in place (see recommendation below) it will ensure your local player (squeezelite) is running and enabled so when you hit Play, it just works; options are provided to auto-resume queue playback upon car connection and to work around unwanted playback requested sent by cars

- it supports in-car queue display and basic management (add to queue, display queue)
<p align="center">
<img width="863" height="367" alt="image" src="https://github.com/user-attachments/assets/97cb691e-77d5-4865-8b24-749e56c10213" />
</p>
- it supports and exposes shuffle mode in the car UI

- it exposes track ratings and allows setting of ratings in the car UI / Now Playing screen (this will only display if you have the ratings-light LMS plugin installed in your server)

- It adds various new Android-Auto specific configuration options
<p align="center">
<img width="480" height="723" alt="image" src="https://github.com/user-attachments/assets/c111600c-cc95-4984-84c1-a4a894eab1e2" />
</p>

- Browse your library from the car: albums (new, random, recently played, rediscover (seldom played), artists, playlists, favourites, and switch libraries and players.

- Advanced features
  - Copy the queue of any other player registered on LMS (to continue playback in your car) including current track position
  - Select virtual library


## Recommended Settings for Phone-Centric Users
For users seeking to mainly use lms-material-app + squeezelite in the phone, streaming from their home server, the following initial settings could help get a 'just works' initial user experience without all the trial and error.
The list of settings below is not exhaustive.
There are three configuration points to check: each of the android apps, and the settings in the server-side for your phone.

### Settings in lms-material-app (Lyrion app in the phone)
- Player App: Squeezelite (ar)
- Auto-start player: ON
- Add menu entry to start player: ON
- Stop player when Quit: ON
- Android Auto:
  - When album is selected: show tracks
  - Ignore the car's play on connect: ON
  - Resume after reconnecting: ON
- Default Player: (set to the player name you chose in the squeezelite app config; avoid apostrophes "'" in this name)
- Only Control Default: ON

### Settings in squeezelite-app (Squeezelite app in the phone)
- Name: choose a name without apostrophes "'"
- Volume control: Synchronized with Android
- Bit-rate limit: Server setting
- Buffer size: 64MB (+46MB next-track buffer)
- Restore Volume: OFF
- Stop on Power Off: ON
- Use Audio Focus: ON
- Use ID and Name: OFF
- Send Track Details: OFF (otherwise, in-car a second active player shows in android auto besides lms-material, creating confusion)
- Auto-start when device connects: None
- Start player when android auto connects: OFF (lms-app will manage start/stop in-car)
- Stop player on disconnect: OFF
- Android Auto:
  - Pause on android auto disconnect: OFF


### Settings in your Lyrion LMS Server, Player Section
To inspect the server-side config, for your phone player, you can use a desktop web browser to access the web UI of your server, or you can use your phone and the lms-material-app after setting: "Only control default": OFF  (put it back ON after configuring the below)

Select your Phone's player name and: scroll to the bottom of the config page and select "Extra Settings"

- Audio
  - Power On Resume: Stop at power off / Remain stopped at power on
  - Volume Control: volume control adjusts outputs
  - Bitrate limiting: No limit (here you can force the server to transcode to mp3 if your cellular network doesn't offer enough bandwidth)
  - Volume Adjustment / replay gain: Smart Gain (only acts if your tracks are tagged with Replay Gain, which is useful specially when shuffling tracks from different albums)
    
- Player Power-On Volume
  - Set last value: Disabled
    

# Donations

Please donate to the current app author: https://github.com/CDrummond/lms-material-app

# Licenses

This app is provided under the [MIT License](LICENSE).

