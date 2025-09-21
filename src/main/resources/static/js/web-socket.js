import {loadVideoById, synchronizeVideo} from "./youtube-player.js";

let stompClient = null;
let socket = null;
let chatCallback = null;

const clientId = Math.random().toString(36).substring(2, 15);

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

export function sendSyncMessage(action, currentTime) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/videoSync/" + currentChannelId, {}, JSON.stringify({
            'action': action,
            'time': currentTime,
            'clientId': clientId
        }));
    }
}

export function sendSourceUrlSyncMessage(videoId) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/syncSource/" + currentChannelId, {}, JSON.stringify({
            'videoId': videoId,
            'clientId': clientId
        }));
    }
}

function handleSyncMessage(message) {
    // Always ignore messages from ourselves
    if (message.clientId === clientId) {
        return;
    }
    synchronizeVideo(message);
}

function handleSourceUrlMessage(syncSourceUrlMessage) {
    // Ignore messages from the same client
    if (syncSourceUrlMessage.clientId === clientId) {
        return;
    }
    loadVideoById(syncSourceUrlMessage.videoId);
}

export function subscribeToChat(onMessageReceived) {
    chatCallback = onMessageReceived;

    if (stompClient?.connected) {
            stompClient.subscribe('/topic/chat.sendMessage/' + currentChannelId, chatCallback);
    }
}

export function sendChatMessage(chatMessage) {
    if (stompClient?.connected && currentChannelId) {
        stompClient.send('/app/chat.sendMessage/' + currentChannelId, {}, JSON.stringify(chatMessage));
    }
}

export { stompClient };