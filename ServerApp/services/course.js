const CourseModel = require('../models/course')

const getCourse = () => CourseModel.find().sort({ data: -1 })

const getCourseById = (id) => CourseModel.findById(id)

module.exports = {
  getCourse,
  getCourseById,
}
