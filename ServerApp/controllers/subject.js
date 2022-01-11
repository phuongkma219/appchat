const SubjectService = require('../services/subject')

const apiResponse = require('../utils/apiResponse')

const Status = require('../constants/status')
const Message = require('../constants/message')

const getSubject = async (req, res) => {
  try {
    const subject = await Subject.getSubject()
    return res.status(Status.OK).json(apiResponse(Status.OK, 'Get subject successfully!!!', subject))
  } catch (e) {
    console.error(err.message)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

const getSubjectById = async (req, res) => {
  try {
    const subject = await Subject.getSubjectById(req.id)
    if (!subject) return res.status(400).json({ msg: 'not found' })
    res.json(subject)
  } catch (err) {
    console.error(err.message)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

const getSubjectRoom = async (req, res) => {
  try {
    const subject = await SubjectService.getSubjectRoom()
    res.json(subject)
  } catch (e) {
    console.error(err.message)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

const getSubjectRoomById = async (req, res) => {
  try {
    const subject = await SubjectService.getSubjectRoomById(req.id)
    if (!subject) return res.status(400).json({ msg: 'not found' })
    res.json(subject)
  } catch (err) {
    console.error(err.message)
    return res.status(Status.INTERNAL_SERVER_ERROR).send(Message.INTERNAL_SERVER_ERROR)
  }
}

module.exports = {
  getSubject,
  getSubjectRoom,
  getSubjectById,
  getSubjectRoomById,
}
