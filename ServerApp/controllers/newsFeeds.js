
const feedService =  require("../services/newFeed")
const apiResponse = require('../utils/apiResponse')

const userId = "609de69d53acbc7983517870"
const getFeeds = async (req,res)=>{
 
  let feeds = await feedService.getFeeds(req.query["last_time"],req.query["limit"])
  let response = apiResponse(true,0,"Thanh cong",feeds)
  console.log("get feeds",response)
  res.send(response)
}


const postFeed = async (req,res)=>{
  let feeds = await feedService.postFeed(req.body,userId)
  
  res.send(apiResponse(true,0,"Thanh cong",feeds))
}

const getMyFeeds = async (req,res)=>{
  let feeds = await feedService.getMyFeed(userId, req.query["last_time"],req.query["limit"])
  res.send(apiResponse(true,0,"Thanh cong",feeds))
}


module.exports = {
  getFeeds,
  postFeed,
  getMyFeeds
}