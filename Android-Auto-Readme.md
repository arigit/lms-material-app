# Android Auto support in LMS-Material-App - a client for Lyrion Music Server
The goal of this reference guide is to help you get an LMS client running in your Android phone as a "music player", playing music in the phone itself: when connected to your car via Android Auto (streaming over a cellular network), or to your phone earbud's (streaming over wifi or a cellular connection).

This is not a generic LMS-Material-App guide, that can do much more than playing music in your phone: it can control remote players, help you setup your server etc.

If you follow the recommended settings and setup always-on connectivity to your LMS server, you can expect a just-works setup, where you can use the Lyrion App in your phone at home, in the office, in your car (via Android Auto) and expect it to work seamlessly. In my case, with this setup I stream (daily) high-resolution audio content (all FLACs, up to 24-bit/192 Khz) without any transcoding over WiFi and 5G Cellular, with zero audio drops; typical commutes last 45 minutes with both city and highway driving. Warning: you may need to setup transcoding (also covered in this guide) if you have a data volume "cap & throttling" plan in your cellular data plan or if your cellular network can't keep up with hi-res content; in my transcoding-free setup I consume around ~100GB of cellular data per month mostly driven by Lyrion audio streaming.

This setup is able to cover cellular coverage gaps that can last up to a few minutes (between 1 and 2 full tracks) ensuring no playback interruption. In the future if there is demand we may be able to add support in Android Squeezelite for more than 2-track dynamic buffering to cover more extended cellular coverage gaps.

## In-car UI Snapshots

<p align="center">
  <img center width="863" height="367" alt="image" src="https://github.com/user-attachments/assets/dc00ab9c-2cbb-4f2a-b6f5-37423f64078a" />
</p>

<p align="center">
<img width="868" height="373" alt="image" src="https://github.com/user-attachments/assets/2ad6fae5-5cbb-4ef8-9022-b230221a146e" />
</p>

<p align="center">
<img width="863" height="367" alt="image" src="https://github.com/user-attachments/assets/97cb691e-77d5-4865-8b24-749e56c10213" />
</p>

<p align="center">
<img width="868" height="373" alt="image" src="https://github.com/user-attachments/assets/673cf3f0-36b9-42cb-bb19-788867a138bc" />
</p>


## Requirements
Two apps are required, both can be found in https://f-droid.org/
- https://f-droid.org/en/packages/org.lyrion.squeezelite/
- https://f-droid.org/en/packages/com.craigd.lmsmaterial.app/

You will need at least version 0.11 of both for Android Auto to work.
Once installed, the LMS-Material-App shows as "Lyrion" in your phone's app list; so we will call it "Lyrion App" for short.

Squeezelite is the app that actually plays the music on your phone (and, through Android Auto, in the car). The Lyrion App is the controller: you browse your library and control playback with it, and it starts and stops Squeezelite when needed.

### VPN Tunneling

