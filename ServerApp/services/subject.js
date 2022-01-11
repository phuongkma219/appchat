const SubjectModel = require('../models/subject')
const SubjectRoomModel = require('../models/subjectRoom')

const getSubject = () => SubjectModel.find().sort({ data: -1 })

const getSubjectById = (id) => SubjectModel.findById(id)

const getSubjectRoom = () => SubjectRoomModel.find().sort({ data: -1 })

const getSubjectRoomById = (id) => SubjectRoomModel.findById(id)

module.exports = {
  getSubject,
  getSubjectRoom,
  getSubjectById,
  getSubjectRoomById,
}
