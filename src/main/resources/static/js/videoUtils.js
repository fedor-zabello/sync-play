function extractVideoId(url) {
    let videoId = null;
    const urlPatterns = [
        /(?:https?:\/\/)?(?:www\.)?youtu\.be\/([a-zA-Z0-9_-]+)/, // youtu.be/<video_id>
        /(?:https?:\/\/)?(?:www\.)?youtube\.com\/watch\?v=([a-zA-Z0-9_-]+)/, // youtube.com/watch?v=<video_id>
        /(?:https?:\/\/)?(?:www\.)?youtube\.com\/embed\/([a-zA-Z0-9_-]+)/ // youtube.com/embed/<video_id>
    ];

    for (let pattern of urlPatterns) {
        const match = url.match(pattern);
        if (match && match[1]) {
            videoId = match[1];
            break;
        }
    }

    return videoId;
}

export { extractVideoId };