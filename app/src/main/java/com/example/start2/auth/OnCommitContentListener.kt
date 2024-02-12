package com.example.start2.auth

interface OnCommitContentListener {
    fun onCommitContent(contentUri: String, description: String, linkUri: String)
}