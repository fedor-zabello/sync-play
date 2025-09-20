import {connect} from "./web-socket.js";
import {loadPlayer} from "./youtube-player.js";
import {initializeChat} from "./chat.js";
import {createChannelOnBackend, deleteChannel, fetchChannelDetails, fetchChannels, renameChannel, inviteMembers} from "./channel-api.js";

document.addEventListener('DOMContentLoaded', async () => {
    await retrieveChannels()
});

let selectedChannel = null;

async function retrieveChannels() {
    fetchChannels()
        .then(initializeChannelsList)
}

function initializeChannelsList(channels) {
    const channelsList = document.getElementById('channels-list');
    channelsList.innerHTML = '';

    channels.forEach(channel => addChannelToList(channel));
}

// Add a channel to the list and handle its selection
function addChannelToList(channel) {
    const channelsList = document.getElementById('channels-list');
    const channelItem = document.createElement('a');
    channelItem.className = 'list-group-item';
    channelItem.textContent = channel.name;
    channelItem.href = '#';
    channelItem.channelId = channel.id;

    channelItem.onclick = (event) => {
        event.preventDefault();

        if (selectedChannel) {
            selectedChannel.classList.remove('active');
        }

        channelItem.classList.add('active');
        selectedChannel = channelItem; // Update the selected channel

        showChannelHeader()
        loadChannelData(channel.id)
        loadPlayer()
        connect(channel.id)
    };

    channelsList.appendChild(channelItem);
}

function showChannelHeader() {
    const headerContainer = document.getElementById("channel-header-container");
    headerContainer.classList.replace("d-none", "d-flex")
    document.getElementById('channel-header').textContent = selectedChannel.textContent;
}

function hideChannelHeader() {
    const headerContainer = document.getElementById("channel-header-container");
    headerContainer.classList.replace("d-flex", "d-none")
}

async function loadChannelData(channelId) {
    const subscriberCountElement = document.getElementById('subscriber-count'); // Get the span element
    const youtubeContainer = document.getElementById('youtube-container');

    try {
        const response = await fetch('/youtube-iframe');
        if (response.ok) {
            youtubeContainer.innerHTML = await response.text();

            const loadButton = document.getElementById('load-video-button');
            loadButton.addEventListener('click', loadVideo);
            connect(channelId);
        } else {
            console.error('❌ Ошибка загрузки YouTube iframe');
        }
    } catch (error) {
        console.error('❌ Ошибка загрузки YouTube iframe:', error);
    }

    await loadChat(channelId);

    fetchChannelDetails(channelId).then(channelData => {
        const subscriberCount = channelData.subscribersCount;
        subscriberCountElement.textContent = `${subscriberCount} subscribers`; // Update the text content
    });
}

async function loadChat(channelId) {
    console.log("❓ Значение channelId в loadChat:", channelId);

    const chatContainer = document.getElementById('chat-container');
    try {
        const response = await fetch('/chat-fragment');
        if (response.ok) {
            chatContainer.innerHTML = await response.text();
            console.log("✅ Чат загружен, вызываем initializeChat()");
            setTimeout(() => initializeChat(channelId), 100);
        } else {
            console.error('❌ Ошибка загрузки chat-fragment');
        }
    } catch (error) {
        console.error('❌ Ошибка загрузки чата:', error);
    }
}


async function createChannel(channelName) {
    createChannelOnBackend(channelName).then(newChannel => {
        addChannelToList(newChannel);

        const channelsList = document.getElementById('channels-list');
        const channelItems = channelsList.getElementsByClassName('list-group-item');
        const newChannelItem = Array.from(channelItems).find(item => item.textContent === newChannel.name);

        if (newChannelItem) {
            newChannelItem.click(); // Trigger the click handler to activate the channel
        }
    });
}

// Event listener for "Create New Channel" button
document.getElementById('create-channel-button').addEventListener('click', async () => {
    const channelName = prompt('Enter a name for the new channel:');
    if (channelName) {
        await createChannel(channelName);
    }
});

document.getElementById('delete-channel-button').addEventListener('click', async () => {
    deleteChannel(selectedChannel.channelId)
        .then(retrieveChannels)
        .then(hideChannelHeader)
        .then(() => {
            const youtubeContainer = document.getElementById('youtube-container');
            youtubeContainer.innerHTML = '';
        });
})

document.getElementById('change-channel-name-form').addEventListener('submit', async (event) => {
    event.preventDefault();
    const newName = document.getElementById('channel-name').value.trim();
    
    if (!newName) {
        alert('Please enter a channel name');
        return;
    }

    try {
        await renameChannel(selectedChannel.channelId, newName);
        // Update UI
        selectedChannel.textContent = newName;
        document.getElementById('channel-header').textContent = newName;
        // Close modal
        bootstrap.Modal.getInstance(document.getElementById('edit-channel-modal')).hide();
        // Clear input
        document.getElementById('channel-name').value = '';
    } catch (error) {
        alert('Failed to rename channel: ' + error.message);
    }
});

document.getElementById('invite-members-form').addEventListener('submit', async (event) => {
    event.preventDefault();
    const usernames = document.getElementById('invite-members').value.trim();
    
    if (!usernames) {
        alert('Please enter usernames');
        return;
    }

    try {
        await inviteMembers(selectedChannel.channelId, usernames);

        bootstrap.Modal.getInstance(document.getElementById('edit-channel-modal')).hide();

        document.getElementById('invite-members').value = '';

        alert('Invitations sent successfully!');

        await loadChannelData(selectedChannel.channelId);
    } catch (error) {
        alert('Failed to send invitations: ' + error.message);
    }
});
