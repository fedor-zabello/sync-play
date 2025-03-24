import {sendSourceUrlSyncMessage, sendSyncMessage} from "./web-socket.js";
import {extractVideoId} from "./videoUtils.js";

let player = null;
let ignorePlayerEvents = false;

export async function loadPlayer() {
    const youtubeContainer = document.getElementById('youtube-container');

    try {
        const response = await fetch('/youtube-iframe');
        if (response.ok) {

            youtubeContainer.innerHTML = await response.text();

            // Bind the loadVideo function to the button
            const loadButton = document.getElementById('load-video-button');
            loadButton.addEventListener('click', loadVideo);

            initializeYouTubePlayer();
        } else {
            console.error('Failed to load YouTube iframe HTML');
        }
    } catch (error) {
        console.error('Error loading YouTube iframe HTML:', error);
    }
}

// Initialize the YouTube player
export function initializeYouTubePlayer() {
    player = new YT.Player('player', {
        events: {
            'onStateChange': onPlayerStateChange // Monitor player state changes
        }
    });
}

function onPlayerStateChange(event) {
    // Only process if it's a user action (not triggered by our code)
    if (!ignorePlayerEvents) {
        if (event.data === YT.PlayerState.PLAYING) {
            sendSyncMessage('play', player.getCurrentTime());
        } else if (event.data === YT.PlayerState.PAUSED) {
            sendSyncMessage('pause', player.getCurrentTime());
        }
    }
}

export function synchronizeVideo(syncState) {
    console.log('synchronizeVideo', syncState);
    // Get current player state and time
    const currentTime = player.getCurrentTime();
    const currentPlayerState = player.getPlayerState();

    // Determine if the player is currently playing or paused
    const isCurrentlyPlaying = currentPlayerState === YT.PlayerState.PLAYING;
    const isCurrentlyPaused = currentPlayerState === YT.PlayerState.PAUSED;

    const stateMatches = (syncState.action === "play" && isCurrentlyPlaying)
        || (syncState.action === "pause" && isCurrentlyPaused);

    const timeMatches = Math.abs(currentTime - syncState.time) <= 5;

    if (stateMatches && timeMatches) {

    } else {
        ignorePlayerEvents = true;
        changePlayerState(syncState);
        setTimeout(() => {
            ignorePlayerEvents = false;
        }, 200);
    }
}

function changePlayerState(syncState) {
    console.log(`Seeking to ${syncState.time} and ${syncState.action}`);
    player.seekTo(syncState.time, true);
    if (syncState.action === "play") {
        player.playVideo();
    } else if (syncState.action === "pause") {
        player.pauseVideo();
    }
}

export function loadVideo() {
    let videoUrl = document.getElementById('video-url').value;
    let videoId = extractVideoId(videoUrl);

    if (videoId) {
        player.loadVideoById(videoId);
    } else {
        alert('Invalid YouTube URL');
    }
    sendSourceUrlSyncMessage(videoId);
}

export function loadVideoById(videoId) {
    player.loadVideoById(videoId);
}
