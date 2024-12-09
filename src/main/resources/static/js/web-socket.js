import { extractVideoId } from './videoUtils.js';

let stompClient = null;
let socket = null;

let player;
let lock = false; // Flag to prevent sending sync messages during a sync event
const clientId = Math.random().toString(36).substring(2, 15); // Unique client ID

let isYouTubeAPIReady = false; // Track YouTube API readiness

let currentChannelId = null;

// Connect to the WebSocket server
export function connect(channelId) {
    // Close the current connection if it exists
    if (stompClient !== null) {
        console.log('Disconnecting from previous channel...');
        stompClient.disconnect(() => {
            console.log('Disconnected from previous channel.');
        });
        stompClient = null;
    }

    if (socket !== null) {
        socket.close();
        socket = null;
    }

    currentChannelId = channelId;

    socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/videoSync/' + currentChannelId, function (messageOutput) {
            let message = JSON.parse(messageOutput.body);
            handleSyncMessage(message);
        });
        stompClient.subscribe('/topic/syncSource/' + currentChannelId, function (syncSourceUrlOutput) {
            let syncSourceUrlMessage = JSON.parse(syncSourceUrlOutput.body);
            handleSourceUrlMessage(syncSourceUrlMessage);
        });
    });
}

// Send a sync message with the current video state and clientId
function sendSyncMessage(action, currentTime) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/videoSync/" + currentChannelId, {}, JSON.stringify({
            'action': action,
            'time': currentTime,
            'clientId': clientId
        }));
    }
}

// Send a syncSourceUrlOutput message with the video url
function sendSourceUrlSyncMessage(videoId) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/syncSource/" + currentChannelId, {}, JSON.stringify({
            'videoId': videoId
        }));
    }
}

// Handle sync messages received from the server
function handleSyncMessage(message) {
    if (message.clientId === clientId) {
        // Ignore messages from the same client
        return;
    }

    // Set lock to true to prevent sending more sync messages
    lock = true;
    setTimeout(() => {
        lock = false; // Reset lock after 1s
    }, 1000);

    // Handle 'play' and 'pause' actions
    if (message.action === 'play') {
        player.seekTo(message.time, true); // Synchronize time
        player.playVideo();
    } else if (message.action === 'pause') {
        player.seekTo(message.time, true); // Synchronize time
        player.pauseVideo();               // Pause the video
    }
}

// Handle syncSourceUrl  messages received from the server
function handleSourceUrlMessage(syncSourceUrlMessage) {
    // Handle 'syncSourceUrlMessage'
        player.loadVideoById(syncSourceUrlMessage.videoId);
}

// YouTube player state change handler
function onPlayerStateChange(event) {
    if (lock) {
        // Skip sending message if lock is true (i.e., during sync)
        console.log("Skipping message due to lock");
        return;
    }

    // Send sync message if the state change is triggered by the user
    if (event.data === YT.PlayerState.PLAYING) {
        sendSyncMessage('play', player.getCurrentTime());
    } else if (event.data === YT.PlayerState.PAUSED) {
        sendSyncMessage('pause', player.getCurrentTime());
    }
}

// // Initialize the YouTube iframe player and hook into state change events
// function onYouTubeIframeAPIReady() {
//     isYouTubeAPIReady = true;
//
//     // Ensure player initialization only happens when iframe is in the DOM
//     const playerElement = document.getElementById('player');
//     if (playerElement) {
//         initializeYouTubePlayer();
//     }
// }

// Initialize the YouTube player
export function initializeYouTubePlayer() {
    if (!isYouTubeAPIReady) {
        console.error('YouTube API is not ready');
        return;
    }

    player = new YT.Player('player', {
        events: {
            'onStateChange': onPlayerStateChange // Monitor player state changes
        }
    });
}

// Load the YouTube IFrame API dynamically
export function loadYouTubeAPI() {
    if (isYouTubeAPIReady) return; // Avoid reloading API if already loaded

    const scriptTag = document.createElement('script');
    scriptTag.src = "https://www.youtube.com/iframe_api";
    const firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(scriptTag, firstScriptTag);

    // Wait for the API to be ready by listening for the window.YT object
    scriptTag.onload = () => {
        isYouTubeAPIReady = true;
        if (typeof YT !== 'undefined' && YT.Player) {
            initializeYouTubePlayer(); // Call your initialization function after the API is loaded
        } else {
            console.error('YouTube API failed to load');
        }
    };
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
