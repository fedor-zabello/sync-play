import { subscribeToChat, sendChatMessage, stompClient } from "./web-socket.js";

let username = null;

export function initializeChat(channelId) {
    console.log("🔥 initializeChat() called!");

    const messageForm = document.getElementById('messageForm');
    const messageInput = document.getElementById('message');
    const messageArea = document.getElementById('messageArea');

    if (!messageForm) {
        console.error("❌ Message sending form NOT found in DOM!");
        return;
    }

    console.log("✅ Form found, adding event handler...");

    messageForm.addEventListener("submit", (event) => {
        event.preventDefault();
        sendMessage();
    });

    subscribeToChat(onMessageReceived);

    function sendMessage() {
        console.log("📩 Sending message...");
        const messageContent = messageInput.value.trim();
        if (messageContent) {
            if (stompClient && stompClient.connected) {
                const chatMessage = {
                    sender: username,
                    content: messageContent,
                    type: "CHAT"
                };
                console.log("📤 Sending message:", chatMessage);
                sendChatMessage(chatMessage);
                messageInput.value = '';
            } else {
                console.error("❌ WebSocket is not connected or stompClient does not exist");
            }
        } else {
            console.error("❌ Message is empty");
        }
    }

    function scrollToBottom() {
        const messageArea = document.getElementById('messageArea');
        if (messageArea) {
            messageArea.scrollTop = messageArea.scrollHeight;
        }
    }

    function debounce(func, wait = 100) {
        let timeout;
        return function(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), wait);
        };
    }

    const debouncedScrollToBottom = debounce(scrollToBottom, 100);

    function onMessageReceived(payload) {
        console.log("📨 Message received through WebSocket:", payload.body);
        const message = JSON.parse(payload.body);

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

        messageArea.appendChild(messageElement);
        debouncedScrollToBottom();
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
        console.log(`👤 Username: ${username}`);
    })
    .catch(error => console.error("❌ Error fetching user:", error));
});
