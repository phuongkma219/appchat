const mongoose = require('mongoose')

const CourseSchema = new mongoose.Schema(
  {
    id: {
      type: String,
      required: true,
    },
    name: {
      type: String,
    },
  },
  {
    collection: 'course',
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' },
  }
)

module.exports = mongoose.model('course', CourseSchema)
