package com.example.ytvideos.dataclasses

data class YoutubeResponse(val items: List<YoutubeVideoItem>)

data class YoutubeVideoItem(
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(
    val videoId: String?
)

data class Snippet(
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
)

data class Thumbnails(
    val medium: ThumbnailDetails
)

data class ThumbnailDetails(
    val url: String
)
