const NewsFeedModel = require('../models/newsFeed')
const UserInfoModel = require('../models/userInfo')
const FeedDTO = require("../models/DTO/FeedDTO")
const mongoose = require('mongoose')

const getFeeds = async (time = 0,limit = 10)=>{
  var list = []
  if (time > 0) {
    list = await  NewsFeedModel.find({createdAt: {"$lt": time} }).limit(limit).sort({"createdAt":"desc"}).populate("user") 
  } else {
    list = await NewsFeedModel.find().limit(limit).sort({"createdAt":"desc"}).populate("user")
  }
  return  list.map(m => FeedDTO.fromEntity(m))
}

const postFeed = async (feeds,userId) => {
  let model = NewsFeedModel({
    user:  mongoose.Types.ObjectId(userId),
    content:feeds
  })
 
return  await model.save()
}


const getMyFeed = async (userId, time = 0, limit = 10) => {
  var query =  null
  var userObjectId = mongoose.Types.ObjectId(userId)
  if (time > 0) {
    query =  NewsFeedModel.find({createdAt: {"$lt": time} , "user": userObjectId})
  } else {
    query =  NewsFeedModel.find({ "user": userObjectId})
  } 
  return  await query.limit(limit).sort({"createdAt":"desc"})
}

module.exports = {
  getFeeds,
  postFeed,
  getMyFeed
} 