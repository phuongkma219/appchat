const mongoose = require('mongoose')

const SubjectRoomSchema = new mongoose.Schema(
  {
    id: {
      type: String,
      required: true,
    },
    subject_id: {
      type: String,
      required: true,
    },
    room_name: {
      type: String,
      required: true,
    },
    place: {
      type: String,
      required: true,
    },
    teacher: {
      type: String,
    },
    limit_student: {
      type: Number,
      required: true,
    },
    schedule_data: [],
    type: {
      type: String,
    },
  },
  {
    collection: 'subject_room',
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' },
  }
)

module.exports = mongoose.model('subjectRoom', SubjectRoomSchema)
