import {sendSourceUrlSyncMessage, sendSyncMessage} from "./web-socket.js";
import {extractVideoId} from "./videoUtils.js";

let player = null;
let currentVideoId = null;
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
    const currentPlayerTime = player.getCurrentTime();
    const currentPlayerState = player.getPlayerState();

    const isCurrentlyPlaying = currentPlayerState === YT.PlayerState.PLAYING;
    const isCurrentlyPaused = currentPlayerState === YT.PlayerState.PAUSED;

    const playerStateMatches = (syncState.action === "play" && isCurrentlyPlaying)
        || (syncState.action === "pause" && isCurrentlyPaused);

    const playerTimeMatches = Math.abs(currentPlayerTime - syncState.time) <= 5;

    if (playerStateMatches && playerTimeMatches) {
        // player is already on needed state. do nothing
    } else {
        // we use this flag to ignore events from iframe, after external synchronization is proceeded
        ignorePlayerEvents = true;

        changePlayerState(syncState);

        // after changing player state once, several events are generated in iframe.
        // last of events can occur after 0.5 - 0.7 seconds approximately. that is why 1 second timeout is needed
        setTimeout(() => {
            ignorePlayerEvents = false;
        }, 3000);
    }
}

function changePlayerState(syncState) {
    console.log("changePlayerState ", syncState);
    if (syncState.videoId !== currentVideoId) {
        loadVideoById(syncState.videoId);
    }

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
    currentVideoId = videoId;
    player.loadVideoById(videoId);
}
