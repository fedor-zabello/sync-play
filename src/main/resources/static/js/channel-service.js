import {connect} from "./web-socket.js";
import {initializeYouTubePlayer, loadVideo} from "./iframe-service.js";

document.addEventListener('DOMContentLoaded', async () => {
    const response = await fetch('/api/v1/channels', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
    });

    if (response.ok) {
        const channels = await response.json();

        const channelsList = document.getElementById('channels-list');
        channelsList.innerHTML = ''; // Clear existing items

        let selectedChannel = null; // Keep track of the selected channel

        channels.forEach(channel => {
            const channelItem = document.createElement('a');
            channelItem.className = 'list-group-item list-group-item-action bg-dark text-light';
            channelItem.textContent = channel.name;
            channelItem.href = '#';

            channelItem.onclick = (event) => {
                event.preventDefault();

                // Remove 'active' class from the previously selected channel
                if (selectedChannel) {
                    selectedChannel.classList.remove('active');
                }

                // Add 'active' class to the current channel
                channelItem.classList.add('active');
                selectedChannel = channelItem; // Update the selected channel

                loadChannelData(channel.id);
            };

            channelsList.appendChild(channelItem);
        });
    } else {
        console.error('Failed to fetch channels');
    }
});

// Function to handle channel click
async function loadChannelData(channelId) {

    const youtubeContainer = document.getElementById('youtube-container');

    try {
        // Fetch the HTML content
        const response = await fetch('/youtube-iframe'); // Adjust the path
        if (response.ok) {
            youtubeContainer.innerHTML = await response.text();

            // Bind the loadVideo function to the button
            const loadButton = document.getElementById('load-video-button');
            loadButton.addEventListener('click', loadVideo);

            // Ensure player is initialized after iframe is added
            initializeYouTubePlayer();

            // Connect WebSocket (if not already connected)
            connect(channelId);
        } else {
            console.error('Failed to load YouTube iframe HTML');
        }
    } catch (error) {
        console.error('Error loading YouTube iframe HTML:', error);
    }
}
