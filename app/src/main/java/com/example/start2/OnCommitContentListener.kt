package com.example.start2

interface OnCommitContentListener {
    fun onCommitContent(contentUri: String, description: String, linkUri: String)
}