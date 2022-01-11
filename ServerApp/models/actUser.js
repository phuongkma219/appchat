const mongoose = require('mongoose')

const ACTUserSchema = new mongoose.Schema(
  {
    username: {
      type: String,
      required: true,
    },
    session: {
      type: String,
    },
  },
  {
    collection: 'act_user',
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' },
  }
)

module.exports = mongoose.model('actUser', ACTUserSchema)
