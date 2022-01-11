const CourseService = require('../services/course')

const getCourse = async (req, res) => {
  try {
    const course = await CourseService.getCourse()
    res.json(course)
  } catch (e) {
    console.error(err.message)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

const getCourseById = async (req, res) => {
  try {
    const course = await CourseService.getCourseById(req.id)
    if (!course) return res.status(400).json({ msg: 'not found' })
    res.json(course)
  } catch (err) {
    console.error(err.message)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

module.exports = {
  getCourse,
  getCourseById,
}
