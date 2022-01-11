const mongoose = require('mongoose')

const UserAvgPointSchema = new mongoose.Schema(
  {
    id: {
      type: String,
      required: true,
    },
    course_id: {
      type: String,
      required: true,
    },
    semester: {
      type: Number,
      required: true,
    },
    TBTL10N1: {
      type: Number,
      required: true,
    },
    TBTL4N1: {
      type: Number,
      required: true,
    },
    TCTLN1: {
      type: Number,
      required: true,
    },
    TBC10N1: {
      type: Number,
      required: true,
    },
    TBC4N1: {
      type: Number,
      required: true,
    },
    TCN1: {
      type: Number,
      required: true,
    },
    TBTL10N2: {
      type: Number,
      required: true,
    },
    TBTL4N2: {
      type: Number,
      required: true,
    },
    TCTLN2: {
      type: Number,
      required: true,
    },
    TBC10N2: {
      type: Number,
      required: true,
    },
    TBC4N2: {
      type: Number,
      required: true,
    },
    TCN2: {
      type: Number,
      required: true,
    },
  },
  {
    collection: 'user_avg_point',
  }
)

module.exports = mongoose.model('userAvgPoint', UserAvgPointSchema)
