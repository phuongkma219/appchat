const mongoose = require('mongoose')

const AttendanceSchema = new mongoose.Schema(
  {
    id: {
      type: String,
      required: true,
    },
    subject_room_id: {
      type: String,
      required: true,
    },
    user_id: {
      type: String,
      required: true,
    },
    data: {
      type: Date,
      default: Date.now,
    },
    check: {
      type: Boolean,
      required: true,
    },
  },
  {
    collection: 'attendance',
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' },
  }
)

module.exports = mongoose.model('attendance', AttendanceSchema)
