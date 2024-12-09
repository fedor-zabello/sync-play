import {connect, initializeYouTubePlayer, loadYouTubeAPI} from "./web-socket.js";

document.addEventListener('DOMContentLoaded', async () => {
    const response = await fetch('/api/v1/channels', {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` }
    });

    if (response.ok) {
        const channels = await response.json();

        const channelsList = document.getElementById('channels-list');
        channelsList.innerHTML = ''; // Clear existing items

        channels.forEach(channel => {
            const channelItem = document.createElement('a');
            channelItem.className = 'list-group-item list-group-item-action bg-dark text-light';
            channelItem.textContent = channel.name;
            channelItem.href = '#';
            channelItem.onclick = (event) => {
                event.preventDefault();
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

            // Update the iframe src dynamically
            const iframe = document.getElementById('player');
            iframe.src = `https://www.youtube.com/embed/?enablejsapi=1`;

            // Load the YouTube API (if not already loaded)
            loadYouTubeAPI();

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
