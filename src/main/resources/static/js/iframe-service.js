import {sendSourceUrlSyncMessage, sendSyncMessage} from "./web-socket.js";
import {extractVideoId} from "./videoUtils.js";

let player = null;

export function getPlayer() {
    if (!player) {
        initializeYouTubePlayer()
    }
    return player;
}

// Initialize the YouTube player
export function initializeYouTubePlayer() {
    loadYouTubeAPI();

    player = new YT.Player('player', {
        events: {
            'onStateChange': onPlayerStateChange // Monitor player state changes
        }
    });
}

// Load the YouTube IFrame API dynamically
function loadYouTubeAPI() {
    const scriptTag = document.createElement('script');
    scriptTag.src = "https://www.youtube.com/iframe_api";
    const firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(scriptTag, firstScriptTag);

    // Wait for the API to be ready by listening for the window.YT object
    scriptTag.onload = () => {
        if (typeof YT !== 'undefined' && YT.Player) {

        } else {
            console.error('YouTube API failed to load');
        }
    };
}

// YouTube player state change handler
function onPlayerStateChange(event) {
    // Send sync message if the state change is triggered by the user
    if (event.data === YT.PlayerState.PLAYING) {
        sendSyncMessage('play', player.getCurrentTime());
    } else if (event.data === YT.PlayerState.PAUSED) {
        sendSyncMessage('pause', player.getCurrentTime());
    }
}

// Load video based on the input URL
export function loadVideo() {
    let videoUrl = document.getElementById('video-url').value;

    // Extract video ID from YouTube URL
    let videoId = extractVideoId(videoUrl);
    if (videoId) {
        // Update the iframe src with the new video ID
        player.loadVideoById(videoId);
    } else {
        alert('Invalid YouTube URL');
    }
    sendSourceUrlSyncMessage(videoId);
}

export function synchronizeVideo(synchronizeMessage) {
    // Handle 'play' and 'pause' actions
    if (synchronizeMessage.action === 'play') {
        player.seekTo(synchronizeMessage.time, true);   // Synchronize time
        player.playVideo();
    } else if (synchronizeMessage.action === 'pause') {
        player.seekTo(synchronizeMessage.time, true);   // Synchronize time
        player.pauseVideo();                            // Pause the video
    }
}

export function loadVideoById(videoId) {
    player.loadVideoById(videoId);
}
