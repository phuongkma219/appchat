const mongoose = require('mongoose')

const UserPointSchema = new mongoose.Schema(
  {
    id: {
      type: String,
      required: true,
    },
    subject_id: {
      type: String,
      required: true,
    },
    course_id: {
      type: String,
      required: true,
    },
    evaluate: {
      type: String,
      required: true,
    },
    TP1: {
      type: Number,
      required: true,
    },
    TP2: {
      type: Number,
      required: true,
    },
    THI: {
      type: Number,
      required: true,
    },
    TKHP: {
      type: Number,
      required: true,
    },
  },
  {
    collection: 'user_point',
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' },
  }
)

module.exports = mongoose.model('userPoint', UserPointSchema)
