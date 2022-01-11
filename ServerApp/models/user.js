const mongoose = require('mongoose')

const UserSchema = new mongoose.Schema(
  {
    id: {
      type: String,
      required: true,
    },
    account: {
      type: String,
      required: true,
    },
    password: {
      type: String,
      required: true,
    },
    // act_user: {
    //   type: String,
    //   required: true,
    // },
    act_user: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'user',
    },
    user_type: {
      type: String,
      required: true,
    },
  },
  {
    collection: 'user',
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' },
  }
)

module.exports = mongoose.model('user', UserSchema)
