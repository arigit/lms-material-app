<p align="center">
  <img center width="863" height="367" alt="image" src="https://github.com/user-attachments/assets/dc00ab9c-2cbb-4f2a-b6f5-37423f64078a" />
</p>

# Introduction
This is an Android-app that can act as a controller of a Lyrion Music Server (LMS), allowing you to play audio on any player connected to the LMS server; it can also be setup as a phone-centric music player used to browse and play content in your phone, when used in combination with the [squeezelite](https://github.com/arigit/squeezelite) app that needs to be installed in the phone as well for local (in-phone) playback.

It includes a fully-featured android-auto (in-car) interface, this is the key difference with the excellent [CDrummond/lms-material-app](https://github.com/CDrummond/lms-material-app). It builds upon the great work done by [chrober/lms-material-app](https://github.com/chrober/lms-material-app/tree/feature/android-auto) but changes the focus of the Android Auto support from 
"in-car server" to remote (in-home) server streaming, that would be your Lyrion server at home, with your phone reaching it over cellular + VPN / Tailscale.

Modifications to the upstream lms-material and squeezelite android apps run a bit deep as I found with them, almost invariably, after connecting the phone to the car and accessing the LMS-material app UI, Playback attempts failed for different reasons, typically related to squeezelite not running, or running but not started, or the wrong player got auto-selected for playback (so the kitchen speaker would start playing music shortly after turning on the car :), or wrong library view etc. Extensive changes done to ensure the primary phone-centric use case "just works" when you get in the car and hit Play or when auto-resume engages.

Changes were needed in squeezelite, the largest is related to buffering: larger buffer options and "next song" buffering are now supported. This is meant to avoid audio drops and stutter due to cellular coverage gaps; I tested this extensively during long commutes, streaming 24/96 and 24/192 original FLAC music with excellent results. It is also possible to setup squeezelite to have server-side mp3 transcoding but I haven't tested that.

The development of this fork has been done entirely with Claude Code. I made all the decisions, all the car testing and a lot of debugging, but all of the code development and the changes are entirely AI. 

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
For users seeking to mainly use lms-material-app + squeezelite in the phone, streaming from their home server, the following initial settings could help get a 'just works' user experience without all the trial and error

### Settings in lms-material-app (Lyrion app in the phone)


### Settings in squeezelite-app (Squeezelite app in the phone)


### Settings in your Lyrion LMS Server, Player Section
Select your Phone's player name and set:

- Audio


# Donations

Please donate to the current app author: https://github.com/CDrummond/lms-material-app

# Licenses

This app is provided under the [MIT License](LICENSE).

