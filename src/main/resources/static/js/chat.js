// chat.js
import { subscribeToChat, sendChatMessage, stompClient } from "./web-socket.js";

let username = null;

export function initializeChat(channelId) {
    console.log("🔥 initializeChat() вызвана!");

    const messageForm = document.getElementById('messageForm');
    const messageInput = document.getElementById('message');
    const messageArea = document.getElementById('messageArea');

    if (!messageForm) {
        console.error("❌ Форма отправки сообщений НЕ найдена в DOM!");
        return;
    }

    console.log("✅ Форма найдена, добавляем обработчик...");

    messageForm.addEventListener("submit", (event) => {
        event.preventDefault();
        sendMessage(channelId);
    });

    subscribeToChat(channelId, onMessageReceived);

    function sendMessage(channelId) {
        console.log("📩 Отправка сообщения...");
        console.log("❓ Значение channelId:", channelId);  // Добавьте это логирование

        const messageContent = messageInput.value.trim();
        if (messageContent) {
            console.log("✅ Проверяем состояние stompClient:", stompClient);
            console.log("✅ Проверяем подключение:", stompClient && stompClient.connected);

            if (stompClient && stompClient.connected && channelId) {
                const chatMessage = {
                    sender: username,
                    content: messageContent,
                    type: "CHAT"
                };
                console.log("📤 Отправляем сообщение:", chatMessage);
                sendChatMessage(channelId, chatMessage);
                messageInput.value = '';
            } else {
                console.error("❌ WebSocket не подключен или channelId не определен.");
                if (!stompClient) {
                    console.error("❌ stompClient не существует!");
                } else if (!stompClient.connected) {
                    console.error("❌ stompClient не подключен!");
                }
                if (!channelId) {
                    console.error("❌ channelId не определен!");
                }
            }
        } else {
            console.error("❌ Сообщение пустое.");
        }
    }


    function onMessageReceived(payload) {
        console.log("📨 Получено сообщение через WebSocket:", payload.body);
        const message = JSON.parse(payload.body);

        if (message.channelId !== channelId) {
            console.log("📨 Сообщение предназначено для другого канала. channelId:", channelId);
            return;
        }

        const messageElement = document.createElement('li');
        messageElement.classList.add('chat-message');

        if (message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            messageElement.textContent = `${message.sender} joined!`;
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            messageElement.textContent = `${message.sender} left!`;
        } else {
            const header = document.createElement('div');
            header.className = 'header';

            const avatarElement = document.createElement('i');
            avatarElement.textContent = message.sender[0];
            avatarElement.style['background-color'] = getAvatarColor(message.sender);

            const usernameElement = document.createElement('span');
            usernameElement.textContent = message.sender;

            header.appendChild(avatarElement);
            header.appendChild(usernameElement);

            const textElement = document.createElement('p');
            textElement.textContent = message.content;

            messageElement.appendChild(header);
            messageElement.appendChild(textElement);
        }

        messageArea.prepend(messageElement);
    }
}

function getAvatarColor(messageSender) {
    const colors = ['#2196F3', '#32c787', '#00BCD4', '#ff5652', '#ffc107', '#ff85af', '#FF9800', '#39bbb0'];
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    return colors[Math.abs(hash % colors.length)];
}

document.addEventListener("DOMContentLoaded", () => {
    fetch('/api/v1/users/me', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
    })
    .then(response => response.ok ? response.json() : Promise.reject("Failed to fetch user"))
    .then(user => {
        username = user.name;
        console.log(`👤 Имя пользователя: ${username}`);
    })
    .catch(error => console.error("❌ Ошибка получения пользователя:", error));
});
