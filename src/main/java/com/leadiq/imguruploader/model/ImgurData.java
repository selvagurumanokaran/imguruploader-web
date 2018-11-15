package com.leadiq.imguruploader.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgurData {

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private Object title;
    @JsonProperty("description")
    private Object description;
    @JsonProperty("datetime")
    private int datetime;
    @JsonProperty("type")
    private String type;
    @JsonProperty("animated")
    private boolean animated;
    @JsonProperty("width")
    private int width;
    @JsonProperty("height")
    private int height;
    @JsonProperty("size")
    private int size;
    @JsonProperty("views")
    private int views;
    @JsonProperty("bandwidth")
    private int bandwidth;
    @JsonProperty("vote")
    private Object vote;
    @JsonProperty("favorite")
    private boolean favorite;
    @JsonProperty("nsfw")
    private Object nsfw;
    @JsonProperty("section")
    private Object section;
    @JsonProperty("account_url")
    private Object accountUrl;
    @JsonProperty("account_id")
    private int accountId;
    @JsonProperty("is_ad")
    private boolean isAd;
    @JsonProperty("in_most_viral")
    private boolean inMostViral;
    @JsonProperty("tags")
    private List<Object> tags = null;
    @JsonProperty("ad_type")
    private int adType;
    @JsonProperty("ad_url")
    private String adUrl;
    @JsonProperty("in_gallery")
    private boolean inGallery;
    @JsonProperty("deletehash")
    private String deletehash;
    @JsonProperty("name")
    private String name;
    @JsonProperty("link")
    private String link;

    @JsonProperty("id")
    public String getId() {
	return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
	this.id = id;
    }

    @JsonProperty("title")
    public Object getTitle() {
	return title;
    }

    @JsonProperty("title")
    public void setTitle(Object title) {
	this.title = title;
    }

    @JsonProperty("description")
    public Object getDescription() {
	return description;
    }

    @JsonProperty("description")
    public void setDescription(Object description) {
	this.description = description;
    }

    @JsonProperty("datetime")
    public int getDatetime() {
	return datetime;
    }

    @JsonProperty("datetime")
    public void setDatetime(int datetime) {
	this.datetime = datetime;
    }

    @JsonProperty("type")
    public String getType() {
	return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
	this.type = type;
    }

    @JsonProperty("animated")
    public boolean isAnimated() {
	return animated;
    }

    @JsonProperty("animated")
    public void setAnimated(boolean animated) {
	this.animated = animated;
    }

    @JsonProperty("width")
    public int getWidth() {
	return width;
    }

    @JsonProperty("width")
    public void setWidth(int width) {
	this.width = width;
    }

    @JsonProperty("height")
    public int getHeight() {
	return height;
    }

    @JsonProperty("height")
    public void setHeight(int height) {
	this.height = height;
    }

    @JsonProperty("size")
    public int getSize() {
	return size;
    }

    @JsonProperty("size")
    public void setSize(int size) {
	this.size = size;
    }

    @JsonProperty("views")
    public int getViews() {
	return views;
    }

    @JsonProperty("views")
    public void setViews(int views) {
	this.views = views;
    }

    @JsonProperty("bandwidth")
    public int getBandwidth() {
	return bandwidth;
    }

    @JsonProperty("bandwidth")
    public void setBandwidth(int bandwidth) {
	this.bandwidth = bandwidth;
    }

    @JsonProperty("vote")
    public Object getVote() {
	return vote;
    }

    @JsonProperty("vote")
    public void setVote(Object vote) {
	this.vote = vote;
    }

    @JsonProperty("favorite")
    public boolean isFavorite() {
	return favorite;
    }

    @JsonProperty("favorite")
    public void setFavorite(boolean favorite) {
	this.favorite = favorite;
    }

    @JsonProperty("nsfw")
    public Object getNsfw() {
	return nsfw;
    }

    @JsonProperty("nsfw")
    public void setNsfw(Object nsfw) {
	this.nsfw = nsfw;
    }

    @JsonProperty("section")
    public Object getSection() {
	return section;
    }

    @JsonProperty("section")
    public void setSection(Object section) {
	this.section = section;
    }

    @JsonProperty("account_url")
    public Object getAccountUrl() {
	return accountUrl;
    }

    @JsonProperty("account_url")
    public void setAccountUrl(Object accountUrl) {
	this.accountUrl = accountUrl;
    }

    @JsonProperty("account_id")
    public int getAccountId() {
	return accountId;
    }

    @JsonProperty("account_id")
    public void setAccountId(int accountId) {
	this.accountId = accountId;
    }

    @JsonProperty("is_ad")
    public boolean isIsAd() {
	return isAd;
    }

    @JsonProperty("is_ad")
    public void setIsAd(boolean isAd) {
	this.isAd = isAd;
    }

    @JsonProperty("in_most_viral")
    public boolean isInMostViral() {
	return inMostViral;
    }

    @JsonProperty("in_most_viral")
    public void setInMostViral(boolean inMostViral) {
	this.inMostViral = inMostViral;
    }

    @JsonProperty("tags")
    public List<Object> getTags() {
	return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<Object> tags) {
	this.tags = tags;
    }

    @JsonProperty("ad_type")
    public int getAdType() {
	return adType;
    }

    @JsonProperty("ad_type")
    public void setAdType(int adType) {
	this.adType = adType;
    }

    @JsonProperty("ad_url")
    public String getAdUrl() {
	return adUrl;
    }

    @JsonProperty("ad_url")
    public void setAdUrl(String adUrl) {
	this.adUrl = adUrl;
    }

    @JsonProperty("in_gallery")
    public boolean isInGallery() {
	return inGallery;
    }

    @JsonProperty("in_gallery")
    public void setInGallery(boolean inGallery) {
	this.inGallery = inGallery;
    }

    @JsonProperty("deletehash")
    public String getDeletehash() {
	return deletehash;
    }

    @JsonProperty("deletehash")
    public void setDeletehash(String deletehash) {
	this.deletehash = deletehash;
    }

    @JsonProperty("name")
    public String getName() {
	return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
	this.name = name;
    }

    @JsonProperty("link")
    public String getLink() {
	return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
	this.link = link;
    }

}
