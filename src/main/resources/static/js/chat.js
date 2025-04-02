export function initializeChat() {
    console.log("🔥 initializeChat() вызвана!");

    const chatPage = document.querySelector('#chat-page');
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
        const channelId = selectedChannel ? selectedChannel.channelId : null; // Получаем channelId
        sendMessage(channelId); // Передаем channelId
    });

    function sendMessage(channelId) { // Принимаем channelId как параметр
        console.log("📩 Отправка сообщения...");

        const messageContent = messageInput.value.trim();
        if (messageContent) {
            if (stompClient && stompClient.connected && channelId) { // Проверяем channelId
                const chatMessage = {
                    channelId: channelId,
                    sender: username,
                    content: messageContent,
                    type: "CHAT"
                };
                console.log("📤 Отправляем сообщение:", chatMessage);
                stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
                messageInput.value = '';
            } else {
                console.error("❌ WebSocket не подключен или channelId не определен. Попытка переподключения...");
                if (channelId) {
                    connect(channelId);
                    setTimeout(() => sendMessage(channelId), 1000); // Повторная попытка через 1 секунду
                } else {
                    console.error("❌ channelId не определен.");
                }
            }
        } else {
            console.error("❌ Сообщение пустое.");
        }
    }
}



let stompClient = null;
let username = null;
let selectedChannel = null; // Добавляем переменную для хранения выбранного канала

// Обновляем функцию connect для передачи channelId
function connect(channelId) {
    if (!username) {
        console.error('❌ Имя пользователя не загружено.');
        return;
    }
    if (!channelId) {
        console.error('❌ channelId не загружен.');
        return;
    }

    if (stompClient && stompClient.connected) {
        console.log("⚡ WebSocket уже подключен.");
        return;
    }

    console.log("🌐 Подключаем WebSocket...");
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
        console.log("✅ WebSocket подключен.");
        stompClient.subscribe('/topic/public', onMessageReceived);
    }, (error) => {
        console.error("❌ Ошибка WebSocket:", error);
        setTimeout(() => connect(channelId), 5000); // Повторная попытка через 5 секунд
    });
}

// 📩 Обрабатываем входящие сообщения
function onMessageReceived(payload) {
    console.log("📨 Получено сообщение через WebSocket:", payload.body);
    const message = JSON.parse(payload.body);

    if (message.channelId !== channelId) {
        console.log("📨 Сообщение предназначено для другого канала.");
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

    const messageArea = document.getElementById('messageArea');
        messageArea.prepend(messageElement);
    }

// 🎨 Функция для генерации случайного цвета аватара
function getAvatarColor(messageSender) {
    const colors = ['#2196F3', '#32c787', '#00BCD4', '#ff5652', '#ffc107', '#ff85af', '#FF9800', '#39bbb0'];
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    return colors[Math.abs(hash % colors.length)];
}

// 🔄 Загружаем пользователя при загрузке страницы и подключаем WebSocket
// 🔄 Загружаем пользователя при загрузке страницы
document.addEventListener("DOMContentLoaded", () => {
    fetch('/api/v1/users/me', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
    })
    .then(response => response.ok ? response.json() : Promise.reject("Failed to fetch user"))
    .then(user => {
        username = user.name;
        console.log(`👤 Имя пользователя: ${username}`);
        // Не подключаем WebSocket здесь, дожидаемся выбора канала
    })
    .catch(error => console.error("❌ Ошибка получения пользователя:", error));
});

