const BASE_URL = '/api/v1/channels';

function getAuthHeaders() {
    return { 'Authorization': `Bearer ${localStorage.getItem('authToken')}` };
}

export async function fetchChannels() {
    const response = await fetch(BASE_URL, {
        headers: getAuthHeaders(),
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch channels: ${response.statusText}`);
    }
    return await response.json();
}

export async function fetchChannelDetails(channelId) {
    const response = await fetch(`${BASE_URL}/${channelId}`, {
        headers: getAuthHeaders(),
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch channel details: ${response.statusText}`);
    }
    return await response.json();
}

export async function createChannelOnBackend(channelName) {
    const response = await fetch(BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': getCsrfToken()
        },
        body: JSON.stringify({ name: channelName }),
    });
    if (!response.ok) {
        throw new Error(`Failed to create channel: ${response.statusText}`);
    }
    return await response.json();
}

export async function deleteChannel(channelId) {
    const response = await fetch(`${BASE_URL}/${channelId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': getCsrfToken()
        },
    });
    if (!response.ok) {
        throw new Error(`Failed to delete channel: ${response.statusText}`);
    }
}

// Helper function to get CSRF token if needed
function getCsrfToken() {
    const csrfElement = document.querySelector('input[name="_csrf"]');
    return csrfElement ? csrfElement.value : '';
}
