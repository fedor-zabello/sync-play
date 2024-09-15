let stompClient = null;
let player;
let lock = false; // Flag to prevent sending sync messages during a sync event
const clientId = Math.random().toString(36).substring(2, 15); // Unique client ID

// Connect to the WebSocket server
function connect() {
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/videoSync', function (messageOutput) {
            let message = JSON.parse(messageOutput.body);
            handleSyncMessage(message);
        });
    });
}

// Send a sync message with the current video state and clientId
function sendSyncMessage(action, currentTime) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/syncVideo", {}, JSON.stringify({
            'action': action,
            'time': currentTime,
            'clientId': clientId
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
        lock = false; // Reset lock after 100ms
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

// Initialize the YouTube iframe player and hook into state change events
function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
        events: {
            'onStateChange': onPlayerStateChange // Monitor player state changes
        }
    });
}

// Load the YouTube IFrame API dynamically
function loadYouTubeAPI() {
    var tag = document.createElement('script');
    tag.src = "https://www.youtube.com/iframe_api";
    var firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
}

// Connect to WebSocket and load YouTube API on page load
window.onload = function () {
    connect();
    loadYouTubeAPI();
};
