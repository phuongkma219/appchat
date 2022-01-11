const mongoose = require('mongoose')

const UserSubjectRoomSchema = new mongoose.Schema(
  {
    user_id: {
      type: String,
      required: true,
    },
    subject_room_id: {
      type: String,
      required: true,
    },
    course_id: {
      type: String,
      required: true,
    },
    is_custom: {
      type: Boolean,
      required: true,
    },
  },
  {
    collection: 'user_subject_room',
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' },
  }
)

module.exports = mongoose.model('userSubjectRoom', UserSubjectRoomSchema)
