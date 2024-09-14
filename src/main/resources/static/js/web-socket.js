let stompClient = null;
let player;
let ignoreEvent = false;  // Flag to prevent resending events triggered by received messages

// Connect to the WebSocket server using SockJS and STOMP
function connect() {
    let socket = new SockJS('/ws');  // Create SockJS connection
    stompClient = Stomp.over(socket);  // Use STOMP protocol over SockJS
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/videoSync', function (messageOutput) {
            let message = JSON.parse(messageOutput.body);
            handleSyncMessage(message);
        });
    });
}

// Send synchronization message (play or pause) via WebSocket
function sendSyncMessage(action, currentTime) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/syncVideo", {}, JSON.stringify({
            'action': action,
            'time': currentTime
        }));
    }
}

// Handle synchronization message received from the server
function handleSyncMessage(message) {
    ignoreEvent = true;  // Set flag to ignore the next player state change event

    if (message.action === 'play') {
        player.seekTo(message.time);   // Move video to the correct time
        player.playVideo();            // Play the video
    } else if (message.action === 'pause') {
        player.seekTo(message.time);   // Move video to the correct time
        player.pauseVideo();           // Pause the video
    }
}

// Handle state change of the YouTube player (play, pause)
function onPlayerStateChange(event) {
    if (ignoreEvent) {
        ignoreEvent = false;  // Reset the flag after ignoring one event
        return;
    }

    // Only send a sync message if the user triggered the event (not a sync message)
    if (event.data === YT.PlayerState.PLAYING) {
        sendSyncMessage('play', player.getCurrentTime());
    } else if (event.data === YT.PlayerState.PAUSED) {
        sendSyncMessage('pause', player.getCurrentTime());
    }
}

// Initialize the YouTube player
function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
        events: {
            'onStateChange': onPlayerStateChange  // Triggered when the video state changes
        }
    });
}

// Dynamically load the YouTube IFrame API script
function loadYouTubeAPI() {
    var tag = document.createElement('script');
    tag.src = "https://www.youtube.com/iframe_api";
    var firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
}

// Establish WebSocket connection and load YouTube API on page load
window.onload = function () {
    connect();
    loadYouTubeAPI();
};
