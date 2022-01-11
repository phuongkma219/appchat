const express = require('express')
const router = express.Router()
const UserController = require('../controllers/user')
const NewsFeeds = require('../controllers/newsFeeds')
const {checkAuth} = require("../middleware/auth")

router.get('/user-act',checkAuth, UserController.getUserAct)
router.get('/feeds', NewsFeeds.getFeeds)
router.get('/my_feeds', NewsFeeds.getMyFeeds)
router.post('/post_feed', NewsFeeds.postFeed)
 
router.all((req, res) => res.status(404).send('Unable to find the requested resource!'))

module.exports = router
