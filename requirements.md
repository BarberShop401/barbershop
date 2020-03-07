# Software Requirements

## Vision of BarbrShop
BarbRshop allows a user to harmonize with him/her/them- selves. Upon entering HarmonizR, the user may sing through the microphone and receive a live playback of an harmonized output of their song. The user may also record their song, which allows saving a recorded instance of the song to be listened to at their convenience. Extra audio manipulation software (in addition to the HarmonizR) are also at the user's disposal, such as the MotionFlippR (changes an audio stream's pitch based on the position of the phone). 

## Scope
Our app will:
- Allow a user to enter the HarmonizR module and speak or sing through the microphone to instantly hear an harmonized output. The user will also be able to change the tonal key, chords, and the number of voices they want to hear as the output. 
- In HarmonizR, the user will also be able to record songs upon tapping a record button. Upon stopping the record feature, the user can then access the Playback screen to view all saved recordings. In Playback, the user can play the tracks, edit the names of tracks, and delete unwanted ones. 
- The user also has access to a Settings page, which allows for basic modifications to the visuals and layout of the app. These modifications include changing Theme (Light/Dark), changing username, and the ability to prompt the user to save or not save a recorded track. 

Our app will NOT: 
- Allow users to play a haircut game or learn about how barbershops work. 
- Allow users to upload/download images or audio files to a cloud-based service. 
- Allow/Force users to purchase content or functionality. 

### MVP
In order for a minimum viable product, the app should: 
- Upon opening the app, take the user to a main screen. 
- Ask the user permission to access the phone's microphone. 
- Run the Oboe library as a wrapper in order to improve input/output latency and additionally manipulate the user's waveform input. 
- Manipulate the user's input audio in order to return the user's original audio in addition to 3 additional waveforms that effectively harmonize with the user's original audio based on the key of C. 

### Stretch
Stretch goals for this app include: 
- A position-vector based module, that when activated, allows a user to playback a previously recorded track and change the frequency of the recorded sound based on the position of the phone. 
- Allow a user to select a key as a root note to manipulate the waveform as desired. 
- Allow a user to select a chord to decide which frequencies to harmonize the user's waveform input with. 
- Allow a user to select a number of voices to harmonize with. 
- Allow a user to select musical intervals in relation to a selected key and chord, in order to enhance the harmony effect. 
- Allow a user to record their input stream, stop the input stream, and save that track to Room's database. 
- Allow a user to access stored tracks and play, edit the name of, and/or delete said track. 

### Data Flow
MVP: 
1. User opens the app. 
2. The app asks for permission to record audio. If selected yes, will resume as the below. 
3. Upon start of the main activity, the Oboe library is instantiated and begins running, which also captures audio through the user's phone's microphone. 
4. User's audio input is taken in by Oboe's AudioEngine class as audio frames. 
5. Oboe's AudioStream class takes in audio frames as samples. 
6. AudioStream will initiate a callback to AudioEngine in order to continue taking in audio frames as necessary. 
7. (will add more once we understand more)

## Non-Functional Requirements
Usability: 
- Users must be able to navigate the app in a user-friendly way that does not interfere with production of an output harmony. 
- Users must be able to navigate between pages of the app without errors. 

Testability: 
- Test the Oboe library
- Test the input audio stream
- Test the output audio stream
- Test harmonies