package com.mezhendosina.sgo.data.requests.other.entities.checkUpdates


import com.google.gson.annotations.SerializedName

data class CheckUpdates(
    @SerializedName("assets")
    val assets: List<Asset>,
    @SerializedName("assets_url")
    val assetsUrl: String,
    @SerializedName("author")
    val author: Author,
    @SerializedName("body")
    val body: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("draft")
    val draft: Boolean,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("node_id")
    val node_id: String,
    @SerializedName("prerelease")
    val prerelease: Boolean,
    @SerializedName("published_at")
    val publishedAt: String,
    @SerializedName("tag_name")
    val tagName: String,
    @SerializedName("tarball_url")
    val tarballUrl: String,
    @SerializedName("target_commitish")
    val targetCommitish: String,
    @SerializedName("upload_url")
    val uploadUrl: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("zipball_url")
    val zipballUrl: String
)