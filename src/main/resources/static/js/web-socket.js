// web-socket.js
import { loadVideoById, synchronizeVideo } from "./youtube-player.js";

let stompClient = null; // Убираем дублирующее объявление ниже
let socket = null;
let subscriptions = {};
let clientId = Math.random().toString(36).substring(2, 15);
let currentChannelId = null;

export function connect(channelId) {
    if (currentChannelId === channelId && stompClient?.connected) {
        console.log(`🔄 Уже подключено к каналу ${channelId}`);
        return;
    }

    // отключаем старое подключение
    if (stompClient) {
        Object.values(subscriptions).forEach(sub => sub.unsubscribe());
        subscriptions = {};
        stompClient.disconnect(() => {
            console.log('🔌 Отключено от предыдущего канала.');
        });
        stompClient = null;
    }

    if (socket) {
        socket.close();
        socket = null;
    }

    currentChannelId = channelId;

    socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        console.log('✅ Подключено: ' + frame);

        // Подписка на видео-синхронизацию
        subscriptions.video = stompClient.subscribe(`/topic/videoSync/${channelId}`, messageOutput => {
            let message = JSON.parse(messageOutput.body);
            handleSyncMessage(message);
        });

        // Подписка на смену видео
        subscriptions.source = stompClient.subscribe(`/topic/syncSource/${channelId}`, syncSourceUrlOutput => {
            let syncSourceUrlMessage = JSON.parse(syncSourceUrlOutput.body);
            handleSourceUrlMessage(syncSourceUrlMessage);
        });

        // Подключение к чату, если callback уже задан
        if (subscriptions.chatCallback) {
            subscribeToChat(channelId, subscriptions.chatCallback);
        }
    });
}


function handleSyncMessage(message) {
    if (message.clientId === clientId) return;

    synchronizeVideo(message);

    setTimeout(() => {
        // allow further sync messages
    }, 2000);
}

function handleSourceUrlMessage(syncSourceUrlMessage) {
    if (syncSourceUrlMessage.clientId === clientId) return;

    loadVideoById(syncSourceUrlMessage.videoId);
}

// ✅ Чат-подписка должна быть здесь
export function subscribeToChat(channelId, onMessageReceived) {
    if (!stompClient || !stompClient.connected) {
        console.warn("⚠️ stompClient еще не подключен. Подписка на чат отложена.");
        subscriptions.chatCallback = onMessageReceived;
        return;
    }

    if (subscriptions.chat) {
        subscriptions.chat.unsubscribe();
    }

    subscriptions.chat = stompClient.subscribe(`/topic/chat.sendMessage/${channelId}`, onMessageReceived);
}

export function sendSyncMessage(action, currentTime) {
    if (stompClient?.connected) {
        stompClient.send(`/app/videoSync/${currentChannelId}`, {}, JSON.stringify({
            action,
            time: currentTime,
            clientId
        }));
    }
}

export function sendSourceUrlSyncMessage(videoId) {
    if (stompClient?.connected && currentChannelId) {
        stompClient.send(`/app/syncSource/${currentChannelId}`, {}, JSON.stringify({
            videoId,
            clientId
        }));
    } else {
        console.warn("⚠️ Не удалось отправить syncSource сообщение — WebSocket не подключен.");
    }
}

export function sendChatMessage(chatMessage) {
    if (stompClient?.connected && currentChannelId) {
        stompClient.send(`/app/chat.sendMessage/${currentChannelId}`, {}, JSON.stringify(chatMessage));
    } else {
        console.warn("⚠️ Не удалось отправить чат-сообщение — WebSocket не подключен.");
    }
}

// Экспортируем stompClient
export { stompClient };
