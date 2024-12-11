import {sendSourceUrlSyncMessage, sendSyncMessage} from "./web-socket.js";
import {extractVideoId} from "./videoUtils.js";

let player = null;

// Initialize the YouTube player
export function initializeYouTubePlayer() {
    player = new YT.Player('player', {
        events: {
            'onStateChange': onPlayerStateChange // Monitor player state changes
        }
    });
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

export function synchronizeVideo(synchronizeMessage) {
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
