package com.example.largefiles

class FileInfo {
    var width: Int
    var height: Int
    var size: Long
    var duration: Long = 0
    var bitrate: Long = 0
    var fileType: Int

    constructor(width: Int, height: Int, size: Long, duration: Long, bitrate: Long, fileType: Int) {
        this.width = width
        this.height = height
        this.size = size
        this.duration = duration
        this.fileType = fileType
        this.bitrate = bitrate
    }

    constructor(width: Int, height: Int, size: Long, fileType: Int) {
        this.width = width
        this.height = height
        this.size = size
        this.fileType = fileType
    }

    override fun equals(obj: Any?): Boolean {
        val fileInfo = obj as FileInfo?
        return fileInfo!!.width == width && fileInfo.height == height && fileInfo.size == size && fileInfo.bitrate == bitrate && fileInfo.duration == duration
    }

    override fun hashCode(): Int {
        return (37 * size + (width + height xor 21) + (bitrate + duration) xor 13).toInt()
    }

    override fun toString(): String {
        return "width= " + width + " height= " + height + " bitrate= " + bitrate + " duration= " + duration + "size= " + size
    }
}
