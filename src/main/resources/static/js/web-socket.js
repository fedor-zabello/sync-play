import {loadVideoById, synchronizeVideo} from "./youtube-player.js";

let stompClient = null;
let socket = null;

// Generate a unique client ID
const clientId = Math.random().toString(36).substring(2, 15);

// Keep track of message processing
let processingMessage = false;

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

// Send a sync message with the current video state
export function sendSyncMessage(action, currentTime) {
    if (stompClient && stompClient.connected && !processingMessage) {
        stompClient.send("/app/videoSync/" + currentChannelId, {}, JSON.stringify({
            'action': action,
            'time': currentTime,
            'clientId': clientId
        }));
        console.log(`Sent ${action} message at time ${currentTime}`);
    }
}

// Send a syncSourceUrlOutput message with the video url
export function sendSourceUrlSyncMessage(videoId) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/syncSource/" + currentChannelId, {}, JSON.stringify({
            'videoId': videoId,
            'clientId': clientId
        }));
    }
}

// Handle sync messages received from the server
function handleSyncMessage(message) {
    // Always ignore messages from ourselves
    if (message.clientId === clientId) {
        console.log("Ignoring own message");
        return;
    }

    console.log(`Received ${message.action} message from ${message.clientId} at time ${message.time}`);

    // Set the processing flag to prevent sending a response
    processingMessage = true;

    // Make sure the player properly responds to the message
    synchronizeVideo(message);

    // Reset the processing flag after a short delay
    setTimeout(() => {
        processingMessage = false;
    }, 2000); // 2-second delay before allowing new messages to be sent
}

// Handle syncSourceUrl messages received from the server
function handleSourceUrlMessage(syncSourceUrlMessage) {
    // Ignore messages from the same client
    if (syncSourceUrlMessage.clientId === clientId) {
        return;
    }

    loadVideoById(syncSourceUrlMessage.videoId);
}