Your smartphone will need connectivity to your LMS server so that the Lyrion App can connect to it and get your music streamed to your phone while away from home. The simplest and most secure way to do this is a light VPN solution; the setup for this is out of scope of this guide but here are two pointers:

  - the simplest, most reliable and lighter (on the phone) own VPN solution I have found is Tailscale (https://tailscale.com/); they have a solid reputation RE privacy and ease of use, the linux client and the android clients are open source; some parts of their backend infrastructure are not. 
    -  install the client in your home-server that is running LMS (linux, mac, etc all supported; I use Tailscale's docker container), then add your home-server to your "Tailnet".
    -  install the client in your phone, add your phone to your Tailnet, done.
    -  turn Tailscale ON in your phone and forget about it.
    -  Your phone will always be able to connect to your home-server and it's apps (e.g. LMS, Home Assistant, etc) all the time regardless of how you connect to the internet: over cellular, over WiFi at home or at work; the VPN client is smart and when possible it routes all traffic peer to peer.
  
In my experience, it just works, all the time, no need to turn it off. It uses Wireguard under the hood which means it is very light on the phone CPU and battery. You can select if you want to funnel all your internet traffic on the VPN tunnel or just the traffic to your home-server.

  - your router / access point solution may include VPN support. For example, mid-range ASUS routers include support for various VPN flavours. The lightest is Wireguard, it's almost trivial to pair your phone with your router via a QR code your router's GUI will display. This solution will funnel all of the internet traffic of your phone to your home router, which will allow your phone to reach your LMS server.

### LMS requirements

Nothing special is required in your LMS server (other than following this setup guide). There are two optional LMS plugins that when installed, will add features to the user interface in the car:

  - `Ratings Light`: track ratings are shown, and can be set, in the car's `Now Playing` screen
  - `Rediscover Releases`: adds a **Rediscover Albums** list under Albums (for this, in addition to installing the add-on, you would need to also turn on **Show Rediscover Albums list** in the Lyrion App's settings -> Android Audio section)

Both can be found as 3rd Party Plugins under "Manage Plugins" in your LMS server.


## Key Features of this setup

- optimized for **in-car playback of a remote music collection**: your music stays on your Lyrion Music Server at home, and the phone streams it to the car through Android Auto over cellular

- tries to provide a 'just works' solution for LMS users: with the right settings in place (see recommendations below) it makes sure your local player (Squeezelite) is running and switched on, so that when you hit Play, playback 'just works'; options are provided to resume playback when the car connects and to work around unwanted play requests sent by cars

- it supports in-car queue display and basic management (add to queue, display queue)

- it supports and exposes shuffle mode in the car UI

- it exposes track ratings and allows setting of ratings in the car UI / Now Playing screen (this will only display if you have the ratings-light LMS plugin installed in your server)

- It adds various new Android-Auto specific configuration options
<p align="center">
<img width="480" height="723" alt="image" src="https://github.com/user-attachments/assets/c111600c-cc95-4984-84c1-a4a894eab1e2" />
</p>

- Browse your library from the car: albums (new, random, recently played, rediscover (seldom played, needs the [Rediscover Releases](https://github.com/arigit/LMS-Rediscover-Your-Music) LMS plugin), artists, playlists, favourites, and switch libraries and players.

- Advanced features
  - Copy the queue of any other player registered on LMS (to continue playback in your car) including current track position
  - Select virtual library


## Recommended Settings for Phone-Centric Users
For the simplified use case (streaming from their home server over cellular or over wi-fi), the following initial settings aim to provide a working setup, fast. So you can later tweak and customize them as needed.

Not every setting is listed - only the most relevant ones.

There are three points of configuration needed: 
  - Lyrion App settings (phone)
  - Squeezelite App settings (phone)
  - LMS Server Settings (Player Section)

### Settings in Lyrion App 
In the App Settings section enter the **Application** settings page
- Player App: `Squeezelite`
- Change device volume: `ON`
- Auto-start player: `ON`
- Add menu entry to start player: `ON`
- Stop player when Quit: `OFF`
- Android Auto:
  - Tab Order:
    - the first tab in the list will be shown first each time; I use: Albums
  - When an album is selected: `show tracks`
  - Show Rediscover Albums list: `ON` if the Rediscover Releases plugin is installed on your server
  - Ignore the car's play on connect: `ON`
  - Resume after reconnecting: `ON`
- Default Player:
  - Set this to the player name you chose in the Squeezelite app config
- Only Control Default: `ON`

### Settings in Squeezelite App
- Name:
  - set to any name e.g. `My Phone`; choose a name without apostrophes `'`
- Volume control: `Synchronized`
- Bit-rate limit: `Use server setting`
  - The recommended settings will stream the track as-is in the server to your phone; if your collection has hi-resolution lossless content, some (cellular) networks may be unable to provide the required throughput. If that happens you will get audio-cuts, if so then change this setting to e.g `192 kbps`; that will cause your server to transcode the content to mp3 and stream it at a far lower bitrate. You would also need to do this if your cellular service has a low volumetric quota (e.g. 10GB / month) that would get exhausted very quickly unless you transcode.
- Apply bit rate limit: `When on cellular or metered network`
- Buffer size: `Current + next song in full (dynamic buffer)`
  - Maximum size for Current Song and Next Song buffers: `256 MB`
  - These two settings will help you achieve audio-cut-free streaming during a drive that may traverse short periods of poor cellular coverage.
- Start player when launched: `ON`
- Restore Volume: `OFF`
- Stop on Power Off: `ON`
- Use Audio Focus: `ON`
- Keep device awake: `OFF`

- Use ID and Name: `OFF`
- Send Track Details: `OFF`
  - otherwise, in-car, a second active player app (Squeezelite App) shows in android auto besides Lyrion App, creating confusion
- Auto-start when device connects: `None`
- Start player when android auto connects: `OFF`
  - (Lyrion app will manage start/stop of Squeezelite App while in-car)
- Stop player on disconnect: `OFF`
- Android Auto:
  - Pause on android auto disconnect: `ON`
  - Pause player when android auto connects: `OFF`


### Settings in your Lyrion LMS Server, Player Section
To review and set the server-side config, for your phone player, you can use a desktop web browser to access the web UI of your server, or you can use your phone and the Lyrion App.

In the App Settings section enter the **Player** settings page, then if prompted to select a player (which could happen if you didn't yet set **Only control Default** to `ON`), select your phone's Squeezelite player **Name** (e.g. `My Phone`)

Then scroll to the bottom of the player settings page and tap **Extra Settings**

- **Audio** section
  - Power On Resume: `Stop at power off / Remain stopped at power on`
    - with this setting, switching the player off and on again (the Power button in the car) starts the current track from the beginning. If you prefer to continue where you left off, choose `Pause at power off / Remain paused at power on`: the Lyrion App then remembers the position and returns to it
  - Volume Control: `Volume control adjusts outputs`
  - Bitrate limiting: `No limit`
    - this setting can be overridden by the Squeezelite App in the phone if transcoding is needed, so it's safe to leave as `No limit`
  - Volume Adjustment / Replay Gain: Smart Gain
    - this setting only acts if your tracks are tagged with Replay Gain values, which is useful specially when shuffling tracks from different albums in a playlist
    
- **Player Volume Settings** section
  - Player Power-On Volume
    - Set last value: `Disabled`
- **Power Save** section
  - Enable Power Save: `OFF`

    
