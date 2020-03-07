# Barbershop

An Android-based entertainment app that provides a singing user with harmonizing vocals.

## Team Members
- Vik Akam
- Shingo Nakajima
- Lucas Wilber

## Project Management: Trello
https://trello.com/b/0Q5UCPBG/401-final-project-management

## User Stories (6)
#### 1
As a user I want the ability to record audio so that the app will return some modified audio (for entertainment)
### Feature Tasks: 
1. Record audio and store it to local storage
2. Use a gestures control to modify the speed. 
Modify audio with Oboe library 

Acceptance test: Ensure the audio is playing back at different speeds. 

### 2
As a user, I want a clean looking app so that I will feel like going back to it later. 
### Feature Tasks: 
1. Using colors (not default colors)
2. Using custom fonts
3. Small logo (barbershop hat) 

Acceptance test: Ensure that people enjoy the look. 

#### 3.
As a user, I want fast responsiveness with the playback so that it’s not distracting to record audio.
#### Feature Tasks: 
1. Implement Oboe library
2. Tweak with buffer size and sample size 

Acceptance test: Ensure that the playback is at 0 latency. 

#### 4
As a user, I want to be able to save recordings so that I can listen to them later. 
### Feature Task: 
1. Save recordings to RoomDB(local storage) so user can play the audio back 
2. Edit the name of the audio
3. Delete the audio from saved 
4. Play audio from saved playback page 

Acceptance Test: Ensure that audio can be saved in local storage 

#### 5  
As a user I want to hear a harmonized version of my input audio so that I get a harmony back(e.g. duet) 
### Feature Task: 
1. Add sliders for the number of voices you want to create a harmony with.  
2. Add another slider to set the harmonizing key 

Acceptance Test: Ensure that harmony is being created by checking that the 3rd, 4th or 5th, etc of the key is being played

#### 6 STRETCH GOAL:
As a user, I want to be able to log in/out so that I can have a profile
### Feature Task: 
1. Implement auth   
2. Login page 

Acceptance Test: Ensure that a user profile is created 

