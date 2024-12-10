import {loadVideoById, synchronizeVideo} from "./iframe-service.js";

let stompClient = null;
let socket = null;

let lock = false; // Flag to prevent sending sync messages during a sync event
const clientId = Math.random().toString(36).substring(2, 15); // Unique client ID

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
export function sendSyncMessage(action, currentTime) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/videoSync/" + currentChannelId, {}, JSON.stringify({
            'action': action,
            'time': currentTime,
            'clientId': clientId
        }));
    }
}

// Send a syncSourceUrlOutput message with the video url
export function sendSourceUrlSyncMessage(videoId) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/syncSource/" + currentChannelId, {}, JSON.stringify({
            'videoId': videoId
        }));
    }
}

// Handle sync messages received from the server
function handleSyncMessage(message) {
    if (lock) {
        // Skip sending message if lock is true (i.e., during sync)
        console.log("Skipping message due to lock");
        return;
    }

    if (message.clientId === clientId) {
        // Ignore messages from the same client
        return;
    }

    // Set lock to true to prevent sending more sync messages
    lock = true;
    setTimeout(() => {
        lock = false; // Reset lock after 1s
    }, 1000);

    synchronizeVideo(message);
}

// Handle syncSourceUrl  messages received from the server
function handleSourceUrlMessage(syncSourceUrlMessage) {
    loadVideoById(syncSourceUrlMessage.videoId)
}
