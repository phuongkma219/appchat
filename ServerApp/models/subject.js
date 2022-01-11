const mongoose = require('mongoose')

const SubjectSchema = new mongoose.Schema(
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
    collection: 'subject',
    timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' },
  }
)

module.exports = mongoose.model('subject', SubjectSchema)
