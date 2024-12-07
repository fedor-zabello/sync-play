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
            channelItem.href = `#`; // Or a dynamic link to channel content
            channelItem.onclick = () => loadChannelContent(channel.id); // Example event handler
            channelsList.appendChild(channelItem);
        });
    } else {
        console.error('Failed to fetch channels');
    }
});

function loadChannelContent(channelId) {
    console.log(`Load content for channel ID: ${channelId}`);
    // Add logic to dynamically load channel-specific content
}
