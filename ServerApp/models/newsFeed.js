const mongoose = require('mongoose')

const NewsFeedSchema = new mongoose.Schema(
  {
    user: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'userInfo'
    },
    content: {}
    
  },
  {
    collection: 'news_feed',
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' },
  }
)

module.exports = mongoose.model('newsFeed', NewsFeedSchema)
