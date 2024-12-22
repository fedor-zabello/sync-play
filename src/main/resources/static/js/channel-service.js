import {connect} from "./web-socket.js";
import {loadPlayer} from "./youtube-player.js";
import {createChannelOnBackend, deleteChannel, fetchChannelDetails, fetchChannels} from "./channel-api.js";

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

    fetchChannelDetails(channelId).then(channelData => {
        const subscriberCount = channelData.subscribersCount;
        subscriberCountElement.textContent = `${subscriberCount} subscribers`; // Update the text content
    });
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
