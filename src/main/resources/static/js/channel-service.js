import {connect} from "./web-socket.js";
import {initializeYouTubePlayer, loadVideo} from "./youtube-player.js";

document.addEventListener('DOMContentLoaded', async () => {
    await retrieveChannels()
});

let selectedChannel = null;

async function retrieveChannels() {
    const response = await fetch('/api/v1/channels', {
        headers: {'Authorization': `Bearer ${localStorage.getItem('authToken')}`}
    });

    if (response.ok) {
        const channels = await response.json();
        initializeChannelsList(channels);
    } else {
        console.error('Failed to fetch channels');
    }
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

        // Remove 'active' class from the previously selected channel
        if (selectedChannel) {
            selectedChannel.classList.remove('active');
        }

        channelItem.classList.add('active');
        selectedChannel = channelItem; // Update the selected channel

        loadChannelData(channel.id);
    };

    channelsList.appendChild(channelItem);
}

// Function to handle channel click
async function loadChannelData(channelId) {
    const youtubeContainer = document.getElementById('youtube-container');

    try {
        // Fetch the HTML content
        const response = await fetch('/youtube-iframe');
        if (response.ok) {

            const headerContainer = document.getElementById("channel-header-container");
            headerContainer.classList.replace("d-none", "d-flex")
            document.getElementById('channel-header').textContent = selectedChannel.textContent;

            youtubeContainer.innerHTML = await response.text();

            // Bind the loadVideo function to the button
            const loadButton = document.getElementById('load-video-button');
            loadButton.addEventListener('click', loadVideo);

            initializeYouTubePlayer();

            connect(channelId);
        } else {
            console.error('Failed to load YouTube iframe HTML');
        }
    } catch (error) {
        console.error('Error loading YouTube iframe HTML:', error);
    }
}

// Function to create a new channel
async function createChannel(channelName) {
    try {
        const response = await fetch('/api/v1/channels', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-Token': getCsrfToken()
            },
            body: JSON.stringify({ name: channelName })
        });

        if (response.ok) {
            const newChannel = await response.json();
            console.log('Channel created successfully:', newChannel);

            addChannelToList(newChannel);

            const channelsList = document.getElementById('channels-list');
            const channelItems = channelsList.getElementsByClassName('list-group-item');
            const newChannelItem = Array.from(channelItems).find(item => item.textContent === newChannel.name);

            if (newChannelItem) {
                newChannelItem.click(); // Trigger the click handler to activate the channel
            }
        } else {
            console.error('Error creating channel:', response.status, response.statusText);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Event listener for "Create New Channel" button
document.getElementById('create-channel-button').addEventListener('click', async () => {
    const channelName = prompt('Enter a name for the new channel:');
    if (channelName) {
        await createChannel(channelName);
    }
});

document.getElementById('delete-channel-button').addEventListener('click', async () => {
    try {
        const channelId = selectedChannel.channelId;
        const url = `/api/v1/channels/${channelId}`;
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-Token': getCsrfToken()
            }
        });

        if (response.ok) {
            await retrieveChannels()
        }
    } catch (error) {
        console.error('Error:', error);
    }
})

// Helper function to get CSRF token if needed
function getCsrfToken() {
    const csrfElement = document.querySelector('input[name="_csrf"]');
    return csrfElement ? csrfElement.value : '';
}
