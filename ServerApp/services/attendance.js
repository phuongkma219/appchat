const AttendanceModel = require('../models/attendance')

const getAttendance = () => AttendanceModel.find().sort({ data: -1 })

const GetAttendanceById = (id) => AttendanceModel.findById(id)

module.exports = {
  getAttendance,
  GetAttendanceById,
}
